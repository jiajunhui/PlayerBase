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

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.utils.PUtils;

public class FloatWindow extends FrameLayout {

    private final int MIN_MOVE_DISTANCE = 20;

    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;

    private boolean isWindowShow;

    public FloatWindow(Context context, View windowView, FloatWindowParams params) {
        super(context);
        init(windowView, params);
    }

    private void init(View childView, FloatWindowParams params) {
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = params.getWindowType();
        wmParams.gravity = params.getGravity();
        wmParams.format = params.getFormat();
        wmParams.flags = params.getFlag();
        wmParams.width = params.getWidth();
        wmParams.height = params.getHeight();
        wmParams.x = params.getX();
        wmParams.y = params.getY();
        if (childView != null) {
            addView(childView);
        }
    }

    /**
     * set shadow.
     * @param elevation
     */
    public void setElevationShadow(float elevation) {
        setElevationShadow(Color.BLACK, elevation);
    }

    /**
     * must setting a color when set shadow.
     * @param backgroundColor
     * @param elevation
     */
    public void setElevationShadow(int backgroundColor, float elevation) {
        setBackgroundColor(backgroundColor);
        ViewCompat.setElevation(this, elevation);
    }

    /**
     * update location position
     *
     * @param x
     * @param y
     */
    public void updateFloatViewPosition(int x, int y) {
        wmParams.x = x;
        wmParams.y = y;
        wm.updateViewLayout(this, wmParams);
    }

    public boolean isWindowShow() {
        return isWindowShow;
    }

    /**
     * add to WindowManager
     * @return
     */
    public boolean show() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    wm.addView(this, wmParams);
                    isWindowShow = true;
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null) {
                        wm.addView(this, wmParams);
                        isWindowShow = true;
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }


        } else {
            return false;
        }
    }

    /**
     * remove from WindowManager
     *
     * @return
     */
    public boolean close() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    wm.removeViewImmediate(this);
                    isWindowShow = false;
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        wm.removeViewImmediate(this);
                        isWindowShow = false;
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }


        } else {
            return false;
        }

    }

    private float mDownX;
    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(ev.getRawX() - mDownX) > MIN_MOVE_DISTANCE
                        || Math.abs(ev.getRawY() - mDownY) > MIN_MOVE_DISTANCE){
                    return true;
                }
                return super.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private int floatX;
    private int floatY;
    private boolean firstTouch = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstTouch) {
                    floatX = (int) event.getX();
                    floatY = (int) (event.getY() + PUtils.getStatusBarHeight(getContext()));
                    firstTouch = false;
                }
                wmParams.x = X - floatX;
                wmParams.y = Y - floatY;
                wm.updateViewLayout(this, wmParams);
                break;
        }
        return super.onTouchEvent(event);
    }

}
