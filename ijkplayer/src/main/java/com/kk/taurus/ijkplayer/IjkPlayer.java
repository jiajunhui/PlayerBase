package com.kk.taurus.ijkplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.config.AppContextAttach;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.BaseInternalPlayer;

import java.util.HashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Taurus on 2018/4/18.
 */

public class IjkPlayer extends BaseInternalPlayer {
    private final String TAG = "IjkPlayer";

    public static final int PLAN_ID = 100;

    private IjkMediaPlayer mMediaPlayer;

    private int mTargetState;

    private int startSeekPos;

    public static void init(Context context){
        PlayerConfig.addDecoderPlan(new DecoderPlan(
                PLAN_ID,
                IjkPlayer.class.getName(),
                "ijkplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID);
        PlayerLibrary.init(context);
    }

    public IjkPlayer() {
        // init player
        mMediaPlayer = createPlayer();
    }

    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    protected IjkMediaPlayer createPlayer(){
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
//        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        //设置清除dns cache
        //IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1

        //open mediacodec
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-hevc", 1);

        //accurate seek
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000000);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        return ijkMediaPlayer;
    }

    @Override
    public void setDataSource(DataSource data) {
        if(data!=null){
            openVideo(data);
        }
    }

    private void openVideo(DataSource dataSource) {
        try {
            if(mMediaPlayer==null){
                mMediaPlayer = new IjkMediaPlayer();
            }else{
                stop();
                reset();
                resetListener();
            }
            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            updateStatus(STATE_INITIALIZED);

            if(dataSource.getTimedTextSource()!=null){
                PLog.e(TAG,"ijkplayer not support timed text !");
            }

            Context applicationContext = AppContextAttach.getApplicationContext();
            String data = dataSource.getData();
            Uri uri = dataSource.getUri();
            String assetsPath = dataSource.getAssetsPath();
            HashMap<String, String> headers = dataSource.getExtra();
            int rawId = dataSource.getRawId();
            if(data!=null){
                if(headers==null)
                    mMediaPlayer.setDataSource(data);
                else
                    mMediaPlayer.setDataSource(data, headers);
            }else if(uri!=null){
                if(uri.getScheme().equals(ContentResolver.SCHEME_ANDROID_RESOURCE)){
                    mMediaPlayer.setDataSource(RawDataSourceProvider.create(applicationContext, uri));
                }else{
                    if(headers==null)
                        mMediaPlayer.setDataSource(applicationContext, uri);
                    else
                        mMediaPlayer.setDataSource(applicationContext, uri, headers);
                }
            }else if(!TextUtils.isEmpty(assetsPath)){
                Log.e(TAG,"ijkplayer not support assets play, you can use raw play.");
            }else if(rawId > 0
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                Uri rawUri = DataSource.buildRawPath(applicationContext.getPackageName(), rawId);
                mMediaPlayer.setDataSource(RawDataSourceProvider.create(applicationContext, rawUri));
            }

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            Bundle bundle = BundlePool.obtain();
            bundle.putSerializable(EventKey.SERIALIZABLE_DATA,dataSource);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET,bundle);
        }catch (Exception e){
            e.printStackTrace();
            updateStatus(STATE_ERROR);
            mTargetState = STATE_ERROR;
            submitErrorEvent(OnErrorEventListener.ERROR_EVENT_COMMON, null);
        }
    }

    private boolean available(){
        return mMediaPlayer!=null;
    }

    @Override
    public void start() {
        if(available() &&
                (getState()==STATE_PREPARED
                        || getState()==STATE_PAUSED
                        || getState()==STATE_PLAYBACK_COMPLETE)){
            mMediaPlayer.start();
            updateStatus(STATE_STARTED);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_START, null);
        }
        mTargetState = STATE_STARTED;
        PLog.d(TAG,"start...");
    }

    @Override
    public void start(int msc){
        if(msc > 0){
            startSeekPos = msc;
        }
        if(available()){
            start();
        }
    }

    @Override
    public void pause() {
        try{
            int state = getState();
            if(available()
                    && state!=STATE_END
                    && state!=STATE_ERROR
                    && state!=STATE_IDLE
                    && state!=STATE_INITIALIZED
                    && state!=STATE_PAUSED
                    && state!=STATE_STOPPED){
                mMediaPlayer.pause();
                updateStatus(STATE_PAUSED);
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mTargetState = STATE_PAUSED;
    }

    @Override
    public void resume() {
        try {
            if(available() && getState() == STATE_PAUSED){
                mMediaPlayer.start();
                updateStatus(STATE_STARTED);
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESUME, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mTargetState = STATE_STARTED;
    }

    @Override
    public void seekTo(int msc) {
        if(available() &&
                (getState()== STATE_PREPARED
                        || getState()== STATE_STARTED
                        || getState()== STATE_PAUSED
                        || getState()== STATE_PLAYBACK_COMPLETE)){
            mMediaPlayer.seekTo(msc);
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_DATA, msc);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO, bundle);
        }
    }

    @Override
    public void stop() {
        if(available() &&
                (getState()== STATE_PREPARED
                        || getState()== STATE_STARTED
                        || getState()== STATE_PAUSED
                        || getState()== STATE_PLAYBACK_COMPLETE)){
            mMediaPlayer.stop();
            updateStatus(STATE_STOPPED);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STOP, null);
        }
        mTargetState = STATE_STOPPED;
    }

    @Override
    public void reset() {
        if(available()){
            mMediaPlayer.reset();
            updateStatus(STATE_IDLE);
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_RESET, null);
        }
        mTargetState = STATE_IDLE;
    }

    @Override
    public boolean isPlaying() {
        if(available() && getState()!= STATE_ERROR){
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()&& (getState()== STATE_PREPARED
                || getState()== STATE_STARTED
                || getState()== STATE_PAUSED
                || getState()== STATE_PLAYBACK_COMPLETE)){
            return (int) mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()
                && getState()!= STATE_ERROR
                && getState()!= STATE_INITIALIZED
                && getState()!= STATE_IDLE){
            return (int) mMediaPlayer.getDuration();
        }
        return 0;
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
    public void destroy() {
        if(available()){
            updateStatus(STATE_END);
            resetListener();
            mMediaPlayer.release();
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY, null);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        try {
            if(available()){
                mMediaPlayer.setDisplay(surfaceHolder);
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setSurface(Surface surface) {
        try {
            if(available()){
                mMediaPlayer.setSurface(surface);
                submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_UPDATE, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if(available()){
            mMediaPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public void setSpeed(float speed) {
        if(available()){
            mMediaPlayer.setSpeed(speed);
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

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            PLog.d(TAG,"onPrepared...");
            updateStatus(STATE_PREPARED);

            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_ARG1, mVideoWidth);
            bundle.putInt(EventKey.INT_ARG2, mVideoHeight);

            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED,bundle);

            int seekToPosition = startSeekPos;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                mMediaPlayer.seekTo(seekToPosition);
                startSeekPos = 0;
            }

            // We don't know the video size yet, but should start anyway.
            // The video size might be reported to us later.
            PLog.d(TAG,"mTargetState = " + mTargetState);
            if (mTargetState == STATE_STARTED) {
                start();
            }else if(mTargetState == STATE_PAUSED){
                pause();
            }else if(mTargetState == STATE_STOPPED
                    || mTargetState == STATE_IDLE){
                reset();
            }
        }
    };

    private int mVideoWidth;
    private int mVideoHeight;
    IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
            new IMediaPlayer.OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum, int sarDen) {
                    mVideoWidth = mp.getVideoWidth();
                    mVideoHeight = mp.getVideoHeight();
                    Bundle bundle = BundlePool.obtain();
                    bundle.putInt(EventKey.INT_ARG1, mVideoWidth);
                    bundle.putInt(EventKey.INT_ARG2, mVideoHeight);
                    bundle.putInt(EventKey.INT_ARG3, sarNum);
                    bundle.putInt(EventKey.INT_ARG4, sarDen);
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE,bundle);
                }
            };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    updateStatus(STATE_PLAYBACK_COMPLETE);
                    mTargetState = STATE_PLAYBACK_COMPLETE;
                    submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE,null);
                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            PLog.d(TAG, "MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            PLog.d(TAG, "MEDIA_INFO_VIDEO_RENDERING_START");
                            startSeekPos = 0;
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                            PLog.d(TAG, "MEDIA_INFO_BUFFERING_START:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                            PLog.d(TAG, "MEDIA_INFO_BUFFERING_END:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                            //not support
                            break;
                        case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                            PLog.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_BAD_INTERLEAVING,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                            PLog.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_NOT_SEEK_ABLE,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                            PLog.d(TAG, "MEDIA_INFO_METADATA_UPDATE:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_METADATA_UPDATE,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR:
                            PLog.d(TAG, "MEDIA_INFO_TIMED_TEXT_ERROR:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_TIMED_TEXT_ERROR,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                            PLog.d(TAG, "MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                            PLog.d(TAG, "MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                            PLog.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
                            Bundle bundle = BundlePool.obtain();
                            bundle.putInt(EventKey.INT_DATA,arg2);
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED,bundle);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                            PLog.d(TAG, "MEDIA_INFO_AUDIO_RENDERING_START:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_RENDER_START,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_DECODED_START:
                            PLog.d(TAG, "MEDIA_INFO_AUDIO_DECODED_START:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_DECODER_START,null);
                            break;
                        case IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START:
                            PLog.d(TAG, "MEDIA_INFO_AUDIO_SEEK_RENDERING_START:");
                            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_SEEK_RENDERING_START,null);
                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            PLog.d(TAG,"EVENT_CODE_SEEK_COMPLETE");
            submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE,null);
        }
    };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    PLog.d(TAG, "Error: " + framework_err + "," + impl_err);
                    updateStatus(STATE_ERROR);
                    mTargetState = STATE_ERROR;

                    switch (framework_err){
                        case 100:
//                            release(true);
                            break;
                    }

                    /* If an error handler has been supplied, use it and finish. */
                    Bundle bundle = BundlePool.obtain();
                    submitErrorEvent(OnErrorEventListener.ERROR_EVENT_COMMON,bundle);
                    return true;
                }
            };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    submitBufferingUpdate(percent, null);
                }
            };

}
