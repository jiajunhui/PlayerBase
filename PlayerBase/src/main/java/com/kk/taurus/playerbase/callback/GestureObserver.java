package com.kk.taurus.playerbase.callback;

import android.view.MotionEvent;

/**
 * Created by Taurus on 2017/3/27.
 */

public interface GestureObserver {
    void onGestureSingleTab(MotionEvent event);
    void onGestureDoubleTab(MotionEvent event);
    void onGestureScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    void onGestureHorizontalSlide(float percent);
    void onGestureRightVerticalSlide(float percent);
    void onGestureLeftVerticalSlide(float percent);

    void onGestureEnableChange(boolean enable);
    void onGestureEnd();
}
