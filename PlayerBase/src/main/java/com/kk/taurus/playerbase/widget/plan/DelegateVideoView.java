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

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/11/18.
 */

public abstract class DelegateVideoView extends BaseVideoView {

    public DelegateVideoView(Context context) {
        super(context);
    }

    @Override
    protected BaseRenderWidget initRenderWidget(Context context) {
        BaseRenderWidget renderWidget = (BaseRenderWidget) ConfigLoader.getPlayerInstance(context,getRenderType());
        if(renderWidget!=null){
            renderWidget.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    onBindPlayerEvent(eventCode, bundle);
                }
            });
            renderWidget.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode, Bundle bundle) {
                    onBindErrorEvent(errorCode, bundle);
                }
            });
        }
        return renderWidget;
    }

    private boolean isLegalState(){
        return mRenderWidget!=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
        if(isLegalState()){
            mRenderWidget.setDataSource(data);
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
    public void start() {
        if(isLegalState()){
            mRenderWidget.start();
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,null);
        }
    }

    @Override
    public void start(int msc) {
        if(isLegalState()){
            mRenderWidget.start(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START,bundle);
        }
    }

    @Override
    public void pause() {
        if(isLegalState()){
            mRenderWidget.pause();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE,bundle);
        }
    }

    @Override
    public void resume() {
        if(isLegalState()){
            mRenderWidget.resume();
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,getCurrentPosition());
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAY_RESUME,bundle);
        }
    }

    @Override
    public void seekTo(int msc) {
        if(isLegalState()){
            mRenderWidget.seekTo(msc);
            Bundle bundle = new Bundle();
            bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_POSITION,msc);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO,bundle);
        }
    }

    @Override
    public void stop() {
        if(isLegalState()){
            mRenderWidget.stop();
            reset();
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP,null);
        }
    }

    @Override
    public void reset() {
        if(isLegalState()){
            mRenderWidget.reset();
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
            return mRenderWidget.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(isLegalState()){
            return mRenderWidget.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(isLegalState()){
            return mRenderWidget.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(isLegalState()){
            return mRenderWidget.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(isLegalState()){
            return mRenderWidget.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(isLegalState()){
            return mRenderWidget.getStatus();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(isLegalState()){
            return mRenderWidget.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(isLegalState()){
            return mRenderWidget.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(isLegalState()){
            Bundle bundle = new Bundle();
            bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_RATE_DATA,rate);
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION,bundle);
            mRenderWidget.changeVideoDefinition(rate);
        }
    }

    @Override
    public void destroy() {
        if(isLegalState()){
            mRenderWidget.destroy();
            onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
            setOnPlayerEventListener(null);
            setOnErrorListener(null);
        }
    }
    
}
