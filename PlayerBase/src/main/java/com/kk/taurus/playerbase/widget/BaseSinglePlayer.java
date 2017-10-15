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
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.ISinglePlayer;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.ViewType;

/**
 * Created by Taurus on 2017/3/25.
 */

public abstract class BaseSinglePlayer extends FrameLayout implements ISinglePlayer {

    protected int mStatus = STATUS_IDLE;
    protected int mTargetStatus = STATUS_IDLE;
    private OnErrorListener mOnErrorListener;
    private OnPlayerEventListener mOnPlayerEventListener;
    protected int startSeekPos;
    private DecodeMode mDecodeMode;
    private ViewType mViewType;
    private AspectRatio mAspectRatio = AspectRatio.AspectRatio_ORIGIN;
    protected boolean useDefaultRender = true;

    public BaseSinglePlayer(Context context) {
        super(context);
        init(context);
    }

    protected void onStartSeek() {
        if(startSeekPos > 0){
            seekTo(startSeekPos);
            startSeekPos = -1;
        }
    }

    protected void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initPlayerView(context);
    }

    private void initPlayerView(Context context) {
        addView(getPlayerView(context),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setDisplay(SurfaceHolder surfaceHolder){

    }

    public void setUseDefaultRender(boolean useDefaultRender){
        this.useDefaultRender = useDefaultRender;
    }

    public abstract View getPlayerView(Context context);

    public void setDecodeMode(DecodeMode mDecodeMode){
        this.mDecodeMode = mDecodeMode;
    }

    @Override
    public View getRenderView() {
        return null;
    }

    public void setViewType(ViewType viewType) {
        this.mViewType = viewType;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        mAspectRatio = aspectRatio;
    }

    public AspectRatio getAspectRatio(){
        return mAspectRatio;
    }

    public void onClickResume() {

    }

    public void setOnErrorListener(OnErrorListener onErrorListener){
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    protected void onErrorEvent(int eventCode, Bundle bundle){
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(eventCode,bundle);
        }
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        if(mOnPlayerEventListener!=null){
            mOnPlayerEventListener.onPlayerEvent(eventCode,bundle);
        }
    }

    public int getStatus() {
        return mStatus;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void destroy() {
        mOnErrorListener = null;
        mOnPlayerEventListener = null;
    }

}
