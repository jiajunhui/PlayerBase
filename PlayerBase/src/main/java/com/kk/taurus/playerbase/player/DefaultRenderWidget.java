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

package com.kk.taurus.playerbase.player;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.widget.plan.BaseRenderWidget;

import java.util.List;

/**
 * Created by Taurus on 2016/11/14.
 */

public class DefaultRenderWidget extends BaseRenderWidget {

    private final String TAG = "MediaSinglePlayer";
    protected MediaVideoView mVideoView;
    private VideoData dataSource;

    public DefaultRenderWidget(Context context) {
        super(context);
    }

    @Override
    public View getPlayerView(Context context) {
        mVideoView = new MediaVideoView(context);
        mVideoView.setFocusable(false);
        mVideoView.setBackgroundColor(Color.BLACK);
        initPlayerListener();
        return mVideoView;
    }

    private void initPlayerListener() {
        if(mVideoView==null)
            return;
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
    }

    private void resetListener(){
        if(mVideoView==null)
            return;
        mVideoView.setOnPreparedListener(null);
        mVideoView.setOnInfoListener(null);
        mVideoView.setOnCompletionListener(null);
        mVideoView.setOnErrorListener(null);
    }

    private void preparedMediaPlayer(IMediaPlayer mediaPlayer) {
        if (mediaPlayer == null)
            return;
        mediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
    }

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            mStatus = STATUS_PREPARED;
            preparedMediaPlayer(mp);
            Log.d(TAG,"EVENT_CODE_PREPARED");

            if(startSeekPos > 0){
                seekTo(startSeekPos);
            }

            if(mTargetStatus==STATUS_STARTED){
                start();
            }
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PREPARED,null);
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_VIDEO_WIDTH,width);
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_VIDEO_HEIGHT,height);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_VIDEO_SIZE_CHANGE,bundle);
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            switch (what) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.d(TAG,"EVENT_CODE_BUFFERING_START");
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_START,null);
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.d(TAG,"EVENT_CODE_BUFFERING_END");
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_END,null);
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.d(TAG,"EVENT_CODE_RENDER_START");
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_RENDER_START,null);
                    break;
                case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Bundle bundle = new Bundle();
                    bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_INT_DATA,extra);
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_VIDEO_ROTATION_CHANGED,bundle);
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {
            Log.d(TAG,"EVENT_CODE_PLAY_COMPLETE");
            mStatus = STATUS_PLAYBACK_COMPLETE;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE,null);
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            mStatus = STATUS_ERROR;
            onErrorEvent(OnErrorListener.ERROR_CODE_COMMON,null);
            return false;
        }
    };

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            Log.d(TAG,"EVENT_CODE_SEEK_COMPLETE");
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE,null);
        }
    };

    private void toggleAspectRatio() {
        if(available()){
            mVideoView.toggleAspectRatio();
        }
    }

    private boolean available(){
        return mVideoView!=null;
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
    public void changeVideoDefinition(Rate videoRate) {

    }

    @Override
    public void setDataSource(VideoData data) {
        if(available() && data!=null && data.getData()!=null && mStatus==STATUS_IDLE){
            mStatus = STATUS_INITIALIZED;
            this.dataSource = data;
            startSeekPos = -1;
            //-----send event-----
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_VIDEO_DATA,data);
            //on set data source
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE,bundle);
            //-----send event-----
            mVideoView.setVideoPath(data.getData());
            initPlayerListener();
        }
        mTargetStatus = STATUS_INITIALIZED;
    }

    @Override
    public void rePlay(int msc) {
        if(available()){
            stop();
            setDataSource(dataSource);
            start(msc);
        }
    }

    @Override
    public void start() {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            mVideoView.start();
            mStatus = STATUS_STARTED;
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,startSeekPos);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
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
    public void pause() {
        if(available() && mStatus==STATUS_STARTED){
            mVideoView.pause();
            mStatus = STATUS_PAUSED;
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,bundle);
        }
        mTargetStatus = STATUS_PAUSED;
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSED){
            mVideoView.start();
            mStatus = STATUS_STARTED;
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
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
            mVideoView.seekTo(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
        }
    }

    @Override
    public void stop() {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_STARTED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            mVideoView.stop();
            mStatus = STATUS_STOPPED;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
        }
        mTargetStatus = STATUS_STOPPED;
    }

    @Override
    public void reset() {
        if(available()){
            mVideoView.reset();
            resetListener();
            mStatus = STATUS_IDLE;
        }
        mTargetStatus = STATUS_IDLE;
    }

    @Override
    public boolean isPlaying() {
        if(available()){
            return mVideoView.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()){
            return mVideoView.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()){
            return mVideoView.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(available()){
            return mVideoView.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(mVideoView!=null)
            return mVideoView.getAudioSessionId();
        return super.getAudioSessionId();
    }

    @Override
    public void setViewType(ViewType viewType) {
        super.setViewType(viewType);
        if(available()){
            mVideoView.setRenderType(viewType==ViewType.SURFACEVIEW);
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_SERIALIZABLE_DATA,viewType);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_RENDER_VIEW_TYPE_UPDATE,bundle);
        }
    }

    @Override
    public View getRenderView() {
        if(available()){
            return mVideoView.getRenderView();
        }
        return super.getRenderView();
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(available()){
            if(aspectRatio == AspectRatio.AspectRatio_16_9){
                mVideoView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_4_3){
                mVideoView.setAspectRatio(IRenderView.AR_4_3_FIT_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_FILL_PARENT){
                mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_ORIGIN){
                mVideoView.setAspectRatio(IRenderView.AR_ASPECT_WRAP_CONTENT);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_SERIALIZABLE_DATA,aspectRatio);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_RENDER_ASPECT_RATIO_UPDATE,bundle);
        }
    }

    @Override
    public void destroy() {
        try{
            mStatus = STATUS_END;
            mTargetStatus = STATUS_IDLE;
            if(mVideoView!=null){
                mVideoView.stopPlayback();
                mVideoView = null;
            }
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
