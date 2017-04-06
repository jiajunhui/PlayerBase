package com.taurus.playerbaselibrary.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.kk.taurus.playerbase.cover.base.BaseFocusCover;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;
import com.taurus.playerbaselibrary.R;

/**
 * Created by Taurus on 2017/4/5.
 */

public class TestKeyActionDownMenuCover extends BaseFocusCover {

    public TestKeyActionDownMenuCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        RelativeLayout focusView = findViewById(R.id.rl_focus_view);
        bindFocusListener(focusView);
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        super.onCoverEvent(eventCode, bundle);
        switch (eventCode){
            case IDpadFocusCover.EVENT_CODE_ACTION_DOWN_MENU:
                requestFocus();
                setCoverState(true);
                break;
        }
    }

    @Override
    public boolean onKeyActionDownDpadBack(View v, int keyCode, KeyEvent event) {
        setCoverState(false);
        releaseFocusToDpadCover();
        return super.onKeyActionDownDpadBack(v, keyCode, event);
    }

    public void setCoverState(boolean state){
        setCoverVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_test_onkey_down_menu_cover,null);
    }
}
