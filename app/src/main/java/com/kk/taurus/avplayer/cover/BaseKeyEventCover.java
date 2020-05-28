package com.kk.taurus.avplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.touch.OnKeyEventListener;

public abstract class BaseKeyEventCover extends BaseCover implements OnKeyEventListener {
    public BaseKeyEventCover(Context context) {
        super(context);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:

                break;
        }
    }

    @Override
    public void onKeyDownInCover(int keyCode, KeyEvent event) {
        dispatchKeyEventInner(true, event);
    }

    @Override
    public void onKeyUpInCover(int keyCode, KeyEvent event) {
        dispatchKeyEventInner(false, event);
    }

    private void dispatchKeyEventInner(boolean down, KeyEvent event){
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            onBackKeyEvent(down, event);
        }
        if (!isActiveState()) {
            return;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MENU:
                onMenuKeyEvent(down, event);
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                onCenterKeyEvent(down, event);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                onUpKeyEvent(down, event);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                onDownKeyEvent(down, event);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                onLeftKeyEvent(down, event);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                onRightKeyEvent(down, event);
                break;
            default:
                break;
        }
    }

    protected void onCenterKeyEvent(boolean down, KeyEvent event){

    }

    protected void onUpKeyEvent(boolean down, KeyEvent event){

    }

    protected void onDownKeyEvent(boolean down, KeyEvent event){

    }

    protected void onLeftKeyEvent(boolean down, KeyEvent event){

    }

    protected void onRightKeyEvent(boolean down, KeyEvent event){

    }

    protected void onBackKeyEvent(boolean down, KeyEvent event){

    }

    protected void onMenuKeyEvent(boolean down, KeyEvent event){

    }

    protected boolean isActiveState(){
        int state = getPlayerStateGetter().getState();
        return state  >= IPlayer.STATE_PREPARED && state  <= IPlayer.STATE_PAUSED;
    }
}
