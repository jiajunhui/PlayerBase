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

package com.kk.taurus.playerbase.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.RenderMeasure;
import com.kk.taurus.playerbase.utils.PLog;

/**
 * Created by Taurus on 2017/11/19.
 *
 * 使用TextureView时，需要开启硬件加速（系统默认是开启的）。
 * 如果硬件加速是关闭的，会造成{@link SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)}不执行。
 *
 */

public class RenderTextureView extends TextureView implements IRender, TextureView.SurfaceTextureListener {

    private IRenderCallback mRenderCallback;
    private RenderMeasure mRenderMeasure;

    public RenderTextureView(Context context) {
        this(context, null);
    }

    public RenderTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderMeasure = new RenderMeasure();
        setSurfaceTextureListener(this);
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
    public void setVideoRotation(int degree) {
        mRenderMeasure.setVideoRotation(degree);
        setRotation(degree);
    }

    @Override
    public void onUpdateAspectRatio(AspectRatio aspectRatio) {
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void onUpdateVideoSize(int videoWidth, int videoHeight) {
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
        if(mRenderCallback!=null){
            mRenderCallback.onRenderViewAttachedToWindow(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        PLog.d(TAG,"onTextureViewDetachedFromWindow");
        if(mRenderCallback!=null){
            mRenderCallback.onRenderViewDetachedFromWindow(this);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        PLog.d(TAG,"<---onSurfaceTextureAvailable---> : width = " + width + " height = " + height);
        if(mRenderCallback!=null){
            mRenderCallback.onSurfaceCreated(this, new Surface(surface), width, height);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        PLog.d(TAG,"onSurfaceTextureSizeChanged : width = " + width + " height = " + height);
        if(mRenderCallback!=null){
            mRenderCallback.onSurfaceChanged(this, new Surface(surface), width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        PLog.d(TAG,"***onSurfaceTextureDestroyed***");
        if(mRenderCallback!=null){
            mRenderCallback.onSurfaceDestroy(this, new Surface(surface));
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
