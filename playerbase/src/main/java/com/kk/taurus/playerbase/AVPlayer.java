/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLoader;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.BaseInternalPlayer;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.player.IPlayerProxy;
import com.kk.taurus.playerbase.player.OnBufferingListener;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.TimerCounterProxy;
import com.kk.taurus.playerbase.record.PlayValueGetter;
import com.kk.taurus.playerbase.record.RecordProxyPlayer;

/**
 * Created by Taurus on 2018/3/17.
 */

public final class AVPlayer implements IPlayer{

    private final String TAG = "AVPlayer";
    //decoder instance , default is MediaPlayer
    //You can access other decoders
    private BaseInternalPlayer mInternalPlayer;

    private IDataProvider mDataProvider;

    //you can set url,uri,headers etc.
    private DataSource mDataSource;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;
    private OnBufferingListener mOnBufferingListener;

    private IDataProvider.OnProviderListener mOnProviderListener;

    private TimerCounterProxy mTimerCounterProxy;

    private int mDecoderPlanId;

    private float mVolumeLeft = -1,mVolumeRight = -1;

    private IPlayerProxy mRecordProxyPlayer;

    public AVPlayer(){
        //default load config plan id.
        this(PlayerConfig.getDefaultPlanId());
    }

    /**
     * setting a decoder plan id for init instance.
     * @param decoderPlanId
     */
    public AVPlayer(int decoderPlanId){
        handleRecordProxy();
        //init timer counter proxy.
        mTimerCounterProxy = new TimerCounterProxy(1000);
        //init internal player instance.
        loadInternalPlayer(decoderPlanId);
    }

    private void handleRecordProxy() {
        if(PlayerConfig.isPlayRecordOpen()){
            mRecordProxyPlayer = new RecordProxyPlayer(new PlayValueGetter() {
                @Override
                public int getCurrentPosition() {
                    return AVPlayer.this.getCurrentPosition();
                }

                @Override
                public int getBufferPercentage() {
                    return AVPlayer.this.getBufferPercentage();
                }

                @Override
                public int getDuration() {
                    return AVPlayer.this.getDuration();
                }
                @Override
                public int getState() {
                    return AVPlayer.this.getState();
                }
            });
        }
    }

    /**
     * init decoder instance according to your config,
     * if init failure, It's likely that your configuration is wrong.
     */
    private void loadInternalPlayer(int decoderPlanId) {
        mDecoderPlanId = decoderPlanId;
        //if internal player not null, destroy it.
        destroy();
        //loader decoder instance from config.
        mInternalPlayer = PlayerLoader.loadInternalPlayer(decoderPlanId);
        if(mInternalPlayer==null)
            throw new RuntimeException(
                    "init decoder instance failure, please check your configuration" +
                            ", maybe your config classpath not found.");
        DecoderPlan plan = PlayerConfig.getPlan(mDecoderPlanId);
        if(plan!=null){
            PLog.d(TAG,"=============================");
            PLog.d(TAG,"DecoderPlanInfo : planId      = " + plan.getIdNumber());
            PLog.d(TAG,"DecoderPlanInfo : classPath   = " + plan.getClassPath());
            PLog.d(TAG,"DecoderPlanInfo : desc        = " + plan.getDesc());
            PLog.d(TAG,"=============================");
        }
    }

    /**
     * With this method, you can switch the decoding plan,
     * this call will be recreate internal player instance.
     * and the subsequent operations after switching must be processed by yourself,
     * such as resetting the data to play and so on.
     * after switch, if you want get current planId,
     * you can get it by {@link PlayerConfig#getDefaultPlanId()}
     *
     * @param decoderPlanId the planId is your configuration ids or default id.
     * @return Whether or not to switch to success.
     *         if return false, maybe your incoming planId is the same as the current planId
     *         or your incoming planId is illegal param.
     *         return true is switch decoder success.
     *
     */
    public boolean switchDecoder(int decoderPlanId){
        if(mDecoderPlanId == decoderPlanId){
            PLog.e(this.getClass().getSimpleName(),
                    "@@Your incoming planId is the same as the current use planId@@");
            return false;
        }
        if(PlayerConfig.isLegalPlanId(decoderPlanId)){
            //and reload internal player instance.
            loadInternalPlayer(decoderPlanId);
            return true;
        }else{
            throw new IllegalArgumentException("Illegal plan id = "
                    + decoderPlanId + ", please check your config!");
        }
    }

    /**
     * see {@link IPlayer#option(int, Bundle)}
     * @param code the code value custom yourself.
     * @param bundle deliver some data if you need.
     */
    @Override
    public void option(int code, Bundle bundle) {
        mInternalPlayer.option(code, bundle);
    }

    /**
     * setting timer proxy state. default open.
     * @param useTimerProxy
     */
    public void setUseTimerProxy(boolean useTimerProxy) {
        mTimerCounterProxy.setUseProxy(useTimerProxy);
    }

    private void initListener() {
        mTimerCounterProxy.setOnCounterUpdateListener(mOnCounterUpdateListener);
        if(mInternalPlayer!=null){
            mInternalPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
            mInternalPlayer.setOnErrorEventListener(mInternalErrorEventListener);
            mInternalPlayer.setOnBufferingListener(mInternalBufferingListener);
        }
    }

    //destroy some listener
    private void resetListener(){
        mTimerCounterProxy.setOnCounterUpdateListener(null);
        if(mInternalPlayer!=null){
            mInternalPlayer.setOnPlayerEventListener(null);
            mInternalPlayer.setOnErrorEventListener(null);
            mInternalPlayer.setOnBufferingListener(null);
        }
    }

    private TimerCounterProxy.OnCounterUpdateListener mOnCounterUpdateListener =
            new TimerCounterProxy.OnCounterUpdateListener() {
        @Override
        public void onCounter() {
            int curr = getCurrentPosition();
            int duration = getDuration();
            int bufferPercentage = getBufferPercentage();
            //check valid data.
            if(duration <= 0 && !isLive())
                return;
            onTimerUpdateEvent(curr, duration, bufferPercentage);
        }
    };

    private void onTimerUpdateEvent(int curr, int duration, int bufferPercentage) {
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_ARG1, curr);
        bundle.putInt(EventKey.INT_ARG2, duration);
        bundle.putInt(EventKey.INT_ARG3, bufferPercentage);
        callBackPlayEventListener(
                OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE, bundle);
    }

    private OnPlayerEventListener mInternalPlayerEventListener =
            new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            mTimerCounterProxy.proxyPlayEvent(eventCode, bundle);
            if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED){
                //when prepared set volume value
                if(mVolumeLeft >= 0 || mVolumeRight >= 0){
                    mInternalPlayer.setVolume(mVolumeLeft, mVolumeRight);
                }
            }else if(eventCode==OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE){
                int duration = getDuration();
                int bufferPercentage = getBufferPercentage();
                //check valid data.
                if(duration <= 0 && !isLive())
                    return;
                onTimerUpdateEvent(duration, duration, bufferPercentage);
            }
            if(isPlayRecordOpen())
                mRecordProxyPlayer.onPlayerEvent(eventCode, bundle);
            callBackPlayEventListener(eventCode, bundle);
        }
    };

    private OnErrorEventListener mInternalErrorEventListener =
            new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            mTimerCounterProxy.proxyErrorEvent(eventCode, bundle);
            if(isPlayRecordOpen())
                mRecordProxyPlayer.onErrorEvent(eventCode, bundle);
            callBackErrorEventListener(eventCode, bundle);
        }
    };

    private OnBufferingListener mInternalBufferingListener =
            new OnBufferingListener() {
        @Override
        public void onBufferingUpdate(int bufferPercentage, Bundle extra) {
            if(mOnBufferingListener!=null)
                mOnBufferingListener.onBufferingUpdate(bufferPercentage, extra);
        }
    };

    //must last callback event listener , because bundle will be recycle after callback.
    private void callBackPlayEventListener(int eventCode, Bundle bundle) {
        if(mOnPlayerEventListener!=null)
            mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
    }

    //must last callback event listener , because bundle will be recycle after callback.
    private void callBackErrorEventListener(int eventCode, Bundle bundle){
        if(mOnErrorEventListener!=null)
            mOnErrorEventListener.onErrorEvent(eventCode, bundle);
    }

    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    @Override
    public void setOnBufferingListener(OnBufferingListener onBufferingListener) {
        this.mOnBufferingListener = onBufferingListener;
    }

    public void setOnProviderListener(IDataProvider.OnProviderListener onProviderListener) {
        this.mOnProviderListener = onProviderListener;
    }

    /**
     * if you need , you can set a data provider.{@link IDataProvider}
     * you need call this method before {@link this#setDataSource(DataSource)}.
     * @param dataProvider
     */
    public void setDataProvider(IDataProvider dataProvider){
        if(mDataProvider!=null)
            mDataProvider.destroy();
        this.mDataProvider = dataProvider;
        if(mDataProvider!=null)
            this.mDataProvider.setOnProviderListener(mInternalProviderListener);
    }

    private IDataProvider.OnProviderListener mInternalProviderListener =
            new IDataProvider.OnProviderListener() {
        @Override
        public void onProviderDataStart() {
            if(mOnProviderListener!=null)
                mOnProviderListener.onProviderDataStart();
            callBackPlayEventListener(
                    OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START, null);
        }

        @Override
        public void onProviderDataSuccess(int code, Bundle bundle) {
            if(mOnProviderListener!=null)
                mOnProviderListener.onProviderDataSuccess(code, bundle);
            switch (code){
                //on data provider load data success,need set data to decoder player.
                case IDataProvider.PROVIDER_CODE_SUCCESS_MEDIA_DATA:
                    if(bundle!=null){
                        Object obj = bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                        if(obj==null || !(obj instanceof DataSource)){
                            throw new RuntimeException("provider media success SERIALIZABLE_DATA must type of DataSource!");
                        }
                        DataSource data = (DataSource) obj;
                        PLog.d(TAG,"onProviderDataSuccessMediaData : DataSource = " + data);
                        interPlayerSetDataSource(data);
                        internalPlayerStart(data.getStartPos());
                        //success video data call back.
                        callBackPlayEventListener(
                                OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_SUCCESS, bundle);
                    }
                    break;
                default:
                    //success other code ,for example ,maybe IDataProvider.PROVIDER_CODE_EXTRA_DATA
                    //Usually, these state codes are customizable by the user.
                    callBackPlayEventListener(code, bundle);
                    break;
            }
        }

        @Override
        public void onProviderError(int code, Bundle bundle) {
            PLog.e(TAG,"onProviderError : code = " + code + ", bundle = " + bundle);
            if(mOnProviderListener!=null)
                mOnProviderListener.onProviderError(code, bundle);
            //need recreate a new bundle, because a bundle will be recycle after call back.
            Bundle errorBundle;
            if(bundle!=null){
                errorBundle = new Bundle(bundle);
            }else{
                errorBundle = new Bundle();
            }
            errorBundle.putInt(EventKey.INT_DATA,code);
            //call back player event
            callBackPlayEventListener(code, bundle);
            //call back error event
            callBackErrorEventListener(
                    OnErrorEventListener.ERROR_EVENT_DATA_PROVIDER_ERROR,errorBundle);
        }
    };

    @Override
    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
        //when data source update, attach listener.
        initListener();
        //if not set DataProvider,will be set data to decoder.
        if(!useProvider())
            interPlayerSetDataSource(dataSource);

    }

    boolean isLive(){
        return mDataSource!=null && mDataSource.isLive();
    }

    private void interPlayerSetDataSource(DataSource dataSource){
        if(isPlayerAvailable()){
            if(isPlayRecordOpen())
                mRecordProxyPlayer.onDataSourceReady(dataSource);
            mInternalPlayer.setDataSource(dataSource);
        }
    }

    @Override
    public void start() {
        int record = getRecord(mDataSource);
        if(useProvider()){
            mDataSource.setStartPos(record);
            mDataProvider.handleSourceData(mDataSource);
        }else{
            internalPlayerStart(record);
        }
    }

    int getRecord(DataSource dataSource){
        if(isPlayRecordOpen() && dataSource!=null)
            return mRecordProxyPlayer.getRecord(dataSource);
        return mDataSource!=null?mDataSource.getStartPos():0;
    }

    boolean isPlayRecordOpen(){
        return PlayerConfig.isPlayRecordOpen() && mRecordProxyPlayer!=null;
    }

    /**
     * If you want to start play at a specified time,
     * please set this method.
     * @param msc
     */
    @Override
    public void start(int msc) {
        if(useProvider()){
            mDataSource.setStartPos(msc);
            mDataProvider.handleSourceData(mDataSource);
        }else{
            internalPlayerStart(msc);
        }
    }

    private void internalPlayerStart(int msc){
        if(isPlayerAvailable())
            mInternalPlayer.start(msc);
    }

    private boolean isPlayerAvailable(){
        return mInternalPlayer!=null;
    }

    private boolean useProvider(){
        return mDataProvider!=null;
    }

    public void rePlay(int msc){
        if(!useProvider() && mDataSource!=null){
            interPlayerSetDataSource(mDataSource);
            internalPlayerStart(msc);
        }else if(useProvider() && mDataSource!=null){
            mDataSource.setStartPos(msc);
            mDataProvider.handleSourceData(mDataSource);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(isPlayerAvailable())
            mInternalPlayer.setDisplay(surfaceHolder);
    }

    @Override
    public void setSurface(Surface surface) {
        if(isPlayerAvailable())
            mInternalPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float left, float right) {
        mVolumeLeft = left;
        mVolumeRight = right;
        if(isPlayerAvailable())
            mInternalPlayer.setVolume(left, right);
    }

    @Override
    public void setSpeed(float speed) {
        if(isPlayerAvailable())
            mInternalPlayer.setSpeed(speed);
    }

    @Override
    public boolean isPlaying() {
        if(isPlayerAvailable())
            return mInternalPlayer.isPlaying();
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(isPlayerAvailable())
            return mInternalPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public int getDuration() {
        if(isPlayerAvailable())
            return mInternalPlayer.getDuration();
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(isPlayerAvailable())
            return mInternalPlayer.getAudioSessionId();
        return 0;
    }

    @Override
    public int getVideoWidth() {
        if(isPlayerAvailable())
            return mInternalPlayer.getVideoWidth();
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if(isPlayerAvailable())
            return mInternalPlayer.getVideoHeight();
        return 0;
    }

    @Override
    public int getState() {
        if(isPlayerAvailable())
            return mInternalPlayer.getState();
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(isPlayerAvailable())
            return mInternalPlayer.getBufferPercentage();
        return 0;
    }

    @Override
    public void pause() {
        if(isPlayerAvailable())
            mInternalPlayer.pause();
    }

    @Override
    public void resume() {
        if(isPlayerAvailable())
            mInternalPlayer.resume();
    }

    @Override
    public void seekTo(int msc) {
        if(isPlayerAvailable())
            mInternalPlayer.seekTo(msc);
    }

    @Override
    public void stop() {
        if(isPlayRecordOpen())
            mRecordProxyPlayer.onIntentStop();
        if(useProvider())
            mDataProvider.cancel();
        if(isPlayerAvailable())
            mInternalPlayer.stop();
    }

    @Override
    public void reset() {
        if(isPlayRecordOpen())
            mRecordProxyPlayer.onIntentReset();
        if(useProvider())
            mDataProvider.cancel();
        if(isPlayerAvailable())
            mInternalPlayer.reset();
    }

    @Override
    public void destroy() {
        if(isPlayRecordOpen())
            mRecordProxyPlayer.onIntentDestroy();
        if(useProvider())
            mDataProvider.destroy();
        if(isPlayerAvailable())
            mInternalPlayer.destroy();
        if(mTimerCounterProxy!=null)
            mTimerCounterProxy.cancel();
        resetListener();
    }
}
