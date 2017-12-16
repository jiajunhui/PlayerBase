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
import com.kk.taurus.playerbase.eventHandler.BaseExtendEventBox;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.widget.plan.InternalPlayerManager;

import java.util.List;

/**
 * Created by Taurus on 2017/12/13.
 * 此类为单实例解码器的播放容器，即不支持解码器的多实例。
 * 如果您接入的解码方案支持多实例且您需要使用多实例的场景，请参见{@link BaseMultiInstancePlayer}
 */

public abstract class BaseSingleDecoderPlayer extends BasePlayer implements InternalPlayerManager.OnInternalPlayerListener{

    public BaseSingleDecoderPlayer(Context context) {
        super(context);
    }

    public BaseSingleDecoderPlayer(Context context, int widgetMode, int type){
        super(context, widgetMode, type);
    }

    public BaseSingleDecoderPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        InternalPlayerManager.get().attachPlayer(this);
    }

    /**
     * 该方法会返回一个View（仅当组件类型WIDGET_MODE为VideoView类型时）。
     *
     * 通过{@link InternalPlayerManager}统一控制。
     *
     * 通过{@link PlayerType}进行设置VideoView类型。
     * WIDGET_MODE为VideoView类型时，通过InternalPlayerManager会初始化你设置好的VideoView类型的对象，并返回给上层。
     *
     * 通过{@link DecoderType}进行设置Decoder类型。
     * WIDGET_MODE为DECODER类型时，InternalPlayerManager只是初始化了你设置好的解码器类型。并没有View返回。
     * @param context
     * @return
     */
    protected View getPlayerWidget(Context context) {
        InternalPlayerManager.get().updateWidgetMode(context,getWidgetMode());
        return InternalPlayerManager.get().getRenderView();
    }

    private void onDataSourceAvailable() {
        InternalPlayerManager.get().setOnInternalPlayerListener(this);
    }

    @Override
    protected void onReceiverCollectionsHasBind() {
        super.onReceiverCollectionsHasBind();
        InternalPlayerManager.get().setOnInternalPlayerListener(this);
    }

    protected abstract BaseExtendEventBox getExtendEventBox();

    @Override
    public void setDataProvider(IDataProvider dataProvider){
        InternalPlayerManager.get().setDataProvider(dataProvider);
    }

    @Override
    protected void onWidgetModeChange(int widgetMode) {
        InternalPlayerManager.get().updateWidgetMode(mAppContext,widgetMode);
    }

    @Override
    public void onInternalPlayerEvent(int eventCode, Bundle bundle) {
        onPlayerEvent(eventCode, bundle);
    }

    @Override
    public void onInternalErrorEvent(int errorCode, Bundle bundle) {
        onErrorEvent(errorCode, bundle);
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
        onDataSourceAvailable();
        InternalPlayerManager.get().attachPlayer(this);
        InternalPlayerManager.get().setDataSource(this, data);
    }

    @Override
    protected void onPlayerTypeChange(int type) {
        InternalPlayerManager.get().updatePlayerType(this, type);
        notifyPlayerWidget(mAppContext);
    }

    @Override
    public void start() {
        InternalPlayerManager.get().start(this);
    }

    @Override
    public void start(int msc) {
        InternalPlayerManager.get().start(this, msc);
    }

    @Override
    public void pause() {
        InternalPlayerManager.get().pause(this);
    }

    @Override
    public void resume() {
        InternalPlayerManager.get().resume(this);
    }

    @Override
    public void seekTo(int msc) {
        InternalPlayerManager.get().seekTo(this, msc);
    }

    @Override
    public void stop() {
        InternalPlayerManager.get().stop(this);
    }

    @Override
    public void reset() {
        InternalPlayerManager.get().reset(this);
    }

    @Override
    public void rePlay(int msc) {
        InternalPlayerManager.get().rePlay(this, msc);
    }

    @Override
    public boolean isPlaying() {
        return InternalPlayerManager.get().isPlaying(this);
    }

    @Override
    public int getCurrentPosition() {
        return InternalPlayerManager.get().getCurrentPosition(this);
    }

    @Override
    public int getDuration() {
        return InternalPlayerManager.get().getDuration(this);
    }

    @Override
    public int getBufferPercentage() {
        return InternalPlayerManager.get().getBufferPercentage(this);
    }

    @Override
    public int getAudioSessionId() {
        return InternalPlayerManager.get().getAudioSessionId(this);
    }

    @Override
    public int getVideoWidth() {
        return InternalPlayerManager.get().getVideoWidth(this);
    }

    @Override
    public int getVideoHeight() {
        return InternalPlayerManager.get().getVideoHeight(this);
    }

    @Override
    public Rate getCurrentDefinition() {
        return InternalPlayerManager.get().getCurrentDefinition(this);
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        return InternalPlayerManager.get().getVideoDefinitions(this);
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        InternalPlayerManager.get().changeVideoDefinition(this, rate);
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        InternalPlayerManager.get().setDecodeMode(this, decodeMode);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        InternalPlayerManager.get().setVolume(leftVolume, rightVolume);
    }

    @Override
    public DecodeMode getDecodeMode() {
        return InternalPlayerManager.get().getDecodeMode(this);
    }

    @Override
    public void setViewType(ViewType viewType) {
        super.setViewType(viewType);
        InternalPlayerManager.get().setViewType(this, viewType);
    }

    @Override
    public ViewType getViewType() {
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            return mViewType;
        }
        return InternalPlayerManager.get().getViewType(this);
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder){
        super.setDisplay(surfaceHolder);
        InternalPlayerManager.get().setDisplay(this, surfaceHolder);
    }

    @Override
    public void setSurface(Surface surface) {
        super.setSurface(surface);
        InternalPlayerManager.get().setSurface(this, surface);
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        super.setAspectRatio(aspectRatio);
        InternalPlayerManager.get().setAspectRatio(this, aspectRatio);
    }

    @Override
    public AspectRatio getAspectRatio() {
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            return mAspectRatio;
        }
        return InternalPlayerManager.get().getAspectRatio(this);
    }

    @Override
    public int getStatus() {
        return InternalPlayerManager.get().getStatus(this);
    }

    @Override
    public int getPlayerType() {
        return InternalPlayerManager.get().getPlayerType(this);
    }

    @Override
    public View getRenderView() {
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            return getPlayerRenderView();
        }
        return InternalPlayerManager.get().getRenderView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy(false);
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyInternalPlayer(true);
    }

    public void destroy(boolean destroyInternalPlayer) {
        super.destroy();
        destroyInternalPlayer(destroyInternalPlayer);
    }

    /**
     * 是否销毁播放核心
     * @param destroyInternalPlayer
     */
    private void destroyInternalPlayer(boolean destroyInternalPlayer) {
        //当明确要销毁播放核心，或者组件模式为VideoView类型时，销毁播放核心。
        if(destroyInternalPlayer || getWidgetMode()==WIDGET_MODE_VIDEO_VIEW){
            InternalPlayerManager.get().destroy();
        }
    }

    protected void destroyContainer() {
        super.destroyContainer();
        //移除宿主IPlayer的关联
        InternalPlayerManager.get().detachPlayer(this);
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            //组件模式为解码器时，同时要清掉RenderView
            clearPlayerContainer();
        }
        //然后移除容器的播放核心的事件监听器
        InternalPlayerManager.get().removeOnInternalPlayerListener(this);
    }

}
