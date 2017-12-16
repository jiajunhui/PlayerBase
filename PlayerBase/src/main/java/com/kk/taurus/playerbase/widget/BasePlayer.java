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
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.eventHandler.BaseExtendEventBox;
import com.kk.taurus.playerbase.setting.RenderCallbackProxy;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.view.RenderSurfaceView;
import com.kk.taurus.playerbase.view.RenderTextureView;

/**
 * Created by Taurus on 2017/3/28.
 * 播放器抽象基类
 */

public abstract class BasePlayer extends BaseBindPlayerEventReceiver  {

    protected ViewType mViewType = ViewType.TEXTUREVIEW;
    protected AspectRatio mAspectRatio = AspectRatio.AspectRatio_ORIGIN;

    protected BaseExtendEventBox extendEventBox;

    private int mWidgetMode;
    private int mSignPlayerType;

    private boolean needProxyRenderEvent;
    private RenderCallbackProxy mRenderCallbackProxy;

    private boolean isRenderAvailable;

    private VideoData mDataSource;

    private int mRecordPos;

    public BasePlayer(Context context) {
        super(context);
        settingDefault();
        notifyPlayerWidget(context);
    }

    public BasePlayer(Context context, int widgetMode, int playerType){
        super(context);
        this.mWidgetMode = widgetMode;
        this.mSignPlayerType = playerType;
        notifyPlayerWidget(context);
    }

    public BasePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        settingDefault();
        notifyPlayerWidget(context);
    }

    private void settingDefault(){
        this.mWidgetMode = ConfigLoader.getWidgetMode();
        this.mSignPlayerType = ConfigLoader.getDefaultType(mWidgetMode);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        extendEventBox = getExtendEventBox();
        mWidgetMode = ConfigLoader.getWidgetMode();
    }

    protected abstract BaseExtendEventBox getExtendEventBox();

    @Override
    protected void onCoversHasInit(Context context) {
        super.onCoversHasInit(context);
        onBindPlayer(this,this);
    }

    public void setWidgetMode(int widgetMode){
        boolean change = this.mWidgetMode!=widgetMode;
        this.mWidgetMode = widgetMode;
        if(change){
            onWidgetModeChange(widgetMode);
        }
    }

    protected abstract void onWidgetModeChange(int widgetMode);

    public int getWidgetMode() {
        return mWidgetMode;
    }

    @Override
    public void updatePlayerType(int type) {
        boolean change = getPlayerType()!=type;
        this.mSignPlayerType = type;
        if(change){
            onPlayerTypeChange(type);
        }
    }

    protected abstract void onPlayerTypeChange(int type);

    protected int getSignPlayerType() {
        return mSignPlayerType;
    }

    @Override
    public void setDataSource(VideoData data) {
        this.mDataSource = data;
    }

    protected boolean isDataSourceAvailable(){
        return mDataSource!=null;
    }

    private void releaseRenderCallbackProxy() {
        if(mRenderCallbackProxy!=null){
            mRenderCallbackProxy.destroy();
        }
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
        addViewToPlayerContainer(render.getRenderView(),true, null);
        needProxyRenderEvent = true;
        isRenderAvailable = true;
    }

    public void setDisplayRotation(float rotation){
        View renderView = getRenderView();
        if(renderView!=null){
            renderView.setRotation(rotation);
        }
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
                        && isDataSourceAvailable()
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
            case OnPlayerEventListener.EVENT_CODE_ON_PLAYER_TIMER_UPDATE:
                if(getDuration()>0 && getDuration() > getCurrentPosition()){
                    mRecordPos = getCurrentPosition();
                }
                break;
        }
    }

    public int getRecordPos(){
        return mRecordPos;
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
    public void setViewType(ViewType viewType) {
        this.mViewType = viewType;
    }

    @Override
    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder){
        isRenderAvailable = surfaceHolder!=null;
    }

    @Override
    public void setSurface(Surface surface) {
        isRenderAvailable = surface!=null;
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.mAspectRatio = aspectRatio;
        if(mRenderCallbackProxy!=null){
            mRenderCallbackProxy.onAspectUpdate(aspectRatio);
        }
    }

    public boolean isExpectedBufferAvailable(){
        return (getBufferPercentage()*getDuration()/100) > getCurrentPosition();
    }

    public void setScreenOrientationLandscape(boolean landscape) {
        /** modify 2017/11/17
         *
         *  this operation is not dependent on Activity context as much as possible.
         *
         * */
        int code = landscape?OnPlayerEventListener.EVENT_CODE_ON_INTENT_SET_SCREEN_ORIENTATION_LANDSCAPE:OnPlayerEventListener.EVENT_CODE_ON_INTENT_SET_SCREEN_ORIENTATION_PORTRAIT;
        onPlayerEvent(code,null);
    }

    public boolean isLandscape() {
        return mAppContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void destroy() {
        destroyExtendBox();
        destroyContainer();
        super.destroy();
    }

    protected void destroyContainer() {
        releaseRenderCallbackProxy();
        isRenderAvailable = false;
        //先发出一个容器销毁的event事件
        sendEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY,null);
    }

    /**
     * 销毁扩展事件盒子
     */
    protected void destroyExtendBox(){
        if(extendEventBox!=null){
            extendEventBox.destroyExtendBox();
        }
    }

}
