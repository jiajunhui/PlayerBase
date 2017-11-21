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
import android.util.Log;
import android.view.TextureView;

import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.inter.IUseSurface;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.RenderMeasure;

/**
 * Created by Taurus on 2017/11/19.
 */

public class RenderTextureView extends TextureView implements IUseSurface, IRender, TextureView.SurfaceTextureListener {

    private IRenderSurfaceTextureCallback mSurfaceTextureCallback;
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
        if(renderCallback instanceof IRenderSurfaceTextureCallback){
            this.mSurfaceTextureCallback = (IRenderSurfaceTextureCallback) renderCallback;
        }
    }

    @Override
    public void onUpdateAspectRatio(AspectRatio aspectRatio) {
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void onUpdateVideoSize(int videoWidth, int videoHeight) {
        Log.d(TAG,"onUpdateVideoSize : videoWidth = " + videoWidth + " videoHeight = " + videoHeight);
        mRenderMeasure.setVideoSize(videoWidth, videoHeight);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG,"onTextureViewAttachedToWindow");
        if(mSurfaceTextureCallback!=null){
            mSurfaceTextureCallback.onSurfaceTextureAttachedToWindow();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG,"onTextureViewDetachedFromWindow");
        if(mSurfaceTextureCallback!=null){
            mSurfaceTextureCallback.onSurfaceTextureDetachedFromWindow();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(TAG,"<---onSurfaceTextureAvailable---> : width = " + width + " height = " + height);
        if(mSurfaceTextureCallback!=null){
            mSurfaceTextureCallback.onSurfaceTextureAvailable(surface, width, height);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.d(TAG,"onSurfaceTextureSizeChanged : width = " + width + " height = " + height);
        if(mSurfaceTextureCallback!=null){
            mSurfaceTextureCallback.onSurfaceTextureSizeChanged(surface, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(TAG,"***onSurfaceTextureDestroyed***");
        return mSurfaceTextureCallback.onSurfaceTextureDestroyed(surface);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if(mSurfaceTextureCallback!=null){
            mSurfaceTextureCallback.onSurfaceTextureUpdated(surface);
        }
    }
}
