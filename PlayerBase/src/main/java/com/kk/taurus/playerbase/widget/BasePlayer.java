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

package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
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

    public BasePlayer(@NonNull Context context) {
        super(context);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCoversHasInit(Context context) {
        super.onCoversHasInit(context);
        onBindPlayer(this,this);
    }

    private boolean available(){
        return mInternalPlayer !=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
        if(available() && data!=null && data.getData()!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_VIDEO_DATA,data);
            //on set data source
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE,bundle);
            if(data instanceof BaseAdVideo){
                //on set ad video data
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_AD_DATA,bundle);
            }else{
                //on set video data
                onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_VIDEO_DATA,bundle);
            }
            mInternalPlayer.setDataSource(data);
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
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
        }
    }

    @Override
    public void pause() {
        if(available()){
            mInternalPlayer.pause();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,bundle);
        }
    }

    @Override
    public void resume() {
        if(available()){
            mInternalPlayer.resume();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(available()){
            mInternalPlayer.seekTo(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
        }
    }

    @Override
    public void stop() {
        if(available()){
            mInternalPlayer.stop();
            reset();
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
        }
    }

    @Override
    public void reset() {
        if(available()){
            mInternalPlayer.reset();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(available() && isDataSourceAvailable()){
            stop();
            setDataSource(dataSource);
            start(msc);
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
    public void changeVideoDefinition(Rate rate) {
        super.changeVideoDefinition(rate);
        if(available() && rate!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_RATE_DATA,rate);
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION,bundle);
            mInternalPlayer.changeVideoDefinition(rate);
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
        super.setAspectRatio(aspectRatio);
        if(available()){
            mInternalPlayer.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyInternalPlayer();
    }

    protected void destroyInternalPlayer() {
        if(available()){
            mInternalPlayer.setOnErrorListener(null);
            mInternalPlayer.setOnPlayerEventListener(null);
            mInternalPlayer.destroy();
        }
    }
}
