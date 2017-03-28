package com.kk.taurus.playerbase.callback;

import android.view.MotionEvent;

/**
 * Created by Taurus on 2017/3/26.
 */

public interface OnPlayerGestureListener {
    void onSingleTapUp(MotionEvent event);
    void onDoubleTap(MotionEvent event);
    void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    void onHorizontalSlide(float percent);
    void onRightVerticalSlide(float percent);
    void onLeftVerticalSlide(float percent);
}
