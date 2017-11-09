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

package com.kk.taurus.plplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.view.MSurfaceView;
import com.kk.taurus.playerbase.widget.BaseSinglePlayer;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.io.IOException;
import java.util.List;

/**
 * Created by Taurus on 2017/10/10.
 */

public class PLSinglePlayer extends BaseSinglePlayer implements PLMediaPlayer.OnPreparedListener, PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnVideoSizeChangedListener, PLMediaPlayer.OnErrorListener, MSurfaceView.OnSurfaceListener, PLMediaPlayer.OnBufferingUpdateListener, PLMediaPlayer.OnSeekCompleteListener {

    private final String TAG = "PLSinglePlayer";
    private PLMediaPlayer mMediaPlayer;
    private VideoData mDataSource;
    private FrameLayout mRenderContainer;
    private int mCurrentBufferPercentage;

    public PLSinglePlayer(Context context) {
        super(context);
    }

    @Override
    public void setDataSource(VideoData data) {
        if(data!=null){
            Log.d(TAG,"url = " + data.getData());
            this.mDataSource = data;
            if(mMediaPlayer==null){
                mMediaPlayer = createMediaPlayer();
            }else{
//                reset();
                resetListener();
            }
            openVideo(data);
        }
    }

    private void resetListener(){
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnSeekCompleteListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        mMediaPlayer.setOnErrorListener(null);
    }

    private void attachListener(){
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    private void openVideo(VideoData data) {
        attachListener();
        try {
            mStatus = STATUS_INITIALIZED;
            mMediaPlayer.setDataSource(getContext(),Uri.parse(data.getData()));
            mMediaPlayer.setScreenOnWhilePlaying(true);
            if(useDefaultRender){
                attachSurfaceView();
            }
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            onErrorEvent(OnErrorListener.ERROR_CODE_COMMON,null);
        }
    }

    private PLMediaPlayer createMediaPlayer() {
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
        // 播放前最大探测流的字节数，单位是 byte 默认值是：128 * 1024
        options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);
        // 默认的缓存大小，单位是 ms 默认值是：2000
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 200);
        // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化 默认值是：0
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        // whether start play automatically after prepared, default value is 1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        return new PLMediaPlayer(getContext(),options);
    }

    private boolean available(){
        return mMediaPlayer!=null;
    }

    @Override
    public void start() {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            mMediaPlayer.start();
            mStatus = STATUS_STARTED;
        }
        mTargetStatus = STATUS_STARTED;
    }

    @Override
    public void start(int msc){
        if(available()){
            if(msc > 0){
                startSeekPos = msc;
            }
            start();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(available()){
            stop();
            setDataSource(mDataSource);
            start(msc);
        }
    }

    @Override
    public void pause() {
        if(available() && mStatus==STATUS_STARTED){
            mMediaPlayer.pause();
            mStatus = STATUS_PAUSED;
        }
        mTargetStatus = STATUS_PAUSED;
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSED){
            mMediaPlayer.start();
            mStatus = STATUS_STARTED;
        }
        mTargetStatus = STATUS_STARTED;
    }

    @Override
    public void seekTo(int msc) {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_STARTED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            mMediaPlayer.seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_STARTED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            mMediaPlayer.stop();
            mStatus = STATUS_STOPPED;
        }
        mTargetStatus = STATUS_STOPPED;
    }

    @Override
    public void reset() {
        if(available()){
            mMediaPlayer.reset();
            resetListener();
            mStatus = STATUS_IDLE;
        }
        mTargetStatus = STATUS_IDLE;
    }

    @Override
    public boolean isPlaying() {
        if(available()){
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()){
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()){
            return (int) mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return mCurrentBufferPercentage;
    }

    @Override
    public Rate getCurrentDefinition() {
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {

    }

    @Override
    public void destroy() {
        super.destroy();
        if(available()){
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public View getPlayerView(Context context) {
        mRenderContainer = new FrameLayout(context);
        mMediaPlayer = createMediaPlayer();
        return mRenderContainer;
    }

    private void attachSurfaceView(){
        MSurfaceView surfaceView = new MSurfaceView(getContext(),this);
        mRenderContainer.removeAllViews();
        mRenderContainer.addView(surfaceView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        super.setDisplay(surfaceHolder);
        if(surfaceHolder!=null && mMediaPlayer!=null){
            mRenderContainer.removeAllViews();
            mMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        mStatus = STATUS_PREPARED;

        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PREPARED,null);

        if(startSeekPos > 0){
            seekTo(startSeekPos);
            startSeekPos = -1;
        }

        if(mTargetStatus==STATUS_STARTED){
            start();
        }
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
        switch (what){
            case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_RENDER_START,null);
                break;

            case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_START,null);
                break;

            case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_END,null);
                break;
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int percent) {
        mCurrentBufferPercentage = percent;
    }

    @Override
    public void onSeekComplete(PLMediaPlayer plMediaPlayer) {
        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE,null);
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE,null);
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(OnErrorListener.KEY_EXTRA,i);
        onErrorEvent(OnErrorListener.ERROR_CODE_COMMON,bundle);
        return false;
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder) {
        if(mMediaPlayer!=null){
            mMediaPlayer.setDisplay(holder);
        }
    }

    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void onSurfaceDestroyed(SurfaceHolder holder) {
        if(mMediaPlayer!=null){
            mMediaPlayer.setDisplay(null);
        }
    }

    @Override
    public void onSurfaceViewAttachedToWindow() {

    }

    @Override
    public void onSurfaceViewDetachedFromWindow() {

    }

}
