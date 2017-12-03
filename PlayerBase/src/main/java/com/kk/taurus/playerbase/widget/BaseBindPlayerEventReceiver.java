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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;
import com.kk.taurus.playerbase.inter.IBindPlayer;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.EventDistributionHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taurus on 2017/3/25.
 */

public abstract class BaseBindPlayerEventReceiver extends BaseContainer implements IPlayer, IBindPlayer, OnCoverEventListener {

    private List<WeakReference<OnPlayerEventListener>> mPlayerEventListenerList = new ArrayList<>();
    private List<WeakReference<OnErrorListener>> mErrorEventListenerList = new ArrayList<>();
    private List<OnCoverEventListener> mCoverEventListenerList = new ArrayList<>();

    private BaseReceiverCollections mReceiverCollections;
    private EventDistributionHandler mEventDistributionHandler;

    public BaseBindPlayerEventReceiver(@NonNull Context context) {
        super(context);
    }

    public BaseBindPlayerEventReceiver(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        callBackPlayerEventListener(eventCode, bundle);
        distributionPlayerEvent(eventCode, bundle);
    }

    private void distributionPlayerEvent(int eventCode, Bundle bundle){
        if(mEventDistributionHandler!=null){
            mEventDistributionHandler.onDistributionPlayerEvent(eventCode, bundle);
        }
    }

    private void callBackPlayerEventListener(int eventCode, Bundle bundle){
        if(mPlayerEventListenerList==null)
            return;
        Iterator<WeakReference<OnPlayerEventListener>> iterator = mPlayerEventListenerList.iterator();
        while (iterator.hasNext()){
            WeakReference<OnPlayerEventListener> onPlayerEventListener = iterator.next();
            if(onPlayerEventListener!=null){
                OnPlayerEventListener eventListener = onPlayerEventListener.get();
                if(eventListener!=null){
                    eventListener.onPlayerEvent(eventCode, bundle);
                }
            }
        }
    }

    protected void onErrorEvent(int eventCode, Bundle bundle){
        callBackErrorEventListener(eventCode, bundle);
        distributionErrorEvent(eventCode, bundle);
    }

    private void distributionErrorEvent(int eventCode, Bundle bundle){
        if(mEventDistributionHandler!=null){
            mEventDistributionHandler.onDistributionErrorEvent(eventCode, bundle);
        }
    }

    public void doConfigChange(Configuration newConfig) {
        if(mEventDistributionHandler!=null){
            mEventDistributionHandler.doConfigChange(newConfig);
        }
    }

    private void callBackErrorEventListener(int eventCode, Bundle bundle) {
        if(mErrorEventListenerList==null)
            return;
        Iterator<WeakReference<OnErrorListener>> iterator = mErrorEventListenerList.iterator();
        while (iterator.hasNext()){
            WeakReference<OnErrorListener> onErrorListener = iterator.next();
            if(onErrorListener!=null){
                OnErrorListener errorListener = onErrorListener.get();
                if(errorListener!=null){
                    errorListener.onError(eventCode,bundle);
                }
            }
        }
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mPlayerEventListenerList.add(new WeakReference<>(onPlayerEventListener));
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mErrorEventListenerList.add(new WeakReference<>(onErrorListener));
    }

    public void removePlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        mPlayerEventListenerList.remove(new WeakReference<>(onPlayerEventListener));
    }

    public void removeErrorEventListener(OnErrorListener onErrorListener){
        mErrorEventListenerList.remove(new WeakReference<>(onErrorListener));
    }

    private void clearEventListener(){
        mPlayerEventListenerList.clear();
        mErrorEventListenerList.clear();
        mCoverEventListenerList.clear();
    }

    public boolean isExpectedBufferAvailable(){
        return (getBufferPercentage()*getDuration()/100) > getCurrentPosition();
    }

    public void setScreenOrientationLandscape(boolean landscape) {
        /** modify 2017/11/17
         *
         *  this operation is not dependent on Activity context as much as possible.
         *
         * */
        int code = landscape?OnPlayerEventListener.EVENT_CODE_ON_INTENT_SET_SCREEN_ORIENTATION_LANDSCAPE:OnPlayerEventListener.EVENT_CODE_ON_INTENT_SET_SCREEN_ORIENTATION_PORTRAIT;
        onPlayerEvent(code,null);
    }

    public boolean isLandscape() {
        return mAppContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Deprecated
    public void bindCoverCollections(BaseReceiverCollections coverCollections){
        bindReceiverCollections(coverCollections);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        mEventDistributionHandler = new EventDistributionHandler();
    }

    public void bindReceiverCollections(BaseReceiverCollections receiverCollections){
        this.mReceiverCollections = receiverCollections;
        mEventDistributionHandler.bindEventReceiverCollections(mReceiverCollections);
        initCovers(mAppContext);
        onReceiverCollectionsHasBind();
    }

    protected OnPlayerGestureListener getPlayerGestureListener(){
        return mEventDistributionHandler;
    }

    @Deprecated
    public void unbindCoverCollections(){
        unbindReceiverCollections();
    }

    public void unbindReceiverCollections(){
        if(mReceiverCollections !=null){
            mReceiverCollections.clear();
        }
        removeAllCovers();
    }

    protected void onReceiverCollectionsHasBind(){
        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_RECEIVER_COLLECTIONS_NEW_BIND,null);
    }

    private void initCovers(Context context) {
        if(mReceiverCollections ==null)
            return;
        List<BaseEventReceiver> covers = mReceiverCollections.getReceivers();
        for(BaseEventReceiver cover : covers){
            if(cover instanceof BaseCover){
                addCover((BaseCover) cover);
            }
        }
        onCoversHasInit(context);
    }

    public BaseReceiverCollections getReceiverCollections(){
        return mReceiverCollections;
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
        if(mReceiverCollections !=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver: mReceiverCollections.getReceivers()){
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
        if(mReceiverCollections !=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver: mReceiverCollections.getReceivers()){
                if(receiver!=null && receiver instanceof IDpadFocusCover){
                    receiver.onNotifyPlayEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS, null);
                }
            }
    }

    @Override
    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        mEventDistributionHandler.onBindPlayer(player, onCoverEventListener);
    }

    @Override
    protected void onPlayerGestureEnableChange(boolean enable) {
        super.onPlayerGestureEnableChange(enable);
        if(mEventDistributionHandler!=null)
            mEventDistributionHandler.onPlayerGestureEnableChange(enable);
    }

    @Override
    public void destroy() {
        clearEventListener();
        unbindReceiverCollections();
    }
}
