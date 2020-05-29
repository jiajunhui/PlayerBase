package com.kk.taurus.playerbase.touch;

import android.view.KeyEvent;

public interface OnKeyEventListener {
    /**
     * press any key
     *
     * @param keyCode A key code that represents the button pressed, from
     *                {@link KeyEvent}.
     * @param event   The KeyEvent object that defines the button action.
     */
    void onKeyDownInCover(int keyCode, KeyEvent event);

    /**
     * up any key
     * @param keyCode A key code that represents the button pressed, from
     *                {@link KeyEvent}.
     * @param event   The KeyEvent object that defines the button action.
     */
    void onKeyUpInCover(int keyCode, KeyEvent event);
}
