package com.kk.taurus.playerbase.touch;

import android.view.KeyEvent;

public class ContainerKeyEventHelper {
    private OnKeyEventListener mOnKeyEventListener;

    private Boolean mKeyEnable = true;

    public ContainerKeyEventHelper(OnKeyEventListener onKeyEventListener){
        this.mOnKeyEventListener = onKeyEventListener;
    }

    public void setKeyEventEnable(boolean enable) {
        mKeyEnable = enable;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mKeyEnable && mOnKeyEventListener != null) {
            mOnKeyEventListener.onKeyDownInCover(keyCode, event);
        }
        return mKeyEnable;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mKeyEnable && mOnKeyEventListener != null) {
            mOnKeyEventListener.onKeyUpInCover(keyCode, event);
        }
        return mKeyEnable;
    }
}
