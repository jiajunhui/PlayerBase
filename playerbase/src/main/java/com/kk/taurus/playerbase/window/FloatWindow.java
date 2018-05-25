package com.kk.taurus.playerbase.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.utils.PUtils;

public class FloatWindow extends FrameLayout {

    private final int MIN_MOVE_DISTANCE = 20;

    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;

    public FloatWindow(Context context, FloatViewParams params) {
        super(context);
        init(params.view, params.x, params.y, params.width, params.height);
    }

    private void init(View childView, int x, int y, int width, int height) {
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.width = width>0?width:WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = height>0?height:WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = x;
        wmParams.y = y;
        if (childView != null) {
            addView(childView);
        }
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

    /**
     * add to WindowManager
     * @return
     */
    public boolean addToWindow() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    wm.addView(this, wmParams);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null) {
                        wm.addView(this, wmParams);
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
    public boolean removeFromWindow() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    wm.removeViewImmediate(this);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        wm.removeViewImmediate(this);
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

    public static class FloatViewParams{

        private View view;
        private int x;
        private int y;
        private int width;
        private int height;

        public View getView() {
            return view;
        }

        public FloatViewParams setView(View view) {
            this.view = view;
            return this;
        }

        public int getX() {
            return x;
        }

        public FloatViewParams setX(int x) {
            this.x = x;
            return this;
        }

        public int getY() {
            return y;
        }

        public FloatViewParams setY(int y) {
            this.y = y;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public FloatViewParams setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public FloatViewParams setHeight(int height) {
            this.height = height;
            return this;
        }
    }


}
