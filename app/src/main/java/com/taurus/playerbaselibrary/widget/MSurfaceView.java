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

package com.taurus.playerbaselibrary.widget;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Taurus on 2017/10/10.
 */

public class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "MSurfaceView";
    private OnSurfaceListener onSurfaceListener;

    public MSurfaceView(Context context, OnSurfaceListener onSurfaceListener) {
        super(context);
        this.onSurfaceListener = onSurfaceListener;
        getHolder().addCallback(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(onSurfaceListener!=null){
            onSurfaceListener.onSurfaceViewDetachedFromWindow();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(onSurfaceListener!=null){
            onSurfaceListener.onSurfaceViewAttachedToWindow();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"onSurfaceCreated...");
        if(onSurfaceListener!=null){
            onSurfaceListener.onSurfaceCreated(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG,"onSurfaceChanged...");
        if(onSurfaceListener!=null){
            onSurfaceListener.onSurfaceChanged(holder, format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"onSurfaceDestroyed...");
        if(onSurfaceListener!=null){
            onSurfaceListener.onSurfaceDestroyed(holder);
        }
    }

    public interface OnSurfaceListener{
        void onSurfaceCreated(SurfaceHolder holder);
        void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height);
        void onSurfaceDestroyed(SurfaceHolder holder);
        void onSurfaceViewAttachedToWindow();
        void onSurfaceViewDetachedFromWindow();
    }
}
