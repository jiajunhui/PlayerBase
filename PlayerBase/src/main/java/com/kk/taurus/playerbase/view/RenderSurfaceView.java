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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.RenderMeasure;
import com.kk.taurus.playerbase.utils.PLog;

/**
 * Created by Taurus on 2017/11/19.
 */

public class RenderSurfaceView extends SurfaceView implements IRender, SurfaceHolder.Callback {

    private IRenderCallback mRenderCallback;
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
        this.mRenderCallback = renderCallback;
    }

    @Override
    public void setVideoRotation(int degree) {
        PLog.d(TAG,"surface view not support rotation ... ");
    }

    @Override
    public void onUpdateAspectRatio(AspectRatio aspectRatio) {
        PLog.d(TAG,"onUpdateAspectRatio ... ");
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        PLog.d(TAG,"onSurfaceViewDetachedFromWindow");
        if(mRenderCallback!=null){
            mRenderCallback.onRenderViewDetachedFromWindow(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PLog.d(TAG,"onSurfaceViewAttachedToWindow");
        if(mRenderCallback!=null){
            mRenderCallback.onRenderViewAttachedToWindow(this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        PLog.d(TAG,"<---surfaceCreated---->");
        if(mRenderCallback!=null){
            mRenderCallback.onSurfaceCreated(this,holder.getSurface(),0,0);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        PLog.d(TAG,"surfaceChanged : width = " + width + " height = " + height);
        getHolder().setFixedSize(width, height);
        requestLayout();
        if(mRenderCallback!=null){
            mRenderCallback.onSurfaceChanged(this,holder.getSurface(),width,height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        PLog.d(TAG,"***surfaceDestroyed***");
        if(mRenderCallback!=null){
            mRenderCallback.onSurfaceDestroy(this, null);
        }
    }
}
