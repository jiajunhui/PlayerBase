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
import android.view.View;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.inter.IRender;

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
        }
    }

    private void updateVideoSize(){
        int videoWidth = mPlayer.getVideoWidth();
        int videoHeight = mPlayer.getVideoHeight();
        if(mRender!=null){
            mRender.onUpdateVideoSize(videoWidth,videoHeight);
        }
    }

    @Override
    public void onSurfaceCreated(IRender render, final Surface surface, int width, int height) {
        setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                mPlayer.setSurface(surface);
                updateVideoSize();
            }
        });
        mPlayer.setSurface(surface);
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