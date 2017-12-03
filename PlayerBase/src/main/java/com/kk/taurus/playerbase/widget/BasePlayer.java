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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.BaseExtendEventBox;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.widget.plan.InternalPlayerManager;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.RenderCallbackProxy;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.view.RenderSurfaceView;
import com.kk.taurus.playerbase.view.RenderTextureView;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public abstract class BasePlayer extends BaseBindPlayerEventReceiver implements InternalPlayerManager.OnInternalPlayerListener {

    private ViewType mViewType;
    private AspectRatio mAspectRatio;

    private BaseExtendEventBox extendEventBox;

    private boolean needProxyRenderEvent;
    private RenderCallbackProxy mRenderCallbackProxy;

    private boolean isRenderAvailable;

    public BasePlayer(@NonNull Context context) {
        super(context);
    }

    public BasePlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        extendEventBox = getExtendEventBox();
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
    protected void onPlayerContainerHasInit(Context context) {
        super.onPlayerContainerHasInit(context);
        setWidgetMode(ConfigLoader.getWidgetMode());
    }

    @Override
    protected void onCoversHasInit(Context context) {
        super.onCoversHasInit(context);
        onBindPlayer(this,this);
    }

    @Override
    public void setDataProvider(IDataProvider dataProvider){
        InternalPlayerManager.get().setDataProvider(dataProvider);
    }

    protected void setWidgetMode(int widgetMode){
        InternalPlayerManager.get().updateWidgetMode(mAppContext,widgetMode);
    }

    /**
     * 传入一个可用的渲染视图，RenderSurfaceView或者RenderTextureView.
     * 如果使用了VideoView类型的解码器，请不要调用此方法.
     * @param render
     */
    public void setRenderViewForDecoder(IRender render){
        if(getWidgetMode()==WIDGET_MODE_VIDEO_VIEW)
            return;
        if(mRenderCallbackProxy!=null){
            mRenderCallbackProxy.destroy();
        }
        mRenderCallbackProxy = new RenderCallbackProxy(this,render);
        mRenderCallbackProxy.proxy();
        if(render instanceof View){
            addViewToPlayerContainer((View) render,true, null);
            needProxyRenderEvent = true;
            isRenderAvailable = true;
        }
    }

    public void setDisplayRotation(float rotation){
        View renderView = getRenderView();
        if(renderView!=null){
            renderView.setRotation(rotation);
        }
    }

    @Override
    public void onInternalPlayerEvent(int eventCode, Bundle bundle) {
        onPlayerEvent(eventCode, bundle);
    }

    @Override
    public void onInternalErrorEvent(int errorCode, Bundle bundle) {
        onErrorEvent(errorCode, bundle);
    }

    /**
     * send some event by player,all receivers can receive this event.
     * @param eventCode
     * @param bundle
     */
    public void sendEvent(int eventCode, Bundle bundle){
        onPlayerEvent(eventCode, bundle);
    }

    @Override
    protected void onPlayerEvent(int eventCode, Bundle bundle) {
        if(needProxyRenderEvent){
            mRenderCallbackProxy.proxyEvent(eventCode, bundle);
        }
        super.onPlayerEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                //当组件模式设置为decoder模式时，且没有设置渲染视图时，此处自动为decoder设置一个渲染视图。
                if(getWidgetMode()==WIDGET_MODE_DECODER
                        && InternalPlayerManager.get().isDataSourceAvailable()
                        && !isRenderAvailable){
                    IRender render;
                    if(getViewType()==ViewType.TEXTUREVIEW){
                        render = new RenderTextureView(mAppContext);
                    }else{
                        render = new RenderSurfaceView(mAppContext);
                    }
                    setRenderViewForDecoder(render);
                }
                break;
        }
    }

    public int getWidgetMode() {
        return InternalPlayerManager.get().getWidgetMode();
    }

    @Override
    public void setDataSource(VideoData data) {
        onDataSourceAvailable();
        InternalPlayerManager.get().setDataSource(data);
    }

    @Override
    public void updatePlayerType(int type) {
        InternalPlayerManager.get().updatePlayerType(type);
    }

    @Override
    public void start() {
        InternalPlayerManager.get().start();
    }

    @Override
    public void start(int msc) {
        InternalPlayerManager.get().start(msc);
    }

    @Override
    public void pause() {
        InternalPlayerManager.get().pause();
    }

    @Override
    public void resume() {
        InternalPlayerManager.get().resume();
    }

    @Override
    public void seekTo(int msc) {
        InternalPlayerManager.get().seekTo(msc);
    }

    @Override
    public void stop() {
        InternalPlayerManager.get().stop();
    }

    @Override
    public void reset() {
        InternalPlayerManager.get().reset();
    }

    @Override
    public void rePlay(int msc) {
        InternalPlayerManager.get().rePlay(msc);
    }

    @Override
    public boolean isPlaying() {
        return InternalPlayerManager.get().isPlaying();
    }

    @Override
    public int getCurrentPosition() {
        return InternalPlayerManager.get().getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return InternalPlayerManager.get().getDuration();
    }

    @Override
    public int getBufferPercentage() {
        return InternalPlayerManager.get().getBufferPercentage();
    }

    @Override
    public int getAudioSessionId() {
        return InternalPlayerManager.get().getAudioSessionId();
    }

    @Override
    public int getVideoWidth() {
        return InternalPlayerManager.get().getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return InternalPlayerManager.get().getVideoHeight();
    }

    @Override
    public Rate getCurrentDefinition() {
        return InternalPlayerManager.get().getCurrentDefinition();
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        return InternalPlayerManager.get().getVideoDefinitions();
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        InternalPlayerManager.get().changeVideoDefinition(rate);
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        InternalPlayerManager.get().setDecodeMode(decodeMode);
    }

    @Override
    public DecodeMode getDecodeMode() {
        return InternalPlayerManager.get().getDecodeMode();
    }

    @Override
    public void setViewType(ViewType viewType) {
        this.mViewType = viewType;
        InternalPlayerManager.get().setViewType(viewType);
    }

    @Override
    public ViewType getViewType() {
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            return mViewType;
        }
        return InternalPlayerManager.get().getViewType();
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder){
        isRenderAvailable = surfaceHolder!=null;
        InternalPlayerManager.get().setDisplay(surfaceHolder);
    }

    @Override
    public void setSurface(Surface surface) {
        isRenderAvailable = surface!=null;
        InternalPlayerManager.get().setSurface(surface);
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.mAspectRatio = aspectRatio;
        if(mRenderCallbackProxy!=null){
            mRenderCallbackProxy.onAspectUpdate(aspectRatio);
        }
        InternalPlayerManager.get().setAspectRatio(aspectRatio);
    }

    @Override
    public AspectRatio getAspectRatio() {
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            return mAspectRatio;
        }
        return InternalPlayerManager.get().getAspectRatio();
    }

    @Override
    public int getStatus() {
        return InternalPlayerManager.get().getStatus();
    }

    @Override
    public View getRenderView() {
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            return getPlayerRenderView();
        }
        return InternalPlayerManager.get().getRenderView();
    }

    @Override
    public void destroy() {
        destroyExtendBox();
        destroyContainer();
        destroyInternalPlayer(true);
    }

    public void destroy(boolean destroyInternalPlayer) {
        destroyExtendBox();
        destroyContainer();
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

    private void destroyContainer() {
        //先发出一个容器销毁的event事件
        sendEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY,null);
        if(getWidgetMode()==WIDGET_MODE_DECODER){
            //组件模式为解码器时，同时要清掉RenderView
            clearPlayerContainer();
        }
        //然后移除容器的播放核心的事件监听器
        InternalPlayerManager.get().removeOnInternalPlayerListener(this);
        //最后清除所有事件监听器，解除已绑定的receiver集合.
        super.destroy();
    }

    /**
     * 销毁扩展事件盒子
     */
    private void destroyExtendBox(){
        if(extendEventBox!=null){
            extendEventBox.destroyExtendBox();
        }
    }

}
