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

package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.GestureObserverHandler;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.callback.PlayerObserverHandler;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;
import com.kk.taurus.playerbase.inter.IEventReceiver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.utils.EventLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 *
 * 绑定receiver集合，负责分发和中转player或者cover消息。
 *
 */

public abstract class BaseBindEventReceiver extends BaseContainer implements IEventReceiver, PlayerObserver,OnCoverEventListener{

    private BaseReceiverCollections receiverCollections;
    private List<OnCoverEventListener> mCoverEventListenerList = new ArrayList<>();

    /**
     * 播放器事件观察者对cover的分发管理
     */
    private PlayerObserverHandler mPlayerObserverHandler;

    /**
     * 手势事件观察者对cover的分发管理
     */
    private GestureObserverHandler mGestureObserverHandler;

    public BaseBindEventReceiver(@NonNull Context context){
        super(context);
    }

    public BaseBindEventReceiver(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseBindEventReceiver(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindCoverCollections(BaseReceiverCollections coverCollections){
        if(this.receiverCollections !=null)
            return;
        this.receiverCollections = coverCollections;
        mPlayerObserverHandler = new PlayerObserverHandler(coverCollections);
        mGestureObserverHandler = new GestureObserverHandler(coverCollections);
        initCovers(mAppContext);
    }

    public void unbindCoverCollections(){
        if(receiverCollections !=null){
            receiverCollections.clear();
            receiverCollections = null;
        }
        removeAllCovers();
        removeAllContainers();
    }

    private void initCovers(Context context) {
        if(receiverCollections ==null)
            return;
        List<BaseEventReceiver> covers = receiverCollections.getReceivers();
        for(BaseEventReceiver cover : covers){
            if(cover instanceof BaseCover){
                addCover((BaseCover) cover);
            }
        }
        onCoversHasInit(context);
    }

    public BaseReceiverCollections getReceiverCollections(){
        return receiverCollections;
    }

    protected void onCoversHasInit(Context context) {

    }

    public void setOnCoverEventListener(OnCoverEventListener onCoverEventListener){
        this.mCoverEventListenerList.add(onCoverEventListener);
    }

    public void removeCoverEventListener(OnCoverEventListener onCoverEventListener){
        this.mCoverEventListenerList.remove(onCoverEventListener);
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        callBackCoverEventListener(eventCode, bundle);
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onCoverEvent(eventCode, bundle);
                }
            }
    }

    private void callBackCoverEventListener(int eventCode, Bundle bundle){
        if(mCoverEventListenerList==null)
            return;
        Iterator<OnCoverEventListener> iterator = mCoverEventListenerList.iterator();
        while (iterator.hasNext()){
            OnCoverEventListener onCoverEventListener = iterator.next();
            if(onCoverEventListener!=null){
                onCoverEventListener.onCoverEvent(eventCode, bundle);
            }
        }
    }

    /**
     * 当cover集合中存在Dpad控制层时，将焦点控制权交给它。
     */
    public void dPadRequestFocus(){
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null && receiver instanceof IDpadFocusCover){
                    receiver.onNotifyPlayEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS, null);
                }
            }
    }

    @Override
    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
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
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyNetWorkConnected(networkType);
    }

    @Override
    public void onNotifyNetWorkChanged(int networkType) {
        if(mPlayerObserverHandler!=null)
            mPlayerObserverHandler.onNotifyNetWorkChanged(networkType);
    }

    @Override
    public void onNotifyNetWorkError() {
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

    //gesture handle----------------

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
        super.onHorizontalSlide(percent,e1, e2, distanceX, distanceY);
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureHorizontalSlide(percent,e1, e2, distanceX, distanceY);
    }

    @Override
    public void onRightVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        super.onRightVerticalSlide(percent, e1, e2, distanceX, distanceY);
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureRightVerticalSlide(percent,e1, e2, distanceX, distanceY);
    }

    @Override
    public void onLeftVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        super.onLeftVerticalSlide(percent, e1, e2, distanceX, distanceY);
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureLeftVerticalSlide(percent,e1, e2, distanceX, distanceY);
    }

    @Override
    public void onEndGesture() {
        super.onEndGesture();
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureEnd();
    }

    @Override
    protected void onPlayerGestureEnableChange(boolean enable) {
        super.onPlayerGestureEnableChange(enable);
        if(mGestureObserverHandler!=null)
            mGestureObserverHandler.onGestureEnableChange(enable);
    }
}
