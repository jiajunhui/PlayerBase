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

import android.view.MotionEvent;

/**
 * Created by Taurus on 2017/3/26.
 */

public interface OnTouchGestureListener {
    /**
     * on gesture single tap up
     * @param event
     */
    void onSingleTapUp(MotionEvent event);

    /**
     * on gesture long press
     * @param event
     */
    void onLongPress(MotionEvent event);

    /**
     * on gesture double tap
     * @param event
     */
    void onDoubleTap(MotionEvent event);

    void onDown(MotionEvent event);

    /**
     * on scroll
     * @param e1
     * @param e2
     * @param distanceX
     * @param distanceY
     */
    void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);

    void onEndGesture();
}
