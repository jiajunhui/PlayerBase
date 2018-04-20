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

package com.kk.taurus.playerbase.render;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;

import java.lang.ref.WeakReference;

/**
 * Created by Taurus on 2017/11/19.
 *
 * 使用TextureView时，需要开启硬件加速（系统默认是开启的）。
 * 如果硬件加速是关闭的，会造成{@link SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)}不执行。
 *
 */

public class RenderTextureView extends TextureView implements IRender {

    final String TAG = "RenderTextureView";

    private IRenderCallback mRenderCallback;
    private RenderMeasure mRenderMeasure;

    public RenderTextureView(Context context) {
        this(context, null);
    }

    public RenderTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderMeasure = new RenderMeasure();
        setSurfaceTextureListener(new InternalSurfaceTextureListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mRenderMeasure.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRenderMeasure.getMeasureWidth(),mRenderMeasure.getMeasureHeight());
    }

    @Override
    public void setRenderCallback(IRenderCallback renderCallback) {
        this.mRenderCallback = renderCallback;
    }

    @Override
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if(videoSarNum > 0 && videoSarDen > 0){
            mRenderMeasure.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        mRenderMeasure.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public void updateAspectRatio(AspectRatio aspectRatio) {
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void updateVideoSize(int videoWidth, int videoHeight) {
        PLog.d(TAG,"onUpdateVideoSize : videoWidth = " + videoWidth + " videoHeight = " + videoHeight);
        mRenderMeasure.setVideoSize(videoWidth, videoHeight);
        requestLayout();
    }

    @Override
    public View getRenderView() {
        return this;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PLog.d(TAG,"onTextureViewAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        PLog.d(TAG,"onTextureViewDetachedFromWindow");
    }

    private static final class InternalRenderHolder implements IRenderHolder{

        private WeakReference<SurfaceTexture> mSurfaceTexture;

        public InternalRenderHolder(SurfaceTexture surfaceTexture){
            this.mSurfaceTexture = new WeakReference<>(surfaceTexture);
        }

        @Override
        public void bindPlayer(IPlayer player) {
            if(player!=null && mSurfaceTexture.get()!=null){
                player.setSurface(new Surface(mSurfaceTexture.get()));
            }
        }

    }

    private class InternalSurfaceTextureListener implements SurfaceTextureListener{

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            PLog.d(TAG,"<---onSurfaceTextureAvailable---> : width = " + width + " height = " + height);
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceCreated(new InternalRenderHolder(surface), width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            PLog.d(TAG,"onSurfaceTextureSizeChanged : width = " + width + " height = " + height);
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceChanged(new InternalRenderHolder(surface), 0, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            PLog.d(TAG,"***onSurfaceTextureDestroyed***");
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceDestroy(new InternalRenderHolder(surface));
            }
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }

    }


}
