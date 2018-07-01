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

package com.kk.taurus.playerbase.window;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.widget.BaseVideoView;

/**
 * Created by Taurus on 2018/5/25.
 *
 * see also IWindow{@link IWindow}
 *
 */
@SuppressLint("ViewConstructor")
public class WindowVideoView extends BaseVideoView implements IWindow {

    private WindowHelper mWindowHelper;

    private OnWindowListener onWindowListener;

    public WindowVideoView(Context context, FloatWindowParams params) {
        super(context);
        init(context, params);
    }

    private void init(Context context, FloatWindowParams params) {
        mWindowHelper = new WindowHelper(context, this, params);
        mWindowHelper.setOnWindowListener(mInternalWindowListener);
    }

    private OnWindowListener mInternalWindowListener =
            new OnWindowListener() {
        @Override
        public void onShow() {
            if(onWindowListener!=null)
                onWindowListener.onShow();
        }
        @Override
        public void onClose() {
            stop();
            resetStyle();
            if(onWindowListener!=null)
                onWindowListener.onClose();
        }
    };

    @Override
    public void setOnWindowListener(OnWindowListener onWindowListener) {
        this.onWindowListener = onWindowListener;
    }

    @Override
    public void setDragEnable(boolean dragEnable) {
        mWindowHelper.setDragEnable(dragEnable);
    }

    @Override
    public boolean isWindowShow() {
        return mWindowHelper.isWindowShow();
    }

    @Override
    public void updateWindowViewLayout(int x, int y) {
        mWindowHelper.updateWindowViewLayout(x, y);
    }

    /**
     * add to WindowManager
     * @return
     */
    @Override
    public boolean show() {
        return mWindowHelper.show();
    }

    @Override
    public boolean show(Animator... items) {
        return mWindowHelper.show(items);
    }

    /**
     * remove from WindowManager
     *
     * @return
     */
    @Override
    public void close() {
        setElevationShadow(0);
        mWindowHelper.close();
    }

    @Override
    public void close(Animator... items) {
        setElevationShadow(0);
        mWindowHelper.close(items);
    }

    public void resetStyle() {
        setElevationShadow(0);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            clearShapeStyle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mWindowHelper.onInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mWindowHelper.onTouchEvent(event) || super.onTouchEvent(event);
    }

}
