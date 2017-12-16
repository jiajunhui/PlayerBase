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
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.PlayerInstanceStateAttach;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.plan.InternalMultiInstancePlayer;

import java.util.List;

/**
 * Created by Taurus on 2017/12/13.
 * 此类为支持多实例解码器的播放容器
 * 单实例解码容器，请参见{@link BaseSingleDecoderPlayer}
 */

public abstract class BaseMultiInstancePlayer extends BasePlayer implements InternalMultiInstancePlayer.OnInternalInstanceListener
        ,PlayerInstanceStateAttach.OnInstanceStateListener{

    private InternalMultiInstancePlayer mInternalPlayer;
    private boolean hasCreateMultiCoreInstance;

    public BaseMultiInstancePlayer(Context context) {
        super(context);
    }

    public BaseMultiInstancePlayer(Context context, int widgetMode, int type){
        super(context, widgetMode, type);
    }

    public BaseMultiInstancePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        PlayerInstanceStateAttach.get().attachInstance(this,this);
    }

    @Override
    protected void onWidgetModeChange(int widgetMode) {
        notifyPlayerWidget(mAppContext);
    }

    @Override
    protected void onPlayerTypeChange(int type) {
        mInternalPlayer.updatePlayerType(type);
    }

    @Override
    protected View getPlayerWidget(Context context) {
        if(mInternalPlayer==null){
            mInternalPlayer = new InternalMultiInstancePlayer(context, getWidgetMode(), getSignPlayerType());
        }else{
            mInternalPlayer.updateWidget(getWidgetMode(),getSignPlayerType());
        }
        mInternalPlayer.setOnInternalInstanceListener(this);
        return mInternalPlayer.getPlayerWidget();
    }

    @Override
    public void onInstanceStateChange(int code, Bundle bundle) {
        switch (code){
            case PlayerInstanceStateAttach.STATE_CODE_ON_NEW_INSTANCE_CREATE:
                hasCreateMultiCoreInstance = true;
                break;
        }
    }

    public boolean isHasCreateMultiCoreInstance() {
        return hasCreateMultiCoreInstance;
    }

    @Override
    public void onInternalPlayerEvent(int eventCode, Bundle bundle) {
        sendEvent(eventCode, bundle);
    }

    @Override
    public void onInternalErrorEvent(int eventCode, Bundle bundle) {
        onErrorEvent(eventCode, bundle);
    }

    private boolean isInternalAvailable(){
        return mInternalPlayer!=null;
    }

    @Override
    public void setDataProvider(IDataProvider dataProvider) {
        if(isInternalAvailable()){
            mInternalPlayer.setDataProvider(dataProvider);
        }
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
        if(isInternalAvailable()){
            mInternalPlayer.setDataSource(data);
        }
    }

    @Override
    public void start() {
        if(isInternalAvailable()){
            mInternalPlayer.start();
        }
    }

    @Override
    public void start(int msc) {
        if(isInternalAvailable()){
            mInternalPlayer.start(msc);
        }
    }

    @Override
    public void pause() {
        if(isInternalAvailable()){
            mInternalPlayer.pause();
        }
    }

    @Override
    public void resume() {
        if(isInternalAvailable()){
            mInternalPlayer.resume();
        }
    }

    @Override
    public void seekTo(int msc) {
        if(isInternalAvailable()){
            mInternalPlayer.seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(isInternalAvailable()){
            mInternalPlayer.stop();
        }
    }

    @Override
    public void reset() {
        if(isInternalAvailable()){
            mInternalPlayer.reset();
        }
    }

    @Override
    public boolean isPlaying() {
        if(isInternalAvailable()){
            return mInternalPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(isInternalAvailable()){
            return mInternalPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(isInternalAvailable()){
            return mInternalPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(isInternalAvailable()){
            return mInternalPlayer.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(isInternalAvailable()){
            return mInternalPlayer.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(isInternalAvailable()){
            return mInternalPlayer.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(isInternalAvailable()){
            return mInternalPlayer.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(isInternalAvailable()){
            mInternalPlayer.changeVideoDefinition(rate);
        }
    }

    @Override
    public void rePlay(int msc) {
        if(isInternalAvailable()){
            mInternalPlayer.rePlay(msc);
        }
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        if(isInternalAvailable()){
            mInternalPlayer.setDecodeMode(decodeMode);
        }
    }


    @Override
    public DecodeMode getDecodeMode() {
        if(isInternalAvailable()){
            return mInternalPlayer.getDecodeMode();
        }
        return null;
    }

    @Override
    public int getPlayerType() {
        if(isInternalAvailable()){
            return mInternalPlayer.getPlayerType();
        }
        return 0;
    }

    @Override
    public int getVideoWidth() {
        if(isInternalAvailable()){
            return mInternalPlayer.getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if(isInternalAvailable()){
            return mInternalPlayer.getVideoHeight();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(isInternalAvailable()){
            return mInternalPlayer.getStatus();
        }
        return 0;
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        super.setDisplay(surfaceHolder);
        if(isInternalAvailable()){
            mInternalPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        super.setAspectRatio(aspectRatio);
        if(isInternalAvailable()){
            mInternalPlayer.setAspectRatio(aspectRatio);
        }
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if(isInternalAvailable()){
            mInternalPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        super.setSurface(surface);
        if(isInternalAvailable()){
            mInternalPlayer.setSurface(surface);
        }
    }

    @Override
    public AspectRatio getAspectRatio() {
        if(isInternalAvailable()){
            return mInternalPlayer.getAspectRatio();
        }
        return null;
    }

    @Override
    public View getRenderView() {
        if(isInternalAvailable()){
            return mInternalPlayer.getRenderView();
        }
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
        PlayerInstanceStateAttach.get().detachInstance(this,this);
        clearPlayerContainer();
        if(mInternalPlayer!=null){
            mInternalPlayer.destroy();
        }
    }
}
