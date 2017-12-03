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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.plan.BaseDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Taurus on 2017/12/3.
 */

public class DefaultDecoderPlayer extends BaseDecoder {

    private final String TAG = "DefaultMediaPlayer";
    private MediaPlayer mMediaPlayer;
    private VideoData mDataSource;
    private int mCurrentBufferPercentage;

    public DefaultDecoderPlayer(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mMediaPlayer = new MediaPlayer();
    }

    @Override
    public void setDataSource(VideoData data) {
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
            if(mMediaPlayer==null){
                mMediaPlayer = new MediaPlayer();
            }else{
                mMediaPlayer.reset();
                mStatus = STATUS_IDLE;
                resetListener();
            }
            Uri mUri = Uri.parse(data.getData());
            Map<String, String> mHeaders = new HashMap<>();
            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            mStatus = STATUS_INITIALIZED;
            mMediaPlayer.setDataSource(mUri.toString());

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
        }catch (Exception e){
            e.printStackTrace();
            mStatus = STATUS_ERROR;
            mTargetStatus = STATUS_ERROR;
        }
    }

    private boolean available(){
        return mMediaPlayer!=null;
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
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            try {
                mMediaPlayer.start();
                mStatus = STATUS_STARTED;
                Bundle bundle = new Bundle();
                bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,startSeekPos);
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
            }catch (Exception e){
                e.printStackTrace();
            }
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
            try {
                mMediaPlayer.pause();
                mStatus = STATUS_PAUSED;
                Bundle bundle = new Bundle();
                bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,bundle);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mTargetStatus = STATUS_PAUSED;
        Log.d(TAG,"pause...");
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSED){
            try {
                mMediaPlayer.start();
                mStatus = STATUS_STARTED;
                Bundle bundle = new Bundle();
                bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mTargetStatus = STATUS_STARTED;
        Log.d(TAG,"resume...");
    }

    @Override
    public void seekTo(int msc) {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_STARTED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            try {
                mMediaPlayer.seekTo(msc);
                Bundle bundle = new Bundle();
                bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_STARTED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            try {
                mMediaPlayer.stop();
                mStatus = STATUS_STOPPED;
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
            }catch (Exception e){
                e.printStackTrace();
            }
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
        Log.d(TAG,"reset...");
    }

    @Override
    public boolean isPlaying() {
        if(available() && mStatus!=STATUS_ERROR){
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()
                && (mStatus==STATUS_PREPARED
                || mStatus==STATUS_STARTED
                || mStatus==STATUS_PAUSED
                || mStatus==STATUS_PLAYBACK_COMPLETE)){
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()
                && mStatus!=STATUS_ERROR
                && mStatus!=STATUS_INITIALIZED
                && mStatus!=STATUS_IDLE){
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return mCurrentBufferPercentage;
    }

    @Override
    public int getVideoWidth() {
        if(available()){
            return mMediaPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if(available()){
            return mMediaPlayer.getVideoHeight();
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
            mMediaPlayer.release();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(surfaceHolder!=null && mMediaPlayer!=null){
            mMediaPlayer.setDisplay(surfaceHolder);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_SURFACE_HOLDER_UPDATE,null);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        if(surface!=null && mMediaPlayer!=null){
            mMediaPlayer.setSurface(surface);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_SURFACE_UPDATE,null);
        }
    }

    @Override
    public int getAudioSessionId() {
        if(available()){
            return mMediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    private void resetListener(){
        if(mMediaPlayer==null)
            return;
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
    }

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            Log.d(TAG,"onPrepared...");
            mStatus = STATUS_PREPARED;

            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PREPARED,null);

            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            int seekToPosition = startSeekPos;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
                startSeekPos = 0;
            }

            // We don't know the video size yet, but should start anyway.
            // The video size might be reported to us later.
            Log.d(TAG,"mTargetStatus = " + mTargetStatus);
            if (mTargetStatus == STATUS_STARTED) {
                start();
            }
        }
    };

    private int mVideoWidth;
    private int mVideoHeight;
    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new MediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    Bundle bundle = new Bundle();
                    bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_VIDEO_WIDTH, mVideoWidth);
                    bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_VIDEO_HEIGHT, mVideoHeight);
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_VIDEO_SIZE_CHANGE,bundle);
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
                            startSeekPos = 0;
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

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new MediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };


}
