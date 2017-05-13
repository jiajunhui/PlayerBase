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

package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.kk.taurus.playerbase.cover.base.BaseFocusCover;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;

/**
 * Created by Taurus on 2017/4/5.
 * dpad监听分发控制层，获取焦点后，监听按键并发送按键消息。
 */

public class DefaultDpadFocusCover extends BaseFocusCover implements IDpadFocusCover{

    public DefaultDpadFocusCover(Context context) {
        super(context);
    }

    public DefaultDpadFocusCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        bindFocusListener(mFocusView);
    }

    @Override
    protected void setDefaultGone() {

    }

    @Override
    public View initCoverLayout(Context context) {
        mFocusView = new FrameLayout(context);
        return mFocusView;
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        /**
         * 当收到播放器获取焦点的请求后，dpad监听层获取焦点。
         */
        if(eventCode== OnPlayerEventListener.EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS){
            setFocusable(true);
            requestFocus();
        }
    }

    protected boolean isInterruptSendEvent(int eventCode, Bundle bundle){
        return false;
    }

    @Override
    protected void notifyCoverEvent(int eventCode, Bundle bundle) {
        if(isInterruptSendEvent(eventCode, bundle)){
            return;
        }
        super.notifyCoverEvent(eventCode, bundle);
    }

    @Override
    public boolean onKeyActionDownDpadBack(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_BACK,null);
        return super.onKeyActionDownDpadBack(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionDownDpadEnter(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_ENTER,null);
        return super.onKeyActionDownDpadEnter(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionDownDpadLeft(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_LEFT,null);
        return super.onKeyActionDownDpadLeft(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionDownDpadUp(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_UP,null);
        return super.onKeyActionDownDpadUp(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionDownDpadRight(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_RIGHT,null);
        return super.onKeyActionDownDpadRight(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionDownDpadDown(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_DOWN,null);
        return super.onKeyActionUpDpadDown(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionDownDpadMenu(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_DOWN_MENU,null);
        return super.onKeyActionDownDpadMenu(v, keyCode, event);
    }


    //---------------------------------Action Up---------------------------------

    @Override
    public boolean onKeyActionUpDpadBack(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_BACK,null);
        return super.onKeyActionDownDpadBack(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionUpDpadEnter(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_ENTER,null);
        return super.onKeyActionDownDpadEnter(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionUpDpadLeft(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_LEFT,null);
        return super.onKeyActionDownDpadLeft(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionUpDpadUp(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_UP,null);
        return super.onKeyActionDownDpadUp(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionUpDpadRight(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_RIGHT,null);
        return super.onKeyActionDownDpadRight(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionUpDpadDown(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_DOWN,null);
        return super.onKeyActionUpDpadDown(v, keyCode, event);
    }

    @Override
    public boolean onKeyActionUpDpadMenu(View v, int keyCode, KeyEvent event) {
        notifyCoverEvent(IDpadFocusCover.EVENT_CODE_ACTION_UP_MENU,null);
        return super.onKeyActionDownDpadMenu(v, keyCode, event);
    }

}
