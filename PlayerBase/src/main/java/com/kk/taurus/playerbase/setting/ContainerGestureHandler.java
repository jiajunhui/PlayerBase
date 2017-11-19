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

import android.view.MotionEvent;
import android.view.View;

import com.kk.taurus.playerbase.callback.GestureObserver;

/**
 * Created by mtime on 2017/10/18.
 */

public class ContainerGestureHandler implements View.OnTouchListener {

    private GestureObserver mObserver;
    private ParamsBean mParams;

    private float mDownX, mDownY;

    public ContainerGestureHandler(View view, GestureObserver gestureObserver, ParamsBean paramsBean){
        view.setOnTouchListener(this);
        this.mObserver = gestureObserver;
        this.mParams = paramsBean;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                v.getParent().requestDisallowInterceptTouchEvent(true);
                float deltaX = x - mDownX;
                float deltaY = y - mDownY;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);

                break;

            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }

    public static class ParamsBean{
        public int width;
        public int height;
    }

}
