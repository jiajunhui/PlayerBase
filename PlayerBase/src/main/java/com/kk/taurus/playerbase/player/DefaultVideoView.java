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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.plan.BaseVideoView;

import java.util.List;

/**
 * Created by Taurus on 2017/12/3.
 */

public class DefaultVideoView extends BaseVideoView {

    private final String TAG = "DefaultVideoView";

    private VideoView mVideoView;

    public DefaultVideoView(Context context) {
        super(context);
    }

    @Override
    public View getPlayerView(Context context) {
        mVideoView = new VideoView(context);
        setListener();
        return mVideoView;
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
        if(data!=null){
            this.mDataSource = data;
            //-----send event-----
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_VIDEO_DATA,data);
            //on set data source
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE,bundle);
            //-----send event-----
            openVideo(data);
        }
    }

    private void openVideo(VideoData data) {
        try {
            Uri mUri = Uri.parse(data.getData());
            mStatus = STATUS_INITIALIZED;
            mVideoView.setVideoURI(mUri);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setListener() {
        mVideoView.setOnPreparedListener(mPreparedListener);
        mVideoView.setOnCompletionListener(mCompletionListener);
        mVideoView.setOnErrorListener(mErrorListener);
        if(Build.VERSION.SDK_INT>=17)
            mVideoView.setOnInfoListener(mInfoListener);
    }

    private void resetListener(){
        mVideoView.setOnPreparedListener(null);
        mVideoView.setOnCompletionListener(null);
        mVideoView.setOnErrorListener(null);
        if(Build.VERSION.SDK_INT>=17)
            mVideoView.setOnInfoListener(null);
    }

    private boolean available(){
        return mVideoView!=null;
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
    public void start() {
        if(available()){
            mVideoView.start();
            mStatus = STATUS_STARTED;
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,startSeekPos);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
        }
        mTargetStatus = STATUS_STARTED;
        Log.d(TAG,"start...");
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
        Log.d(TAG,"pause...");
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSED){
            mVideoView.resume();
            mStatus = STATUS_STARTED;
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
        }
        mTargetStatus = STATUS_STARTED;
        Log.d(TAG,"resume...");
    }

    @Override
    public void seekTo(int msc) {
        if(available()){
            mVideoView.seekTo(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
        }
    }

    @Override
    public void setDecodeMode(DecodeMode mDecodeMode) {
        super.setDecodeMode(mDecodeMode);

    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        super.setAspectRatio(aspectRatio);

    }

    @Override
    public void stop() {
        mTargetStatus = STATUS_STOPPED;
    }

    @Override
    public void reset() {
        mTargetStatus = STATUS_IDLE;
    }

    @Override
    public boolean isPlaying() {
        if(available() && mStatus!=STATUS_ERROR){
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
        if (available()){
            return mVideoView.getBufferPercentage();
        }
        return 0;
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
        if(available()){
            resetListener();
            mVideoView.stopPlayback();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
        }
    }

    @Override
    public int getAudioSessionId() {
        if(available() && Build.VERSION.SDK_INT >= 18){
            return mVideoView.getAudioSessionId();
        }
        return 0;
    }

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG,"onPrepared...");
            mStatus = STATUS_PREPARED;

            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PREPARED,null);

            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }

            // We don't know the video size yet, but should start anyway.
            // The video size might be reported to us later.
            Log.d(TAG,"mTargetStatus = " + mTargetStatus);
            if (mTargetStatus == STATUS_STARTED) {
                start();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mStatus = STATUS_PLAYBACK_COMPLETE;
                    mTargetStatus = STATUS_PLAYBACK_COMPLETE;
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE,null);
                }
            };

    private int mSeekWhenPrepared;
    private MediaPlayer.OnInfoListener mInfoListener =
            new MediaPlayer.OnInfoListener() {
                public boolean onInfo(MediaPlayer mp, int arg1, int arg2) {
                    switch (arg1) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            Log.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                            Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START:" + " renderView == SurfaceView : " + (mCurrentRender==RENDER_SURFACE_VIEW));
                            Log.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                            mSeekWhenPrepared = 0;
                            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_RENDER_START,null);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_START,null);
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            Log.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_BUFFERING_END,null);
                            break;
                        case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            break;
                        case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            break;
                        case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            break;
                        case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            Log.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                            break;
                        case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            Log.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                            break;
                    }
                    return true;
                }
            };

    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.d(TAG,"EVENT_CODE_SEEK_COMPLETE");
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE,null);
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener =
            new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
                    Log.d(TAG, "Error: " + framework_err + "," + impl_err);
                    mStatus = STATUS_ERROR;
                    mTargetStatus = STATUS_ERROR;

                    switch (framework_err){
                        case 100:
//                            release(true);
                            break;
                    }

                    /* If an error handler has been supplied, use it and finish. */
                    Bundle bundle = new Bundle();
                    bundle.putInt(OnErrorListener.KEY_EXTRA,framework_err);
                    onErrorEvent(OnErrorListener.ERROR_CODE_COMMON,bundle);
                    return true;
                }
            };

}
