package com.kk.taurus.ijkplayer;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.kk.taurus.ijkplayer.media.IjkVideoView;
import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.plan.BaseRenderWidget;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Taurus on 2016/11/14.
 */

public class IJKRenderWidgetPlayer extends BaseRenderWidget {

    private final String TAG = "IjkSinglePlayer";
    protected IjkVideoView mVideoView;
    private boolean hasLoadLibrary;
    private VideoData dataSource;

    public IJKRenderWidgetPlayer(Context context) {
        super(context);
    }

    @Override
    public View getPlayerView(Context context) {
        loadLibrary();
        mVideoView = new IjkVideoView(context);
        mVideoView.setFocusable(false);
        mVideoView.setBackgroundColor(Color.BLACK);
        initPlayerListener();
        return mVideoView;
    }

    private void loadLibrary() {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            hasLoadLibrary = true;
        } catch (Throwable e) {
            Log.e(TAG, "loadLibraries error", e);
        }
    }

    private void initPlayerListener() {
        if(mVideoView==null)
            return;
        mVideoView.setOnPreparedListener(mOnPreparedListener);
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

            if(startSeekPos > 0){
                seekTo(startSeekPos);
                startSeekPos = -1;
            }
            Log.d(TAG,"EVENT_CODE_PREPARED");
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PREPARED,null);
//                //IjkVideoView  ...int STATE_PLAYING = 3;
            if(available() && mTargetStatus==STATUS_STARTED){
                start();
            }
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
        return mVideoView!=null && hasLoadLibrary;
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
        }
        mTargetStatus = STATUS_PAUSED;
    }

    @Override
    public void resume() {
//        if(available() && mStatus == STATUS_PAUSED){
//            mVideoView.start();
//            mStatus = STATUS_STARTED;
//        }
//        mTargetStatus = STATUS_STARTED;
        start();
    }

    @Override
    public void seekTo(int msc) {
        if(available() &&
                (mStatus==STATUS_PREPARED
                        || mStatus==STATUS_STARTED
                        || mStatus==STATUS_PAUSED
                        || mStatus==STATUS_PLAYBACK_COMPLETE)){
            mVideoView.seekTo(msc);
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
        }
        mTargetStatus = STATUS_STOPPED;
    }

    @Override
    public void reset() {
        if(available()){
//            mVideoView.reset();
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
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(available()){
            if(aspectRatio == AspectRatio.AspectRatio_16_9){
                mVideoView.setAspectRatio(com.kk.taurus.playerbase.player.IRenderView.AR_16_9_FIT_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_4_3){
                mVideoView.setAspectRatio(com.kk.taurus.playerbase.player.IRenderView.AR_4_3_FIT_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_FILL_PARENT){
                mVideoView.setAspectRatio(com.kk.taurus.playerbase.player.IRenderView.AR_ASPECT_FILL_PARENT);
            }else if(aspectRatio == AspectRatio.AspectRatio_ORIGIN){
                mVideoView.setAspectRatio(com.kk.taurus.playerbase.player.IRenderView.AR_ASPECT_WRAP_CONTENT);
            }
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
