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

package com.kk.taurus.playerbase.widget.plan;

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.inter.IPlayerManager;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.utils.PLog;
import com.kk.taurus.playerbase.widget.BaseSingleDecoderPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/11/19.
 * 该类为解码器(或VideoView)的单利管理类，由框架内部调用。请不要尝试直接调用该类中的方法。
 */

public class InternalPlayerManager implements IPlayer, IPlayerManager {

    private final String TAG = "InternalPlayerManager";
    private static InternalPlayerManager instance;

    private InternalPlayerManager(){}

    public static InternalPlayerManager get(){
        if(null==instance){
            synchronized (InternalPlayerManager.class){
                if(null==instance){
                    instance = new InternalPlayerManager();
                }
            }
        }
        return instance;
    }

    private List<OnInternalPlayerListener> mOnInternalPlayerListeners;
    private MixMediaPlayer mMediaPlayer;
    private MixRenderWidget mVideoView;
    private int mWidgetMode;
    private VideoData mDataSource;
    private Context mApplicationContext;

    public void setOnInternalPlayerListener(OnInternalPlayerListener onInternalPlayerListener) {
        if(mOnInternalPlayerListeners==null){
            mOnInternalPlayerListeners = new ArrayList<>();
        }
        if(mOnInternalPlayerListeners.contains(onInternalPlayerListener))
            return;
        this.mOnInternalPlayerListeners.add(onInternalPlayerListener);
    }

    public void removeOnInternalPlayerListener(OnInternalPlayerListener onInternalPlayerListener){
        if(mOnInternalPlayerListeners!=null){
            mOnInternalPlayerListeners.remove(onInternalPlayerListener);
        }
    }

    public void updateWidgetMode(Context context, int widgetMode){
        //use application context.
        mApplicationContext = context.getApplicationContext();
        boolean isModeChange = mWidgetMode!=widgetMode;
        mWidgetMode = widgetMode;
        if(isModeChange || isNeedInitInternalPlayer())
            initInternalPlayer(mApplicationContext);
    }

    public int getWidgetMode(){
        return mWidgetMode;
    }

    private boolean isNeedInitInternalPlayer(){
        return (mMediaPlayer==null && mWidgetMode==WIDGET_MODE_DECODER)
                || (mVideoView==null && mWidgetMode==WIDGET_MODE_VIDEO_VIEW);
    }

    private void initInternalPlayer(Context context){
        destroy();
        if(mWidgetMode==WIDGET_MODE_VIDEO_VIEW && mVideoView==null){
            mVideoView = createVideoView(context);
            attachVideoViewListener();
        }else if(mWidgetMode==WIDGET_MODE_DECODER && mMediaPlayer==null){
            mMediaPlayer = createMediaPlayer(context);
            attachMediaPlayerListener();
        }
    }

    private MixMediaPlayer createMediaPlayer(Context context){
        PLog.d(TAG,"createMediaPlayer ...");
        return new MixMediaPlayer(context);
    }

    private MixRenderWidget createVideoView(Context context){
        PLog.d(TAG,"createVideoView ...");
        return new MixRenderWidget(context);
    }

    private void attachMediaPlayerListener(){
        if(mMediaPlayer!=null){
            mMediaPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    if(mOnInternalPlayerListeners==null)
                        return;
                    for(OnInternalPlayerListener onInternalPlayerListener : mOnInternalPlayerListeners){
                        if(onInternalPlayerListener!=null){
                            onInternalPlayerListener.onInternalPlayerEvent(eventCode, bundle);
                        }
                    }
                }
            });
            mMediaPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode, Bundle bundle) {
                    if(mOnInternalPlayerListeners==null)
                        return;
                    for(OnInternalPlayerListener onInternalPlayerListener : mOnInternalPlayerListeners){
                        if(onInternalPlayerListener!=null){
                            onInternalPlayerListener.onInternalErrorEvent(errorCode, bundle);
                        }
                    }
                }
            });
        }
    }

    private void detachMediaPlayerListener(){
        if(mMediaPlayer!=null){
            mMediaPlayer.setOnPlayerEventListener(null);
            mMediaPlayer.setOnErrorListener(null);
        }
    }

    private void attachVideoViewListener(){
        if(mVideoView!=null){
            mVideoView.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    if(mOnInternalPlayerListeners==null)
                        return;
                    for(OnInternalPlayerListener onInternalPlayerListener : mOnInternalPlayerListeners){
                        if(onInternalPlayerListener!=null){
                            onInternalPlayerListener.onInternalPlayerEvent(eventCode, bundle);
                        }
                    }
                }
            });
            mVideoView.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode, Bundle bundle) {
                    if(mOnInternalPlayerListeners==null)
                        return;
                    for(OnInternalPlayerListener onInternalPlayerListener : mOnInternalPlayerListeners){
                        if(onInternalPlayerListener!=null){
                            onInternalPlayerListener.onInternalErrorEvent(errorCode, bundle);
                        }
                    }
                }
            });
        }
    }

    private void detachVideoViewListener(){
        if(mVideoView!=null){
            mVideoView.setOnPlayerEventListener(null);
            mVideoView.setOnErrorListener(null);
        }
    }

    private void destroyMediaPlayer(){
        if(mMediaPlayer!=null){
            mMediaPlayer.destroy();
            detachMediaPlayerListener();
            mMediaPlayer = null;
        }
    }

    private void destroyVideoView(){
        if(mVideoView!=null){
            mVideoView.destroy();
            detachVideoViewListener();
            mVideoView = null;
        }
    }

    private boolean isDecoderMode(){
        checkWidget();
        return mWidgetMode==WIDGET_MODE_DECODER && mMediaPlayer!=null;
    }

    private boolean isVideoViewMode(){
        checkWidget();
        return mWidgetMode==WIDGET_MODE_VIDEO_VIEW && mVideoView!=null;
    }

    private void checkWidget(){
        if((mMediaPlayer==null && mWidgetMode==WIDGET_MODE_DECODER)
                || (mVideoView==null && mWidgetMode==WIDGET_MODE_VIDEO_VIEW)){
            initInternalPlayer(mApplicationContext);
        }
    }

    @Override
    public void setDataProvider(IDataProvider dataProvider) {
        if(isDecoderMode()){
            mMediaPlayer.setDataProvider(dataProvider);
        }else if(isVideoViewMode()){
            mVideoView.setDataProvider(dataProvider);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(isDecoderMode()){
            mMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        if(isDecoderMode()){
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void updatePlayerType(int type) {
        if(isDecoderMode()){
            mMediaPlayer.setDecoderType(type);
        }else if(isVideoViewMode()){
            mVideoView.setRenderType(type);
        }
    }

    @Override
    public int getPlayerType() {
        if(isDecoderMode()){
            return mMediaPlayer.getDecoderType();
        }else if(isVideoViewMode()){
            return mVideoView.getRenderType();
        }
        return 0;
    }

    public boolean isDataSourceAvailable(){
        return mDataSource!=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        this.mDataSource = data;
        if(isDecoderMode()){
            mMediaPlayer.setDataSource(data);
        }else if(isVideoViewMode()){
            mVideoView.setDataSource(data);
        }
    }

    @Override
    public void start() {
        if(isDecoderMode()){
            mMediaPlayer.start();
        }else if(isVideoViewMode()){
            mVideoView.start();
        }
    }

    @Override
    public void start(int msc) {
        if(isDecoderMode()){
            mMediaPlayer.start(msc);
        }else if(isVideoViewMode()){
            mMediaPlayer.start(msc);
        }
    }

    @Override
    public void pause() {
        if(isDecoderMode()){
            mMediaPlayer.pause();
        }else if(isVideoViewMode()){
            mVideoView.pause();
        }
    }

    @Override
    public void resume() {
        if(isDecoderMode()){
            mMediaPlayer.resume();
        }else if(isVideoViewMode()){
            mVideoView.resume();
        }
    }

    @Override
    public void seekTo(int msc) {
        if(isDecoderMode()){
            mMediaPlayer.seekTo(msc);
        }else if(isVideoViewMode()){
            mVideoView.seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(isDecoderMode()){
            mMediaPlayer.stop();
        }else if(isVideoViewMode()){
            mVideoView.stop();
        }
    }

    @Override
    public void reset() {
        if(isDecoderMode()){
            mMediaPlayer.reset();
        }else if(isVideoViewMode()){
            mVideoView.reset();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(isDecoderMode()){
            mMediaPlayer.rePlay(msc);
        }else if(isVideoViewMode()){
            mVideoView.rePlay(msc);
        }
    }

    @Override
    public boolean isPlaying() {
        if(isDecoderMode()){
            return mMediaPlayer.isPlaying();
        }else if(isVideoViewMode()){
            return mVideoView.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(isDecoderMode()){
            return mMediaPlayer.getCurrentPosition();
        }else if(isVideoViewMode()){
            return mVideoView.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(isDecoderMode()){
            return mMediaPlayer.getDuration();
        }else if(isVideoViewMode()){
            return mVideoView.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(isDecoderMode()){
            return mMediaPlayer.getBufferPercentage();
        }else if(isVideoViewMode()){
            return mVideoView.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(isDecoderMode()){
            return mMediaPlayer.getAudioSessionId();
        }else if(isVideoViewMode()){
            return mVideoView.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(isDecoderMode()){
            return mMediaPlayer.getStatus();
        }else if(isVideoViewMode()){
            return mVideoView.getStatus();
        }
        return 0;
    }

    @Override
    public int getVideoWidth() {
        if(isDecoderMode()){
            return mMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if(isDecoderMode()){
            return mMediaPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(isDecoderMode()){
            return mMediaPlayer.getCurrentDefinition();
        }else if(isVideoViewMode()){
            return mVideoView.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(isDecoderMode()){
            return mMediaPlayer.getVideoDefinitions();
        }else if(isVideoViewMode()){
            return mVideoView.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(isDecoderMode()){
            mMediaPlayer.changeVideoDefinition(rate);
        }else if(isVideoViewMode()){
            mVideoView.changeVideoDefinition(rate);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(isVideoViewMode()){
            mVideoView.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        if(isDecoderMode()){
            mMediaPlayer.setDecodeMode(decodeMode);
        }else if(isVideoViewMode()){
            mVideoView.setDecodeMode(decodeMode);
        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if(isDecoderMode()){
            mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public void setViewType(ViewType viewType) {
        if(isVideoViewMode()){
            mVideoView.setViewType(viewType);
        }
    }

    @Override
    public ViewType getViewType() {
        if(isVideoViewMode()){
            return mVideoView.getViewType();
        }
        return null;
    }

    @Override
    public AspectRatio getAspectRatio() {
        if(isVideoViewMode()){
            return mVideoView.getAspectRatio();
        }
        return null;
    }

    @Override
    public DecodeMode getDecodeMode() {
        if(isDecoderMode()){
            return mMediaPlayer.getDecodeMode();
        }else if(isVideoViewMode()){
            return mVideoView.getDecodeMode();
        }
        return null;
    }

    @Override
    public View getRenderView() {
        if(isVideoViewMode()){
            return mVideoView.getRenderView();
        }
        return null;
    }

    @Override
    public void destroy() {
        destroyMediaPlayer();
        destroyVideoView();
    }

    public interface OnInternalPlayerListener{
        void onInternalPlayerEvent(int eventCode, Bundle bundle);
        void onInternalErrorEvent(int errorCode, Bundle bundle);
    }

    //-------------------IPlayerManager-------------------

    /***
     * 该类为对解码核心的单利管理类。由于对于{@link BaseSingleDecoderPlayer}
     * 来说解码核心是单一的实例，也就是说这个解码核心只能同时存在一个操纵者，销毁的容器会被抛弃。
     * 新创建的容器被认为是解码核心的宿主对象，也就是拥有对该单利解码器的控制权。
     */

    private List<IPlayer> mHostArrays = new ArrayList<>(2);

    private boolean isContainPlayer(IPlayer host){
        if(mHostArrays==null || mHostArrays.size()<=0)
            return false;
        return mHostArrays.contains(host);
    }

    /**
     * 当容器执行
     * {@link BaseSingleDecoderPlayer#setDataSource(VideoData)}
     * {@link BaseSingleDecoderPlayer#initBaseInfo(Context)}方法时该方法会被调用
     * @param host
     */
    @Override
    public void attachPlayer(IPlayer host) {
        if(!isContainPlayer(host)){
            this.mHostArrays.add(host);
        }
        PLog.d(TAG,"attachPlayer ...");
    }

    /**
     * 当容器{@link BaseSingleDecoderPlayer#destroyContainer()} 被销毁时该方法会被调用
     * @param host
     */
    @Override
    public void detachPlayer(IPlayer host) {
        this.mHostArrays.remove(host);
        PLog.d(TAG,"detachPlayer ...");
    }

    @Override
    public void rePlay(IPlayer host, int msc) {
        if(isContainPlayer(host)){
            rePlay(msc);
        }
    }

    @Override
    public void updatePlayerType(IPlayer host, int type) {
        if(isContainPlayer(host)){
            updatePlayerType(type);
        }
    }

    @Override
    public int getPlayerType(IPlayer host) {
        if(isContainPlayer(host)){
            return getPlayerType();
        }
        return 0;
    }

    @Override
    public void setViewType(IPlayer host, ViewType viewType) {
        if(isContainPlayer(host)){
            setViewType(viewType);
        }
    }

    @Override
    public void setAspectRatio(IPlayer host, AspectRatio aspectRatio) {
        if(isContainPlayer(host)){
            setAspectRatio(aspectRatio);
        }
    }

    @Override
    public ViewType getViewType(IPlayer host) {
        if(isContainPlayer(host)){
            return getViewType();
        }
        return null;
    }

    @Override
    public AspectRatio getAspectRatio(IPlayer host) {
        if(isContainPlayer(host)){
            return getAspectRatio();
        }
        return null;
    }

    @Override
    public View getRenderView(IPlayer host) {
        if(isContainPlayer(host)){
            return getRenderView();
        }
        return null;
    }

    @Override
    public void setDataSource(IPlayer host, VideoData data) {
        if(isContainPlayer(host)){
            setDataSource(data);
        }
    }

    @Override
    public void start(IPlayer host) {
        if(isContainPlayer(host)){
            start();
        }
    }

    @Override
    public void start(IPlayer host, int msc) {
        if(isContainPlayer(host)){
            start(msc);
        }
    }

    @Override
    public void pause(IPlayer host) {
        if(isContainPlayer(host)){
            pause();
        }
    }

    @Override
    public void resume(IPlayer host) {
        if(isContainPlayer(host)){
            resume();
        }
    }

    @Override
    public void seekTo(IPlayer host, int msc) {
        if(isContainPlayer(host)){
            seekTo(msc);
        }
    }

    @Override
    public void stop(IPlayer host) {
        if(isContainPlayer(host)){
            stop();
        }
    }

    @Override
    public void reset(IPlayer host) {
        if(isContainPlayer(host)){
            reset();
        }
    }

    @Override
    public boolean isPlaying(IPlayer host) {
        if(isContainPlayer(host)){
            return isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition(IPlayer host) {
        if(isContainPlayer(host)){
            return getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration(IPlayer host) {
        if(isContainPlayer(host)){
            return getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage(IPlayer host) {
        if(isContainPlayer(host)){
            return getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId(IPlayer host) {
        if(isContainPlayer(host)){
            return getAudioSessionId();
        }
        return 0;
    }

    @Override
    public int getStatus(IPlayer host) {
        if(isContainPlayer(host)){
            return getStatus();
        }
        return 0;
    }

    @Override
    public int getVideoWidth(IPlayer host) {
        if(isContainPlayer(host)){
            return getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight(IPlayer host) {
        if(isContainPlayer(host)){
            return getVideoHeight();
        }
        return 0;
    }

    @Override
    public void setVolume(IPlayer host, float leftVolume, float rightVolume) {
        if(isContainPlayer(host)){
            setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public void setDecodeMode(IPlayer host, DecodeMode decodeMode) {
        if(isContainPlayer(host)){
            setDecodeMode(decodeMode);
        }
    }

    @Override
    public DecodeMode getDecodeMode(IPlayer host) {
        if(isContainPlayer(host)){
            return getDecodeMode();
        }
        return null;
    }

    @Override
    public void setDisplay(IPlayer host, SurfaceHolder surfaceHolder) {
        if(isContainPlayer(host)){
            setDisplay(surfaceHolder);
        }
    }

    @Override
    public void setSurface(IPlayer host, Surface surface) {
        if(isContainPlayer(host)){
            setSurface(surface);
        }
    }

    @Override
    public Rate getCurrentDefinition(IPlayer host) {
        if(isContainPlayer(host)){
            return getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions(IPlayer host) {
        if(isContainPlayer(host)){
            return getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(IPlayer host, Rate rate) {
        if(isContainPlayer(host)){
            changeVideoDefinition(rate);
        }
    }

    //-------------------IPlayerManager-------------------

}
