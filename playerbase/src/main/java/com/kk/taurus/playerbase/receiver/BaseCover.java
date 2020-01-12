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

package com.kk.taurus.playerbase.receiver;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IntRange;
import android.view.View;

import com.kk.taurus.playerbase.assist.InterEvent;

/**
 * Created by Taurus on 2018/3/17.
 */

public abstract class BaseCover extends BaseReceiver implements
        ICover, ICoverHandle, View.OnAttachStateChangeListener {

    private View mCoverView;

    public BaseCover(Context context) {
        super(context);
        mCoverView = onCreateCoverView(context);
        mCoverView.addOnAttachStateChangeListener(this);
    }

    @Override
    public final void requestPause(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_PAUSE, bundle);
    }

    @Override
    public final void requestResume(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_RESUME, bundle);
    }

    @Override
    public final void requestSeek(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_SEEK, bundle);
    }

    @Override
    public final void requestStop(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_STOP, bundle);
    }

    @Override
    public final void requestReset(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_RESET, bundle);
    }

    @Override
    public final void requestRetry(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_RETRY, bundle);
    }

    @Override
    public final void requestReplay(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_REPLAY, bundle);
    }

    @Override
    public final void requestPlayDataSource(Bundle bundle) {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_PLAY_DATA_SOURCE, bundle);
    }

    @Override
    public final void requestNotifyTimer() {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_NOTIFY_TIMER, null);
    }

    @Override
    public final void requestStopTimer() {
        notifyReceiverEvent(InterEvent.CODE_REQUEST_STOP_TIMER, null);
    }

    protected final <T extends View> T findViewById(int id){
        return mCoverView.findViewById(id);
    }

    @Override
    public final void setCoverVisibility(int visibility){
        mCoverView.setVisibility(visibility);
    }

    @Override
    public final View getView(){
        return mCoverView;
    }

    @Override
    public final void onViewAttachedToWindow(View v) {
        onCoverAttachedToWindow();
    }

    @Override
    public final void onViewDetachedFromWindow(View v) {
        onCoverDetachedToWindow();
    }

    /**
     * when cover view add to window.
     */
    protected void onCoverAttachedToWindow(){

    }

    /**
     * when cover view removed from window.
     */
    protected void onCoverDetachedToWindow(){

    }

    protected abstract View onCreateCoverView(Context context);

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_LOW;
    }

    /**
     * setting the priority in COVER_LEVEL_LOW.
     * The high priority cover will be placed above,
     * otherwise the lower priority will be placed below.
     *
     * @param priority range from 0-31
     * @return
     */
    protected final int levelLow(@IntRange(from = 0, to = 31)int priority){
        return levelPriority(ICover.COVER_LEVEL_LOW, priority);
    }

    /**
     * setting the priority in COVER_LEVEL_MEDIUM.
     * The high priority cover will be placed above,
     * otherwise the lower priority will be placed below.
     *
     * @param priority range from 0-31
     * @return
     */
    protected final int levelMedium(@IntRange(from = 0, to = 31)int priority){
        return levelPriority(ICover.COVER_LEVEL_MEDIUM, priority);
    }

    /**
     * setting the priority in COVER_LEVEL_HIGH.
     * The high priority cover will be placed above,
     * otherwise the lower priority will be placed below.
     *
     * @param priority range from 0-31
     * @return
     */
    protected final int levelHigh(@IntRange(from = 0, to = 31)int priority){
        return levelPriority(ICover.COVER_LEVEL_HIGH, priority);
    }

    private int levelPriority(int level, int priority){
        return level + (priority%LEVEL_MAX);
    }
}
