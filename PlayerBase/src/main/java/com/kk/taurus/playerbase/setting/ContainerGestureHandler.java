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
