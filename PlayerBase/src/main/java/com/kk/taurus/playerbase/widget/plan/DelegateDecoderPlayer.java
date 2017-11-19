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

package com.kk.taurus.playerbase.widget.plan;

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by mtime on 2017/11/17.
 */

public abstract class DelegateDecoderPlayer extends BaseMediaPlayer{

    public DelegateDecoderPlayer(Context context){
        super(context);
    }

    @Override
    protected BaseDecoder initDecoder(Context context) {
        BaseDecoder decoder = (BaseDecoder) ConfigLoader.getDecoderInstance(context, getDecoderType());
        if(decoder!=null){
            decoder.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    onBindPlayerEvent(eventCode, bundle);
                }
            });
            decoder.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode, Bundle bundle) {
                    onBindErrorEvent(errorCode,bundle);
                }
            });
        }
        return decoder;
    }

    private boolean isLegalState(){
        return mDecoder!=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
        if(isLegalState()){
            mDecoder.setDataSource(data);
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_VIDEO_DATA,data);
            //on set data source
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE,bundle);
            if(data instanceof BaseAdVideo){
                //on set ad video data
                onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_AD_DATA,bundle);
            }else{
                //on set video data
                onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_VIDEO_DATA,bundle);
            }
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(isLegalState()){
            mDecoder.setDisplay(surfaceHolder);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_SURFACE_HOLDER_UPDATE,null);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        if(isLegalState()){
            mDecoder.setSurface(surface);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_SURFACE_UPDATE,null);
        }
    }

    @Override
    public void start() {
        if(isLegalState()){
            mDecoder.start();
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,null);
        }
    }

    @Override
    public void start(int msc) {
        if(isLegalState()){
            mDecoder.start(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
        }
    }

    @Override
    public void pause() {
        if(isLegalState()){
            mDecoder.pause();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,bundle);
        }
    }

    @Override
    public void resume() {
        if(isLegalState()){
            mDecoder.resume();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(isLegalState()){
            mDecoder.seekTo(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
        }
    }

    @Override
    public void stop() {
        if(isLegalState()){
            mDecoder.stop();
            reset();
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
        }
    }

    @Override
    public void reset() {
        if(isLegalState()){
            mDecoder.reset();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(isLegalState()){
            stop();
            setDataSource(mDataSource);
            start(msc);
        }
    }

    @Override
    public boolean isPlaying() {
        if(isLegalState()){
            return mDecoder.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(isLegalState()){
            return mDecoder.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(isLegalState()){
            return mDecoder.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(isLegalState()){
            return mDecoder.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(isLegalState()){
            return mDecoder.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(isLegalState()){
            return mDecoder.getStatus();
        }
        return 0;
    }

    @Override
    public int getVideoWidth() {
        if(isLegalState()){
            return mDecoder.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if(isLegalState()){
            return mDecoder.getVideoHeight();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(isLegalState()){
            return mDecoder.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(isLegalState()){
            return mDecoder.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(isLegalState()){
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_RATE_DATA,rate);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION,bundle);
            mDecoder.changeVideoDefinition(rate);
        }
    }

    @Override
    public void destroy() {
        if(isLegalState()){
            mDecoder.destroy();
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
            setOnPlayerEventListener(null);
            setOnErrorListener(null);
        }
    }

}
