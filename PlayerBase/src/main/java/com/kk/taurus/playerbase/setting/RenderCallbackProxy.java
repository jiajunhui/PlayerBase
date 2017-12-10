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

package com.kk.taurus.playerbase.setting;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.widget.BasePlayer;

/**
 * Created by Taurus on 2017/11/19.
 */

public class RenderCallbackProxy implements IRender.IRenderCallback {

    private IPlayer mPlayer;
    private IRender mRender;
    private OnPreparedListener mOnPreparedListener;

    public RenderCallbackProxy(IPlayer player, IRender render){
        this.mPlayer = player;
        this.mRender = render;
    }

    public void proxy(){
        mRender.setRenderCallback(this);
    }

    public void destroy(){
        if(this.mRender!=null){
            mOnPreparedListener = null;
            this.mRender.setRenderCallback(null);
        }
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public void onAspectUpdate(AspectRatio aspectRatio){
        if(aspectRatio!=null && mRender!=null){
            mRender.onUpdateAspectRatio(aspectRatio);
        }
    }

    public void proxyEvent(int eventCode, Bundle bundle){
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                updateVideoSize();
                if(mOnPreparedListener!=null){
                    mOnPreparedListener.onPrepared();
                }
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
                mOnPreparedListener=null;
                /**
                 * 当播放器销毁时，渲染视图置空。使渲染视图状态为不可用。
                 * 这样，后面再设置新的资源时，当prepared回调后会重新设置渲染视图。
                 * {@link BasePlayer#setDisplay(SurfaceHolder)}
                 */
                mPlayer.setDisplay(null);
                /**
                 * 当播放器销毁时，渲染视图置空。使渲染视图状态为不可用。
                 * {@link BasePlayer#setSurface(Surface)}
                 */
                mPlayer.setSurface(null);
                break;
        }
    }

    private void updateVideoSize(){
        int videoWidth = mPlayer.getVideoWidth();
        int videoHeight = mPlayer.getVideoHeight();
        if(mRender!=null){
            mRender.onUpdateVideoSize(videoWidth,videoHeight);
        }
    }

    private void setSurface(Surface surface){
        if(mPlayer!=null){
            mPlayer.setSurface(surface);
        }
    }

    @Override
    public void onSurfaceCreated(IRender render, final Surface surface, int width, int height) {
        setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                setSurface(surface);
                updateVideoSize();
            }
        });
        setSurface(surface);
        updateVideoSize();
    }

    @Override
    public void onSurfaceChanged(IRender render, Surface surface, int width, int height) {
        updateVideoSize();
    }

    @Override
    public void onSurfaceDestroy(IRender render, Surface surface) {

    }

    @Override
    public void onRenderViewAttachedToWindow(IRender render) {
        if(mRender!=null && mRender instanceof View){
            ((View)mRender).requestLayout();
        }
    }

    @Override
    public void onRenderViewDetachedFromWindow(IRender render) {

    }

    public interface OnPreparedListener{
        void onPrepared();
    }
}