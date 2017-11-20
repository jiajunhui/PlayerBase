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

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.inter.ITimerGetter;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taurus on 2017/3/25.
 */

public abstract class BaseSettingPlayer extends BaseBindEventReceiver implements IPlayer {

    private final String TAG = "BaseSettingPlayer";

    protected Rate rate;
    protected int mStatus = STATUS_IDLE;
    private DecodeMode mDecodeMode = DecodeMode.SOFT;
    private AspectRatio aspectRatio = AspectRatio.AspectRatio_FILL_PARENT;

    private List<WeakReference<OnPlayerEventListener>> mPlayerEventListenerList = new ArrayList<>();
    private List<WeakReference<OnErrorListener>> mErrorEventListenerList = new ArrayList<>();

    public BaseSettingPlayer(@NonNull Context context) {
        super(context);
    }

    public BaseSettingPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        this.rate = rate;
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        onHandleStatus(eventCode,bundle);
        callBackPlayerEventListener(eventCode, bundle);
        onNotifyPlayEvent(eventCode, bundle);
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

    private void onHandleStatus(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                mStatus = STATUS_STARTED;
                break;
            case OnPlayerEventListener.EVENT_CODE_ON_PLAYER_TIMER_UPDATE:
                int curr = bundle.getInt(ITimerGetter.KEY_TIMER_CURRENT_POSITION);
                int duration = bundle.getInt(ITimerGetter.KEY_TIMER_DURATION);
                int bufferPos = bundle.getInt(ITimerGetter.KEY_TIMER_BUFFER_POSITION);
                onNotifyPlayTimerCounter(curr,duration,bufferPos);
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE:
                mStatus = STATUS_PAUSED;
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAY_RESUME:
                mStatus = STATUS_STARTED;
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                mStatus = STATUS_STOPPED;
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
        }
    }

    protected void onErrorEvent(int eventCode, Bundle bundle){
        onHandleErrorStatus(eventCode, bundle);
        callBackErrorEventListener(eventCode, bundle);
        onNotifyErrorEvent(eventCode, bundle);
    }

    public void doConfigChange(Configuration newConfig) {
        onNotifyConfigurationChanged(newConfig);
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

    private void onHandleErrorStatus(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnErrorListener.ERROR_CODE_COMMON:
                mStatus = STATUS_ERROR;
                break;
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
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        this.mDecodeMode = decodeMode;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public AspectRatio getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public int getStatus() {
        return mStatus;
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

    @Override
    public void destroy() {
        clearEventListener();
        unbindReceiverCollections();
    }
}
