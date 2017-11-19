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

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.inter.IRenderProxyGetter;
import com.kk.taurus.playerbase.inter.IUseSurface;
import com.kk.taurus.playerbase.inter.IUseSurfaceHolder;

/**
 * Created by Taurus on 2017/11/19.
 */

public class RenderCallbackProxy {

    private IPlayer mPlayer;
    private IRender mRender;
    private IRenderProxyGetter mProxyGetter;
    private OnPreparedListener mOnPreparedListener;
    private boolean hasPrepared;

    private int mWidth, mHeight;

    public RenderCallbackProxy(IPlayer player, IRender render, IRenderProxyGetter proxyGetter){
        this.mPlayer = player;
        this.mRender = render;
        this.mProxyGetter = proxyGetter;
    }

    public void proxy(boolean hasPrepared){
        this.hasPrepared = hasPrepared;
        attachCallback();
    }

    public void destroy(){
        if(this.mRender!=null){
            this.mRender.setRenderCallback(null);
        }
    }

    private void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
        if(hasPrepared && mOnPreparedListener!=null){
            mOnPreparedListener.onPrepared();
        }
    }

    public void onAspectUpdate(AspectRatio aspectRatio){
        if(aspectRatio!=null && mRender!=null && mProxyGetter!=null){
            if(mWidth > 0 && mHeight > 0){
                mRender.onUpdateAspectRatio(aspectRatio,mWidth,mHeight
                        , mProxyGetter.getSourceVideoWidth(),mProxyGetter.getSourceVideoHeight());
            }
        }
    }

    public void proxyEvent(int eventCode, Bundle bundle){
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                hasPrepared = false;
                break;
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                if(mOnPreparedListener!=null){
                    mOnPreparedListener.onPrepared();
                }
                hasPrepared = true;
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY:
                hasPrepared = false;
                destroy();
                break;
        }
    }

    private void attachCallback(){
        if(this.mRender instanceof IUseSurfaceHolder){
            this.mRender.setRenderCallback(new IRender.IRenderSurfaceHolderCallback() {
                @Override
                public void onSurfaceCreated(final SurfaceHolder holder) {
                    setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared() {
                            mPlayer.setDisplay(holder);
                        }
                    });
                }

                @Override
                public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    mWidth = width;
                    mHeight = height;
                    if(mRender!=null && mProxyGetter!=null){
                        mRender.onUpdateAspectRatio(mProxyGetter.getRenderAspectRatio()
                                ,width,height
                                ,mProxyGetter.getSourceVideoWidth(),mProxyGetter.getSourceVideoHeight());
                    }
                }

                @Override
                public void onSurfaceDestroyed(SurfaceHolder holder) {

                }

                @Override
                public void onSurfaceViewAttachedToWindow() {

                }

                @Override
                public void onSurfaceViewDetachedFromWindow() {

                }
            });
        }else if(this.mRender instanceof IUseSurface){
            this.mRender.setRenderCallback(new IRender.IRenderSurfaceTextureCallback() {
                @Override
                public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
                    setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared() {
                            mPlayer.setSurface(new Surface(surface));
                        }
                    });
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                    mWidth = width;
                    mHeight = height;
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }

                @Override
                public void onSurfaceTextureAttachedToWindow() {

                }

                @Override
                public void onSurfaceTextureDetachedFromWindow() {

                }
            });
        }
    }

    interface OnPreparedListener{
        void onPrepared();
    }



}
