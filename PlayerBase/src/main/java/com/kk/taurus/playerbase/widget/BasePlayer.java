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
import com.kk.taurus.playerbase.inter.IRenderProxyGetter;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.InternalPlayerManager;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.RenderCallbackProxy;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.view.RenderSurfaceView;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public abstract class BasePlayer extends BaseSettingPlayer implements IRenderProxyGetter {

    private final String TAG = "BasePlayer";

    private int mWidgetMode;
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
        this.mWidgetMode = widgetMode;
        InternalPlayerManager.get().updateWidgetMode(mAppContext,mWidgetMode);
    }

    /**
     * 传入一个可用的渲染视图，RenderSurfaceView或者RenderTextureView.
     * 如果使用了VideoView类型的解码器，请不要调用此方法.
     * @param render
     */
    public void setRenderViewForDecoder(IRender render){
        setRenderViewForDecoder(render,false);
    }

    private void setRenderViewForDecoder(IRender render, boolean hasPrepared){
        if(getWidgetMode()==WIDGET_MODE_VIDEO_VIEW)
            return;
        if(mRenderCallbackProxy!=null){
            mRenderCallbackProxy.destroy();
        }
        mRenderCallbackProxy = new RenderCallbackProxy(this,render,this);
        mRenderCallbackProxy.proxy(hasPrepared);
        if(render instanceof View){
            addViewToPlayerContainer((View) render,true);
            needProxyRenderEvent = true;
            isRenderAvailable = true;
        }
    }

    @Override
    public AspectRatio getRenderAspectRatio() {
        return super.getAspectRatio();
    }

    @Override
    public int getSourceVideoWidth() {
        return getVideoWidth();
    }

    @Override
    public int getSourceVideoHeight() {
        return getVideoHeight();
    }

    /**
     * send some event by player,all receivers can receive this event.
     * @param eventCode
     * @param bundle
     */
    public void sendEvent(int eventCode, Bundle bundle){
        if(needProxyRenderEvent){
            mRenderCallbackProxy.proxyEvent(eventCode, bundle);
        }
        onPlayerEvent(eventCode, bundle);
    }

    @Override
    protected void onPlayerEvent(int eventCode, Bundle bundle) {
        super.onPlayerEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                //当组件模式设置为decoder模式时，且没有设置渲染视图时，此处自动为decoder设置一个渲染视图。
                if(getWidgetMode()==WIDGET_MODE_DECODER && !isRenderAvailable){
                    setRenderViewForDecoder(new RenderSurfaceView(mAppContext), true);
                }
                break;
        }
    }

    public int getWidgetMode() {
        return mWidgetMode;
    }

    @Override
    public void setDataSource(VideoData data) {
        super.setDataSource(data);
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
        super.changeVideoDefinition(rate);
        InternalPlayerManager.get().changeVideoDefinition(rate);
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        super.setDecodeMode(decodeMode);
        InternalPlayerManager.get().setDecodeMode(decodeMode);
    }

    @Override
    public void setViewType(ViewType viewType) {
        InternalPlayerManager.get().setViewType(viewType);
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
        super.setAspectRatio(aspectRatio);
        if(mRenderCallbackProxy!=null){
            mRenderCallbackProxy.onAspectUpdate(aspectRatio);
        }
        InternalPlayerManager.get().setAspectRatio(aspectRatio);
    }

    @Override
    public View getRenderView() {
        return InternalPlayerManager.get().getRenderView();
    }

    @Override
    public void destroy() {
        sendEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY,null);
        super.destroy();
    }

    public void destroy(boolean destroyInternalPlayer) {
        this.destroy();
        if(destroyInternalPlayer){
            InternalPlayerManager.get().destroy();
        }
    }
}
