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

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.config.PlayerLoader;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.player.BaseInternalPlayer;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.TimerCounterProxy;

/**
 * Created by Taurus on 2018/3/17.
 */

public final class AVPlayer implements IPlayer{

    //decoder instance , default is MediaPlayer
    //You can access other decoders
    private BaseInternalPlayer mInternalPlayer;

    private IDataProvider mDataProvider;

    //you can set url,uri,headers etc.
    private DataSource mDataSource;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;

    private IDataProvider.OnProviderListener mOnProviderListener;

    private TimerCounterProxy mTimerCounterProxy;

    public AVPlayer(Context context){
        //loader decoder instance from config.
        mInternalPlayer = PlayerLoader.loadInternalPlayer(context);
        //init timer counter proxy.
        mTimerCounterProxy = new TimerCounterProxy(1000);
    }

    private void initListener() {
        mTimerCounterProxy.setOnCounterUpdateListener(mOnCounterUpdateListener);
        if(mInternalPlayer!=null){
            mInternalPlayer.setOnPlayerEventListener(mInternalPlayerEventListener);
            mInternalPlayer.setOnErrorEventListener(mInternalErrorEventListener);
        }
    }

    //destroy some listener
    private void resetListener(){
        mTimerCounterProxy.setOnCounterUpdateListener(null);
        if(mInternalPlayer!=null){
            mInternalPlayer.setOnPlayerEventListener(null);
            mInternalPlayer.setOnErrorEventListener(null);
        }
    }

    private TimerCounterProxy.OnCounterUpdateListener mOnCounterUpdateListener =
            new TimerCounterProxy.OnCounterUpdateListener() {
        @Override
        public void onCounter() {
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_ARG1, getCurrentPosition());
            bundle.putInt(EventKey.INT_ARG2, getDuration());
            callBackPlayEventListener(OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE, bundle);
        }
    };

    private OnPlayerEventListener mInternalPlayerEventListener = new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            mTimerCounterProxy.proxyPlayEvent(eventCode, bundle);
            callBackPlayEventListener(eventCode, bundle);
        }
    };

    private OnErrorEventListener mInternalErrorEventListener = new OnErrorEventListener() {
        @Override
        public void onErrorEvent(int eventCode, Bundle bundle) {
            mTimerCounterProxy.proxyErrorEvent(eventCode, bundle);
            if(mOnErrorEventListener!=null)
                mOnErrorEventListener.onErrorEvent(eventCode, bundle);
        }
    };

    private void callBackPlayEventListener(int eventCode, Bundle bundle) {
        if(mOnPlayerEventListener!=null)
            mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
    }

    @Override
    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    @Override
    public void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
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

    private IDataProvider.OnProviderListener mInternalProviderListener = new IDataProvider.OnProviderListener() {
        @Override
        public void onProviderDataStart() {
            if(mOnProviderListener!=null)
                mOnProviderListener.onProviderDataStart();
            callBackPlayEventListener(OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START, null);
        }

        @Override
        public void onProviderDataSuccess(int code, Bundle bundle) {
            if(mOnProviderListener!=null)
                mOnProviderListener.onProviderDataSuccess(code, bundle);
            switch (code){
                //on data provider load data success,need set data to decoder player.
                case IDataProvider.PROVIDER_CODE_SUCCESS_MEDIA_DATA:
                    if(bundle!=null){
                        DataSource data = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                        if(data!=null){
                            interPlayerSetDataSource(data);
                            internalPlayerStart(data.getStartPos());
                        }
                    }
                    break;
            }
            //must last callback event listener , because bundle will be recycle after callback.
            callBackPlayEventListener(OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_SUCCESS, bundle);
        }

        @Override
        public void onProviderError(int code, Bundle bundle) {
            if(mOnProviderListener!=null)
                mOnProviderListener.onProviderError(code, bundle);
            callBackPlayEventListener(OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR, bundle);
        }
    };

    @Override
    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
        initListener();
        if(!useProvider())
            interPlayerSetDataSource(dataSource);

    }

    private void interPlayerSetDataSource(DataSource dataSource){
        if(isPlayerAvailable())
            mInternalPlayer.setDataSource(dataSource);
    }

    @Override
    public void start() {
        if(useProvider())
            mDataProvider.handleSourceData(mDataSource);
        else
            internalPlayerStart(0);
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
        if(isPlayerAvailable())
            mInternalPlayer.setVolume(left, right);
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
        if(useProvider())
            mDataProvider.cancel();
        if(isPlayerAvailable())
            mInternalPlayer.stop();
    }

    @Override
    public void reset() {
        if(useProvider())
            mDataProvider.cancel();
        if(isPlayerAvailable())
            mInternalPlayer.reset();
    }

    @Override
    public void destroy() {
        if(useProvider())
            mDataProvider.destroy();
        if(isPlayerAvailable())
            mInternalPlayer.destroy();
        resetListener();
    }
}
