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

package com.kk.taurus.playerbase.setting;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.GestureObserverHandler;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.callback.PlayerObserverHandler;
import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;
import com.kk.taurus.playerbase.inter.IBindPlayer;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;
import com.kk.taurus.playerbase.inter.ITimerGetter;
import com.kk.taurus.playerbase.utils.EventLog;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.util.List;

/**
 * Created by Taurus on 2017/11/22.
 */

public class EventDistributionHandler implements PlayerObserver, IBindPlayer, OnPlayerGestureListener {

    private BaseReceiverCollections mReceiverCollections;

    /**
     * 播放器事件观察者对cover的分发管理
     */
    private PlayerObserverHandler mPlayerObserverHandler;

    /**
     * 手势事件观察者对cover的分发管理
     */
    private GestureObserverHandler mGestureObserverHandler;

    public EventDistributionHandler(){

    }

    public void bindEventReceiverCollections(BaseReceiverCollections receiverCollections){
        this.mReceiverCollections = receiverCollections;
        mPlayerObserverHandler = new PlayerObserverHandler(receiverCollections);
        mGestureObserverHandler = new GestureObserverHandler(receiverCollections);
    }

    public void onDistributionPlayerEvent(int eventCode, Bundle bundle){
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_ON_PLAYER_TIMER_UPDATE:
                int curr = bundle.getInt(ITimerGetter.KEY_TIMER_CURRENT_POSITION);
                int duration = bundle.getInt(ITimerGetter.KEY_TIMER_DURATION);
                int bufferPos = bundle.getInt(ITimerGetter.KEY_TIMER_BUFFER_POSITION);
                onNotifyPlayTimerCounter(curr,duration,bufferPos);
                break;
            case OnPlayerEventListener.EVENT_CODE_ON_NETWORK_ERROR:
                onNotifyNetWorkError();
                break;
            case OnPlayerEventListener.EVENT_CODE_ON_NETWORK_CHANGE:
                onNotifyNetWorkChanged(bundle.getInt(OnPlayerEventListener.BUNDLE_KEY_INT_DATA));
                break;
            case OnPlayerEventListener.EVENT_CODE_ON_NETWORK_CONNECTED:
                onNotifyNetWorkConnected(bundle.getInt(OnPlayerEventListener.BUNDLE_KEY_INT_DATA));
                break;
            default:
                onNotifyPlayEvent(eventCode, bundle);
                break;
        }
    }

    public void onDistributionErrorEvent(int eventCode, Bundle bundle){
        onNotifyErrorEvent(eventCode, bundle);
    }

    public void doConfigChange(Configuration newConfig) {
        onNotifyConfigurationChanged(newConfig);
    }

    /**
     * 当cover集合中存在Dpad控制层时，将焦点控制权交给它。
     */
    public void dPadRequestFocus(){
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null && receiver instanceof IDpadFocusCover){
                    receiver.onNotifyPlayEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS, null);
                }
            }
    }

    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onBindPlayer(player,onCoverEventListener);
                }
            }
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyConfigurationChanged(newConfig);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        EventLog.onNotifyPlayerEvent(eventCode, bundle);
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyPlayEvent(eventCode, bundle);
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyErrorEvent(eventCode, bundle);
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        EventLog.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        EventLog.onNotifyNetWorkConnected();
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyNetWorkConnected(networkType);
    }

    @Override
    public void onNotifyNetWorkChanged(int networkType) {
        EventLog.onNotifyNetWorkChanged();
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyNetWorkChanged(networkType);
    }

    @Override
    public void onNotifyNetWorkError() {
        EventLog.onNotifyNetWorkError();
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyNetWorkError();
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyAdPrepared(adVideos);
    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyAdStart(adVideo);
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyAdFinish(data, isAllFinish);
    }

    //-------------------------------OnPlayerGestureListener-----------------------------

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        if(mGestureObserverHandler!=null)
            return mGestureObserverHandler.onGestureSingleTab(event);
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        if(mGestureObserverHandler!=null)
            return mGestureObserverHandler.onGestureDoubleTab(event);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        if(mGestureObserverHandler!=null)
            return mGestureObserverHandler.onGestureDown(event);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mGestureObserverHandler!=null)
            return mGestureObserverHandler.onGestureScroll(e1, e2, distanceX, distanceY);
        return false;
    }

    @Override
    public void onHorizontalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureHorizontalSlide(percent,e1, e2, distanceX, distanceY);
    }

    @Override
    public void onRightVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureRightVerticalSlide(percent,e1, e2, distanceX, distanceY);
    }

    @Override
    public void onLeftVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureLeftVerticalSlide(percent,e1, e2, distanceX, distanceY);
    }

    @Override
    public void onEndGesture() {
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureEnd();
    }

    public void onPlayerGestureEnableChange(boolean enable) {
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureEnableChange(enable);
    }
}
