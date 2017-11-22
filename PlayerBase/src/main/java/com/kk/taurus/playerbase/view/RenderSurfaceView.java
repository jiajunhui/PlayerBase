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
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.inter.IUseSurfaceHolder;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.RenderMeasure;

/**
 * Created by Taurus on 2017/11/19.
 */

public class RenderSurfaceView extends SurfaceView implements IUseSurfaceHolder, IRender, SurfaceHolder.Callback {

    private IRenderSurfaceHolderCallback mSurfaceHolderCallback;
    private RenderMeasure mRenderMeasure;

    public RenderSurfaceView(Context context) {
        this(context, null);
    }

    public RenderSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRenderMeasure = new RenderMeasure();
        getHolder().addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRenderMeasure.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mRenderMeasure.getMeasureWidth(),mRenderMeasure.getMeasureHeight());
    }

    @Override
    public void setRenderCallback(IRenderCallback renderCallback) {
        if(renderCallback instanceof IRenderSurfaceHolderCallback){
            this.mSurfaceHolderCallback = (IRenderSurfaceHolderCallback) renderCallback;
        }
    }

    @Override
    public void onUpdateAspectRatio(AspectRatio aspectRatio) {
        Log.d(TAG,"onUpdateAspectRatio ... ");
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void onUpdateVideoSize(int videoWidth, int videoHeight) {
        Log.d(TAG,"onUpdateVideoSize : videoWidth = " + videoWidth + " videoHeight = " + videoHeight);
        mRenderMeasure.setVideoSize(videoWidth, videoHeight);
        requestLayout();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG,"onSurfaceViewDetachedFromWindow");
        if(mSurfaceHolderCallback!=null){
            mSurfaceHolderCallback.onSurfaceViewDetachedFromWindow();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG,"onSurfaceViewAttachedToWindow");
        if(mSurfaceHolderCallback!=null){
            mSurfaceHolderCallback.onSurfaceViewAttachedToWindow();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"<---surfaceCreated---->");
        if(mSurfaceHolderCallback!=null){
            mSurfaceHolderCallback.onSurfaceCreated(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"surfaceChanged : width = " + width + " height = " + height);
        getHolder().setFixedSize(width, height);
        requestLayout();
        if(mSurfaceHolderCallback!=null){
            mSurfaceHolderCallback.onSurfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"***surfaceDestroyed***");
        if(mSurfaceHolderCallback!=null){
            mSurfaceHolderCallback.onSurfaceDestroyed(holder);
        }
    }
}
