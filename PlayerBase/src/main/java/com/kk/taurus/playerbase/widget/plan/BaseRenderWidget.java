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
import android.view.View;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.VideoCacheProxy;
import com.kk.taurus.playerbase.inter.IEventBinder;
import com.kk.taurus.playerbase.inter.IRenderWidget;
import com.kk.taurus.playerbase.inter.ITimerGetter;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.TimerCounterProxy;
import com.kk.taurus.playerbase.setting.TimerData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;

/**
 * Created by Taurus on 2017/11/18.
 */

public abstract class BaseRenderWidget implements IRenderWidget, IEventBinder, ITimerGetter, TimerCounterProxy.OnTimerHandlerListener {

    protected Context mContext;
    protected BaseVideoView mRenderWidget;
    protected VideoData mDataSource;
    private int mRenderType;
    private OnErrorListener mOnErrorListener;
    private OnPlayerEventListener mOnPlayerEventListener;
    private DecodeMode mDecodeMode;
    private ViewType mViewType;
    private AspectRatio mAspectRatio = AspectRatio.AspectRatio_ORIGIN;

    private TimerCounterProxy timerCounterProxy;
    private Bundle mBundle;

    public Bundle getBundle(){
        if(mBundle==null){
            mBundle = new Bundle();
        }
        mBundle.clear();
        return mBundle;
    }

    public BaseRenderWidget(Context context){
        this.mContext = context;
        mRenderType = PlayerType.getInstance().getDefaultPlayerType();
        mRenderWidget = initRenderWidget(context);
        timerCounterProxy = new TimerCounterProxy(this);
        timerCounterProxy.setOnTimerHandlerListener(this);
    }

    @Override
    public void setDataSource(VideoData data) {
        this.mDataSource = data.clone();
        if(VideoCacheProxy.get().isVideoCacheOpen()){
            String sourceUrl = data.getData();
            data.setData(VideoCacheProxy.get().proxyVideoUrl(mContext,sourceUrl));
        }
    }

    protected abstract BaseVideoView initRenderWidget(Context context);

    public void setOnErrorListener(OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener mOnPlayerEventListener) {
        this.mOnPlayerEventListener = mOnPlayerEventListener;
    }

    @Override
    public void onBindPlayerEvent(int eventCode, Bundle bundle) {
        timerCounterProxy.proxyPlayerEvent(eventCode, bundle);
        if(this.mOnPlayerEventListener!=null){
            this.mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
        }
    }

    @Override
    public void onBindErrorEvent(int eventCode, Bundle bundle) {
        if(this.mOnErrorListener!=null){
            this.mOnErrorListener.onError(eventCode, bundle);
        }
    }

    @Override
    public void onTimerCounter(TimerData timerData) {
        Bundle bundle = getBundle();
        bundle.putInt(ITimerGetter.KEY_TIMER_CURRENT_POSITION,timerData.getCurrentPosition());
        bundle.putInt(ITimerGetter.KEY_TIMER_DURATION,timerData.getDuration());
        bundle.putInt(ITimerGetter.KEY_TIMER_BUFFER_POSITION,timerData.getBufferPosition());
        onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_PLAYER_TIMER_UPDATE,bundle);
    }

    @Override
    public int getTimerCurrentPosition() {
        return getCurrentPosition();
    }

    @Override
    public int getTimerDuration() {
        return getDuration();
    }

    @Override
    public int getTimerBufferPercentage() {
        return getBufferPercentage();
    }

    public void setDecodeMode(DecodeMode mDecodeMode) {
        this.mDecodeMode = mDecodeMode;
    }

    public void setViewType(ViewType mViewType) {
        this.mViewType = mViewType;
    }

    public void setAspectRatio(AspectRatio mAspectRatio) {
        this.mAspectRatio = mAspectRatio;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    public AspectRatio getAspectRatio() {
        return mAspectRatio;
    }

    public int getRenderType() {
        return mRenderType;
    }

    public void setRenderType(int renderType) {
        boolean needUpdateRenderType = mRenderType!=renderType;
        this.mRenderType = renderType;
        if(needUpdateRenderType){
            initRenderWidget(mContext);
        }
    }

    @Override
    public View getRenderView() {
        return mRenderWidget;
    }
}
