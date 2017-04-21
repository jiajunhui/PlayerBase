package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.kk.taurus.playerbase.inter.IFocusCover;

/**
 * Created by Taurus on 2017/3/31.
 * 获取焦点后，监听key事件。
 */

public abstract class BaseFocusCover extends BaseCoverPlayerHandle implements IFocusCover,View.OnKeyListener, View.OnFocusChangeListener {

    public static final String KEY = "focus_cover";
    private final String TAG = "focus_cover";
    protected View mFocusView;

    public BaseFocusCover(Context context){
        super(context);
    }

    public BaseFocusCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG,"onFocusChange : " + hasFocus);
    }

    protected void bindFocusListener(View focusView){
        mFocusView = focusView;
        setFocusable(true);
        mFocusView.setOnKeyListener(this);
        mFocusView.setOnFocusChangeListener(this);
    }

    @Override
    public void setFocusable(boolean focusable) {
        mFocusView.setFocusable(focusable);
    }

    @Override
    public void requestFocus() {
        mFocusView.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (event.getAction()){
            case KeyEvent.ACTION_DOWN:
                return handleKeyActionDown(v, keyCode, event);

            case KeyEvent.ACTION_UP:
                return handleKeyActionUp(v, keyCode, event);
        }
        return false;
    }

    protected boolean isInterruptOnKeyActionDown(View v, int keyCode, KeyEvent event){
        return false;
    }

    private boolean handleKeyActionDown(View v, int keyCode, KeyEvent event) {
        if(isInterruptOnKeyActionDown(v, keyCode, event)){
            return true;
        }
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.d(TAG,"onKeyActionDown : dpad up");
                return onKeyActionDownDpadUp(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.d(TAG,"onKeyActionDown : dpad down");
                return onKeyActionDownDpadDown(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.d(TAG,"onKeyActionDown : dpad left");
                return onKeyActionDownDpadLeft(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.d(TAG,"onKeyActionDown : dpad right");
                return onKeyActionDownDpadRight(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                Log.d(TAG,"onKeyActionDown : dpad enter");
                return onKeyActionDownDpadEnter(v, keyCode, event);

            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG,"onKeyActionDown : dpad back");
                return onKeyActionDownDpadBack(v, keyCode, event);

            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG,"onKeyActionDown : dpad menu");
                return onKeyActionDownDpadMenu(v, keyCode, event);
        }
        return false;
    }

    protected boolean isInterruptOnKeyActionUp(View v, int keyCode, KeyEvent event){
        return false;
    }

    private boolean handleKeyActionUp(View v, int keyCode, KeyEvent event) {
        if(isInterruptOnKeyActionUp(v, keyCode, event)){
            return true;
        }
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                Log.d(TAG,"onKeyActionUp : dpad up");
                return onKeyActionUpDpadUp(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_DOWN:
                Log.d(TAG,"onKeyActionUp : dpad down");
                return onKeyActionUpDpadDown(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_LEFT:
                Log.d(TAG,"onKeyActionUp : dpad left");
                return onKeyActionUpDpadLeft(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.d(TAG,"onKeyActionUp : dpad right");
                return onKeyActionUpDpadRight(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                Log.d(TAG,"onKeyActionUp : dpad enter");
                return onKeyActionUpDpadEnter(v, keyCode, event);

            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG,"onKeyActionUp : dpad back");
                return onKeyActionUpDpadBack(v, keyCode, event);

            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG,"onKeyActionUp : dpad menu");
                return onKeyActionUpDpadMenu(v, keyCode, event);
        }
        return false;
    }

    @Override
    public boolean onKeyActionDownDpadLeft(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionDownDpadUp(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionDownDpadRight(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionDownDpadDown(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionDownDpadEnter(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionDownDpadBack(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionDownDpadMenu(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadLeft(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadUp(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadRight(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadDown(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadEnter(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadBack(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    @Override
    public boolean onKeyActionUpDpadMenu(View v, int keyCode, KeyEvent event) {
        return defaultReturn();
    }

    protected boolean defaultReturn(){
        return true;
    }

}
