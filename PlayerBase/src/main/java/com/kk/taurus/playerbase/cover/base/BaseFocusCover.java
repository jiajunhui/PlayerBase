package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.inter.IFocusCover;

/**
 * Created by Taurus on 2017/3/31.
 */

public abstract class BaseFocusCover extends BaseCover implements IFocusCover,View.OnKeyListener {

    protected FrameLayout mFocusView;

    public BaseFocusCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        mFocusView.setOnKeyListener(this);
    }

    @Override
    public View initCoverLayout(Context context) {
        mFocusView = new FrameLayout(context);
        mFocusView.setFocusable(true);
        return mFocusView;
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

    private boolean handleKeyActionDown(View v, int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyActionDownDpadUp(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyActionDownDpadDown(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyActionDownDpadLeft(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyActionDownDpadRight(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                return onKeyActionDownDpadEnter(v, keyCode, event);

            case KeyEvent.KEYCODE_BACK:
                return onKeyActionDownDpadBack(v, keyCode, event);

            case KeyEvent.KEYCODE_MENU:
                return onKeyActionDownDpadMenu(v, keyCode, event);
        }
        return false;
    }

    private boolean handleKeyActionUp(View v, int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_DPAD_UP:
                return onKeyActionUpDpadUp(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_DOWN:
                return onKeyActionUpDpadDown(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_LEFT:
                return onKeyActionUpDpadLeft(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                return onKeyActionUpDpadRight(v, keyCode, event);

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                return onKeyActionUpDpadEnter(v, keyCode, event);

            case KeyEvent.KEYCODE_BACK:
                return onKeyActionUpDpadBack(v, keyCode, event);

            case KeyEvent.KEYCODE_MENU:
                return onKeyActionUpDpadMenu(v, keyCode, event);
        }
        return false;
    }

    @Override
    public void requestFocus() {
        mFocusView.requestFocus();
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
