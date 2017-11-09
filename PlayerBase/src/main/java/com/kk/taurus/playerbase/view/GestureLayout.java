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
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.setting.PlayerGestureDetectorListener;

/**
 * Created by Taurus on 2017/10/15.
 */

public class GestureLayout extends FrameLayout implements View.OnTouchListener {

    private GestureDetector mGestureDetector;
    private int mGestureW, mGestureH;
    private PlayerGestureDetectorListener mPlayerGestureDetectorListener;

    public GestureLayout(Context context){
        super(context);
        init(context);
    }

    public GestureLayout(@NonNull Context context, int gestureWidth, int gestureHeight) {
        super(context);
        mGestureW = gestureWidth;
        mGestureH = gestureHeight;
        init(context);
    }

    private void init(Context context) {
        mPlayerGestureDetectorListener = new PlayerGestureDetectorListener(mGestureW,mGestureH);
        mGestureDetector = new GestureDetector(getContext(), mPlayerGestureDetectorListener);
        setBackgroundColor(Color.TRANSPARENT);
        setClickable(true);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGestureW = w;
        mGestureH = h;
        mPlayerGestureDetectorListener.updateWH(w, h);
    }

    public void updateWH(int width, int height){
        mGestureW  = width;
        mGestureH = height;
        mPlayerGestureDetectorListener.updateWH(width, height);
    }

    public void setPlayerGestureListener(OnPlayerGestureListener onPlayerGestureListener){
        mPlayerGestureDetectorListener.setOnPlayerGestureListener(onPlayerGestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                onEndGesture();
                break;
        }
        return false;
    }

    private void onEndGesture() {
        mPlayerGestureDetectorListener.onEndGesture();
    }
}
