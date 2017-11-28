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

package com.kk.taurus.playerbase.setting;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.widget.plan.MixMediaPlayer;
import com.kk.taurus.playerbase.widget.plan.MixVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/11/19.
 * 该类为解码器(或VideoView)的单利管理类，由框架内部调用。请不要尝试直接调用该类中的方法。
 */

public class InternalPlayerManager implements IPlayer {

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
    private MixVideoView mVideoView;
    private int mWidgetMode;

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
        Context appContext = context.getApplicationContext();
        boolean isModeChange = mWidgetMode!=widgetMode;
        mWidgetMode = widgetMode;
        if(isModeChange || isNeedInitInternalPlayer())
            initInternalPlayer(appContext);
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
        Log.d(TAG,"createMediaPlayer ...");
        return new MixMediaPlayer(context);
    }

    private MixVideoView createVideoView(Context context){
        Log.d(TAG,"createVideoView ...");
        return new MixVideoView(context);
    }

    private void attachMediaPlayerListener(){
        if(mMediaPlayer!=null){
            mMediaPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
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
        return mWidgetMode==WIDGET_MODE_DECODER && mMediaPlayer!=null;
    }

    private boolean isVideoViewMode(){
        return mWidgetMode==WIDGET_MODE_VIDEO_VIEW && mVideoView!=null;
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
    public void setDataSource(VideoData data) {
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
    public void setViewType(ViewType viewType) {
        if(isVideoViewMode()){
            mVideoView.setViewType(viewType);
        }
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

}
