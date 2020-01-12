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

package com.kk.taurus.playerbase.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.log.PLog;

/**
 * Created by Taurus on 2017/11/20.
 */

public class BaseGestureCallbackHandler extends GestureDetector.SimpleOnGestureListener {

    private final String TAG = "GestureCallbackHandler";
    protected OnTouchGestureListener mOnTouchGestureListener;

    private boolean mGestureEnable = true;
    private boolean mGestureScrollEnable = true;

    public BaseGestureCallbackHandler(OnTouchGestureListener onTouchGestureListener){
        this.mOnTouchGestureListener = onTouchGestureListener;
    }

    public void setGestureEnable(boolean enable){
        this.mGestureEnable = enable;
    }

    public void setGestureScrollEnable(boolean gestureScrollEnable) {
        this.mGestureScrollEnable = gestureScrollEnable;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mOnTouchGestureListener !=null){
            mOnTouchGestureListener.onSingleTapUp(e);
        }
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(mOnTouchGestureListener !=null){
            mOnTouchGestureListener.onDoubleTap(e);
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        PLog.d(TAG,"onDown...");
        if(mOnTouchGestureListener !=null){
            mOnTouchGestureListener.onDown(e);
        }
        return mGestureEnable;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mOnTouchGestureListener != null && mGestureScrollEnable){
            mOnTouchGestureListener.onScroll(e1, e2, distanceX, distanceY);
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    public void onEndGesture(MotionEvent event){
        if(mOnTouchGestureListener !=null){
            mOnTouchGestureListener.onEndGesture();
        }
    }
}
