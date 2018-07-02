package com.kk.taurus.playerbase;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.player.OnBufferingListener;
import com.kk.taurus.playerbase.player.OnDoublePlayerListener;
import com.kk.taurus.playerbase.player.OnNextHandler;

public class DoublePlayer implements IPlayer {

    public final int REFER_PLAYER_UNSPECIFIED = 0;
    public final int REFER_PLAYER_A = 1;
    public final int REFER_PLAYER_B = 2;

    private int mUseRefer = REFER_PLAYER_A;
    private AVPlayer mPlayerA;
    private AVPlayer mPlayerB;

    private int mNextRefer = REFER_PLAYER_UNSPECIFIED;

    private DataSource mDataSource;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;
    private OnBufferingListener mOnBufferingListener;

    private OnDoublePlayerListener mOnDoublePlayerListener;
    private DataSource mNextDataSource;

    public DoublePlayer(int decoderPlanIdA, int decoderPlanIdB){
        //init playerA
        mPlayerA = new AVPlayer(decoderPlanIdA);

        //init playerB
        mPlayerB = new AVPlayer(decoderPlanIdB);
    }

    @Override
    public void option(int code, Bundle bundle) {
        mPlayerA.option(code, bundle);
        mPlayerB.option(code, bundle);
    }

    public void setOnDoublePlayerListener(OnDoublePlayerListener onDoublePlayerListener) {
        this.mOnDoublePlayerListener = onDoublePlayerListener;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
        attachListener();
        AVPlayer currentReferPlayer = getCurrentReferPlayer();
        currentReferPlayer.setDataSource(dataSource);
    }

    private void attachListener(){
        mPlayerA.setOnPlayerEventListener(mInternalPlayerAEventListener);
        mPlayerB.setOnPlayerEventListener(mInternalPlayerBEventListener);
    }

    private AVPlayer getCurrentReferPlayer(){
        return getReferPlayer(mUseRefer);
    }

    private AVPlayer getNextReferPlayer(){
        return getReferPlayer(mNextRefer);
    }

    private AVPlayer getReferPlayer(int useRefer){
        switch (useRefer){
            case REFER_PLAYER_B:
                return mPlayerB;
            case REFER_PLAYER_A:
            default:
                return mPlayerA;
        }
    }

    private int calcNextReferType(){
        switch (mUseRefer){
            case REFER_PLAYER_A:
                return REFER_PLAYER_B;
            case REFER_PLAYER_B:
            default:
                return REFER_PLAYER_A;
        }
    }

    private boolean isPlaybackState(int state){
        return state!= IPlayer.STATE_END
                && state!= IPlayer.STATE_ERROR
                && state!= IPlayer.STATE_IDLE
                && state!= IPlayer.STATE_INITIALIZED
                && state!= IPlayer.STATE_STOPPED;
    }

    private OnNextHandler mInternalNextHandler = new OnNextHandler() {
        @Override
        public void start(int msc) {
            mUseRefer = mNextRefer;
            //reset next refer.
            mNextRefer = REFER_PLAYER_UNSPECIFIED;
            AVPlayer currentReferPlayer = getCurrentReferPlayer();
            int state = currentReferPlayer.getState();
            //if in playback state, seek or start it.
            if(isPlaybackState(state)){
                //send a prepared event for re attach surface.
                if(mUseRefer==REFER_PLAYER_A){
                    mInternalPlayerAEventListener.onPlayerEvent(
                            OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);
                }else if(mUseRefer==REFER_PLAYER_B){
                    mInternalPlayerBEventListener.onPlayerEvent(
                            OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED, null);
                }
                if(msc > 0){
                    currentReferPlayer.seekTo(msc);
                }
                currentReferPlayer.start();
                //prepare next
                handlePrepareNext();
            }else{
                //maybe player prepared not ready or occur error.
                currentReferPlayer.stop();
                currentReferPlayer.reset();
                currentReferPlayer.setDataSource(mNextDataSource);
                currentReferPlayer.start(msc);
            }
        }
    };

    private OnPlayerEventListener mInternalPlayerAEventListener =
            new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            if(mUseRefer==REFER_PLAYER_A && mOnPlayerEventListener!=null){
                mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
            }
            switch (eventCode){
                case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                    handlePrepareNext();
                    break;
            }
        }
    };

    private OnPlayerEventListener mInternalPlayerBEventListener =
            new OnPlayerEventListener() {
        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            if(mUseRefer==REFER_PLAYER_B && mOnPlayerEventListener!=null){
                mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
            }
            switch (eventCode){
                case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                    handlePrepareNext();
                    break;
            }
        }
    };

    private void handlePrepareNext(){
        //on first player prepared, prepare to next DataSource.
        if(mOnDoublePlayerListener!=null && mNextRefer==REFER_PLAYER_UNSPECIFIED){
            mNextDataSource = mOnDoublePlayerListener.getNextDataSource();
            if(mNextDataSource!=null){
                mNextRefer = calcNextReferType();
                AVPlayer referPlayer = getReferPlayer(mNextRefer);
                referPlayer.setDataSource(mNextDataSource);
                mOnDoublePlayerListener.onNextHandlerPrepared(mInternalNextHandler);
            }
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        getCurrentReferPlayer().setDisplay(surfaceHolder);
    }

    @Override
    public void setSurface(Surface surface) {
        getCurrentReferPlayer().setSurface(surface);
    }

    @Override
    public void setVolume(float left, float right) {
        getCurrentReferPlayer().setVolume(left, right);
    }

    @Override
    public void setSpeed(float speed) {
        getCurrentReferPlayer().setSpeed(speed);
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

    @Override
    public int getBufferPercentage() {
        return getCurrentReferPlayer().getBufferPercentage();
    }

    @Override
    public boolean isPlaying() {
        return getCurrentReferPlayer().isPlaying();
    }

    @Override
    public int getCurrentPosition() {
        return getCurrentReferPlayer().getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return getCurrentReferPlayer().getDuration();
    }

    @Override
    public int getAudioSessionId() {
        return getCurrentReferPlayer().getAudioSessionId();
    }

    @Override
    public int getVideoWidth() {
        return getCurrentReferPlayer().getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return getCurrentReferPlayer().getVideoHeight();
    }

    @Override
    public int getState() {
        return getCurrentReferPlayer().getState();
    }

    @Override
    public void start() {
        getCurrentReferPlayer().start();
    }

    @Override
    public void start(int msc) {
        getCurrentReferPlayer().start(msc);
    }

    @Override
    public void pause() {
        getCurrentReferPlayer().pause();
    }

    @Override
    public void resume() {
        getCurrentReferPlayer().resume();
    }

    @Override
    public void seekTo(int msc) {
        getCurrentReferPlayer().seekTo(msc);
    }

    @Override
    public void stop() {
        getCurrentReferPlayer().stop();
    }

    @Override
    public void reset() {
        getCurrentReferPlayer().reset();
    }

    @Override
    public void destroy() {
        mPlayerA.destroy();
        mPlayerB.destroy();
    }
}
