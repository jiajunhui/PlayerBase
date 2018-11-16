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
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;

import java.lang.ref.WeakReference;

/**
 * Created by Taurus on 2018/3/17.
 */

public class RenderSurfaceView extends SurfaceView implements IRender {

    final String TAG = "RenderSurfaceView";

    private IRenderCallback mRenderCallback;
    private RenderMeasure mRenderMeasure;
    private boolean isReleased;

    public RenderSurfaceView(Context context) {
        this(context, null);
    }

    public RenderSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRenderMeasure = new RenderMeasure();
        getHolder().addCallback(new InternalSurfaceHolderCallback());
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
    public void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen) {
        if(videoSarNum > 0 && videoSarDen > 0){
            mRenderMeasure.setVideoSampleAspectRatio(videoSarNum, videoSarDen);
            requestLayout();
        }
    }

    @Override
    public void setVideoRotation(int degree) {
        PLog.e(TAG,"surface view not support rotation ... ");
    }

    @Override
    public void updateAspectRatio(AspectRatio aspectRatio) {
        mRenderMeasure.setAspectRatio(aspectRatio);
        requestLayout();
    }

    @Override
    public void updateVideoSize(int videoWidth, int videoHeight) {
        mRenderMeasure.setVideoSize(videoWidth, videoHeight);
        fixedSize(videoWidth, videoHeight);
        requestLayout();
    }

    void fixedSize(int videoWidth, int videoHeight){
        if(videoWidth != 0 && videoHeight != 0){
            getHolder().setFixedSize(videoWidth, videoHeight);
        }
    }

    @Override
    public View getRenderView() {
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        PLog.d(TAG,"onSurfaceViewDetachedFromWindow");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        PLog.d(TAG,"onSurfaceViewAttachedToWindow");
    }

    @Override
    public void release() {
        isReleased = true;
    }

    @Override
    public boolean isReleased() {
        return isReleased;
    }

    private static final class InternalRenderHolder implements IRenderHolder{

        private WeakReference<SurfaceHolder> mSurfaceHolder;

        public InternalRenderHolder(SurfaceHolder surfaceHolder){
            this.mSurfaceHolder = new WeakReference<>(surfaceHolder);
        }

        @Override
        public void bindPlayer(IPlayer player) {
            if(player!=null && mSurfaceHolder.get()!=null){
                player.setDisplay(mSurfaceHolder.get());
            }
        }
    }

    private class InternalSurfaceHolderCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            PLog.d(TAG,"<---surfaceCreated---->");
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceCreated(new InternalRenderHolder(holder),0,0);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            PLog.d(TAG,"surfaceChanged : width = " + width + " height = " + height);
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceChanged(new InternalRenderHolder(holder),format, width,height);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            PLog.d(TAG,"***surfaceDestroyed***");
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceDestroy(new InternalRenderHolder(holder));
            }
        }

    }


}
