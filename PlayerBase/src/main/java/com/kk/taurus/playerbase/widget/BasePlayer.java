package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public abstract class BasePlayer extends BaseAdPlayer {

    private final String TAG = "BasePlayer";
    protected BaseSinglePlayer mInternalPlayer;
    protected VideoData dataSource;

    public BasePlayer(@NonNull Context context) {
        super(context);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean available(){
        return mInternalPlayer !=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        if(available() && data!=null && data.getData()!=null){
            this.dataSource = data;
            mInternalPlayer.setDataSource(data);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",data);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE,bundle);
        }
    }

    @Override
    public void start() {
        if(available()){
            startPos = 0;
            mInternalPlayer.start();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,null);
        }
    }

    @Override
    public void start(int msc) {
        if(available()){
            startPos = msc;
            mInternalPlayer.start(msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,null);
        }
    }

    @Override
    public void pause() {
        if(available()){
            mInternalPlayer.pause();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,null);
        }
    }

    @Override
    public void resume() {
        if(available() && mStatus == STATUS_PAUSE){
            mInternalPlayer.resume();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,null);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(available()){
            mInternalPlayer.seekTo(msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,null);
        }
    }

    @Override
    public void stop() {
        if(available()){
            mInternalPlayer.stop();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
        }
    }

    @Override
    public void rePlay(int msc) {
        if(available()){
            if(dataSource!=null && available()){
                stop();
                setDataSource(dataSource);
                start(msc);
            }
        }
    }

    @Override
    public boolean isPlaying() {
        if(available()){
            return mInternalPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(available()){
            return mInternalPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(available()){
            return mInternalPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(available()){
            return mInternalPlayer.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(available()){
            return mInternalPlayer.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(available()){
            return mInternalPlayer.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate videoRate) {
        if(available()){
            mInternalPlayer.changeVideoDefinition(videoRate);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION,null);
        }
    }

    @Override
    public void setDecodeMode(DecodeMode mDecodeMode) {
        super.setDecodeMode(mDecodeMode);
        if(available()){
            mInternalPlayer.setDecodeMode(mDecodeMode);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(available()){
            mInternalPlayer.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyPlayer();
    }

    protected void destroyPlayer() {
        if(available()){
            mInternalPlayer.setOnErrorListener(null);
            mInternalPlayer.setOnPlayerEventListener(null);
            mInternalPlayer.destroy();
        }
    }
}
