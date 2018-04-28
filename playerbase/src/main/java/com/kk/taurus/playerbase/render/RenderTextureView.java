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
import android.os.Build;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;

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

    private SurfaceTexture mSurfaceTexture;

    private boolean mTakeOverSurfaceTexture;

    public RenderTextureView(Context context) {
        this(context, null);
    }

    public RenderTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderMeasure = new RenderMeasure();
        setSurfaceTextureListener(new InternalSurfaceTextureListener());
    }

    /**
     * If you want to take over the life cycle of SurfaceTexture,
     * please set the tag to true.
     * @param takeOverSurfaceTexture
     */
    public void setTakeOverSurfaceTexture(boolean takeOverSurfaceTexture){
        this.mTakeOverSurfaceTexture = takeOverSurfaceTexture;
    }

    public boolean isTakeOverSurfaceTexture() {
        return mTakeOverSurfaceTexture;
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

    @Override
    public void release() {
        if(mSurfaceTexture!=null){
            setSurfaceTextureListener(null);
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
    }

    public SurfaceTexture getOwnSurfaceTexture(){
        return mSurfaceTexture;
    }

    private static final class InternalRenderHolder implements IRenderHolder{

        private Surface mSurfaceRefer;
        private RenderTextureView mTextureView;

        public InternalRenderHolder(RenderTextureView textureView, SurfaceTexture surfaceTexture){
            this.mTextureView = textureView;
            mSurfaceRefer = new Surface(surfaceTexture);
        }

        @Override
        public void bindPlayer(IPlayer player) {
            if(player!=null && mSurfaceRefer!=null){
                SurfaceTexture surfaceTexture = mTextureView.getOwnSurfaceTexture();
                //When the user sets the takeover flag and SurfaceTexture is available.
                if(mTextureView.isTakeOverSurfaceTexture()
                        && surfaceTexture!=null
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    PLog.d("RenderTextureView","****setSurfaceTexture****");
                    mTextureView.setSurfaceTexture(surfaceTexture);
                }else{
                    PLog.d("RenderTextureView","****bindPlayer****");
                    player.setSurface(mSurfaceRefer);
                }
            }
        }

    }

    private class InternalSurfaceTextureListener implements SurfaceTextureListener{

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            PLog.d(TAG,"<---onSurfaceTextureAvailable---> : width = " + width + " height = " + height);
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceCreated(
                        new InternalRenderHolder(RenderTextureView.this, surface), width, height);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            PLog.d(TAG,"onSurfaceTextureSizeChanged : width = " + width + " height = " + height);
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceChanged(
                        new InternalRenderHolder(RenderTextureView.this,surface), 0, width, height);
            }
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            PLog.d(TAG,"***onSurfaceTextureDestroyed***");
            if(mRenderCallback!=null){
                mRenderCallback.onSurfaceDestroy(
                        new InternalRenderHolder(RenderTextureView.this,surface));
            }
            if(mTakeOverSurfaceTexture)
                mSurfaceTexture = surface;
            return !mTakeOverSurfaceTexture;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }

    }

}
