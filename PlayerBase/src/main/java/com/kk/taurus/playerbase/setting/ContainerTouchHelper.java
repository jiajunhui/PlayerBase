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

package com.kk.taurus.playerbase.setting;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.BaseGestureCallbackHandler;
import com.kk.taurus.playerbase.inter.IGestureHelper;

/**
 * Created by Taurus on 2017/11/20.
 */

public class ContainerTouchHelper implements IGestureHelper{

    private GestureDetector mGestureDetector;
    private BaseGestureCallbackHandler mGestureCallback;
    private boolean mGestureEnable = true;

    public ContainerTouchHelper(Context context, BaseGestureCallbackHandler gestureCallback){
        this.mGestureCallback = gestureCallback;
        mGestureDetector = new GestureDetector(context,gestureCallback);
    }

    public boolean onTouch(MotionEvent event){
        if(!mGestureEnable){
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                onEndGesture(event);
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh){
        mGestureCallback.onLayoutSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setGestureEnable(boolean enable) {
        this.mGestureEnable = enable;
    }

    @Override
    public void onEndGesture(MotionEvent event) {
        mGestureCallback.onEndGesture(event);
    }
}
