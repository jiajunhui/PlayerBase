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
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.kk.taurus.playerbase.utils.PUtils;

/**
 * Created by Taurus on 2018/5/27.
 */
public class WindowHelper implements IWindow {

    private View mWindowView;

    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;

    private boolean isWindowShow;

    private boolean mDragEnable = true;

    private AnimatorSet mShowAnimatorSet;
    private AnimatorSet mCloseAnimatorSet;
    private boolean defaultAnimation;

    private OnWindowListener mOnWindowListener;

    public WindowHelper(Context context, View windowView, FloatWindowParams params){
        this.mWindowView = windowView;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = params.getWindowType();
        wmParams.gravity = params.getGravity();
        wmParams.format = params.getFormat();
        wmParams.flags = params.getFlag();
        wmParams.width = params.getWidth();
        wmParams.height = params.getHeight();
        wmParams.x = params.getX();
        wmParams.y = params.getY();
        defaultAnimation = params.isDefaultAnimation();
    }

    @Override
    public void setOnWindowListener(OnWindowListener onWindowListener) {
        this.mOnWindowListener = onWindowListener;
    }

    @Override
    public void setDragEnable(boolean dragEnable) {
        this.mDragEnable = dragEnable;
    }

    @Override
    public void updateWindowViewLayout(int x, int y) {
        wmParams.x = x;
        wmParams.y = y;
        wm.updateViewLayout(mWindowView, wmParams);
    }

    @Override
    public boolean isWindowShow() {
        return isWindowShow;
    }

    /**
     * add to WindowManager
     * @return
     */
    @Override
    public boolean show() {
        return show(defaultAnimation?
                getDefaultAnimators(true):
                null);
    }

    @Override
    public boolean show(Animator... items) {
        boolean addToWindow = addToWindow();
        if(!addToWindow)
            return false;
        ViewParent parent = mWindowView.getParent();
        if(parent!=null){
            parent.requestLayout();
        }
        if(items!=null && items.length>0){
            cancelCloseAnimation();
            cancelShowAnimation();
            mShowAnimatorSet = new AnimatorSet();
            mShowAnimatorSet.playTogether(items);
            mShowAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mShowAnimatorSet.removeAllListeners();
                }
            });
            mShowAnimatorSet.start();
        }
        if(mOnWindowListener!=null){
            mOnWindowListener.onShow();
        }
        return true;
    }

    private void cancelShowAnimation() {
        if(mShowAnimatorSet!=null){
            mShowAnimatorSet.cancel();
            mShowAnimatorSet.removeAllListeners();
        }
    }

    private boolean addToWindow(){
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!mWindowView.isAttachedToWindow()) {
                    wm.addView(mWindowView, wmParams);
                    isWindowShow = true;
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (mWindowView.getParent() == null) {
                        wm.addView(mWindowView, wmParams);
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

    private Animator[] getDefaultAnimators(boolean showAnimators){
        final float startV = showAnimators?0:1;
        final float endV = showAnimators?1:0;
        return new Animator[]{
                ObjectAnimator.ofFloat(mWindowView, "scaleX", startV, endV).setDuration(DURATION_ANIMATION),
                ObjectAnimator.ofFloat(mWindowView, "scaleY", startV, endV).setDuration(DURATION_ANIMATION),
                ObjectAnimator.ofFloat(mWindowView, "alpha", startV, endV).setDuration(DURATION_ANIMATION)
        };
    }

    /**
     * remove from WindowManager
     *
     * @return
     */
    @Override
    public void close() {
        close(defaultAnimation?
                getDefaultAnimators(false):
                null);
    }

    @Override
    public void close(Animator... items) {
        if(items!=null && items.length>0){
            cancelShowAnimation();
            cancelCloseAnimation();
            mCloseAnimatorSet = new AnimatorSet();
            mCloseAnimatorSet.playTogether(items);
            mCloseAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCloseAnimatorSet.removeAllListeners();
                    removeFromWindow();
                }
            });
            mCloseAnimatorSet.start();
        }else{
            removeFromWindow();
        }
    }

    private void cancelCloseAnimation() {
        if(mCloseAnimatorSet!=null){
            mCloseAnimatorSet.cancel();
            mCloseAnimatorSet.removeAllListeners();
        }
    }

    private boolean removeFromWindow(){
        boolean isClose = false;
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (mWindowView.isAttachedToWindow()) {
                    wm.removeViewImmediate(mWindowView);
                    isWindowShow = false;
                    isClose = true;
                }
            } else {
                try {
                    if (mWindowView.getParent() != null) {
                        wm.removeViewImmediate(mWindowView);
                        isWindowShow = false;
                        isClose = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(isClose && mOnWindowListener!=null){
            mOnWindowListener.onClose();
        }
        return isClose;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!mDragEnable)
            return false;
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
                return false;
        }
        return false;
    }

    private float mDownX;
    private float mDownY;

    private int floatX;
    private int floatY;
    private boolean firstTouch = true;

    private int wX, wY;

    public boolean onTouchEvent(MotionEvent event) {
        if(!mDragEnable)
            return false;
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (firstTouch) {
                    floatX = (int) event.getX();
                    floatY = (int) (event.getY() + PUtils.getStatusBarHeight(mWindowView.getContext()));
                    firstTouch = false;
                }
                wX = X - floatX;
                wY = Y - floatY;
                updateWindowViewLayout(wX, wY);
                break;
        }
        return false;
    }

}
