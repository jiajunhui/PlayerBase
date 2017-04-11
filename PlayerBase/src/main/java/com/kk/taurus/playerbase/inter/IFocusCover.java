package com.kk.taurus.playerbase.inter;

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by Taurus on 2017/3/31.
 */

public interface IFocusCover {

    void setFocusable(boolean focusable);
    void requestFocus();

    boolean onKeyActionDownDpadLeft(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadUp(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadRight(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadDown(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadEnter(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadBack(View v, int keyCode, KeyEvent event);
    boolean onKeyActionDownDpadMenu(View v, int keyCode, KeyEvent event);

    boolean onKeyActionUpDpadLeft(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadUp(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadRight(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadDown(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadEnter(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadBack(View v, int keyCode, KeyEvent event);
    boolean onKeyActionUpDpadMenu(View v, int keyCode, KeyEvent event);
    
}
