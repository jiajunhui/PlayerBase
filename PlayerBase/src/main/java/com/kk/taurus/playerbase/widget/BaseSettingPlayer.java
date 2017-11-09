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

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.MSG;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taurus on 2017/3/25.
 */

public abstract class BaseSettingPlayer extends BaseBindPlayerEvent {

    private final String TAG = "BaseSettingPlayer";
    protected VideoData dataSource;
    protected Rate rate;
    private int mPlayerType;
    protected int startPos;
    protected int mStatus = STATUS_IDLE;
    private DecodeMode mDecodeMode = DecodeMode.SOFT;
    private AspectRatio aspectRatio = AspectRatio.AspectRatio_FILL_PARENT;

    private List<OnPlayerEventListener> mPlayerEventListenerList = new ArrayList<>();
    private List<OnErrorListener> mErrorEventListenerList = new ArrayList<>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG.MSG_CODE_PLAYING:
                    int curr = getCurrentPosition();
                    int duration = getDuration();
                    int bufferPercentage = getBufferPercentage();
                    int bufferPos = bufferPercentage*duration/100;
                    onNotifyPlayTimerCounter(curr,duration,bufferPos);
                    if(duration > 0 && curr >=0){
                        sendPlayMsg();
                    }else{
                        removePlayMsg();
                    }
                    break;
            }
        }
    };

    public BaseSettingPlayer(@NonNull Context context) {
        super(context);
    }

    public BaseSettingPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSettingPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDataSource(VideoData dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        this.rate = rate;
    }

    @Override
    protected void onPlayerContainerHasInit(Context context) {
        super.onPlayerContainerHasInit(context);
        this.mPlayerType = PlayerType.getInstance().getDefaultPlayerType();
    }

    @Override
    protected void onPlayerEvent(int eventCode, Bundle bundle){
        onHandleStatus(eventCode,bundle);
        super.onPlayerEvent(eventCode, bundle);
        callBackPlayerEventListener(eventCode, bundle);
    }

    private void callBackPlayerEventListener(int eventCode, Bundle bundle){
        if(mPlayerEventListenerList==null)
            return;
        Iterator<OnPlayerEventListener> iterator = mPlayerEventListenerList.iterator();
        while (iterator.hasNext()){
            OnPlayerEventListener onPlayerEventListener = iterator.next();
            if(onPlayerEventListener!=null){
                onPlayerEventListener.onPlayerEvent(eventCode, bundle);
            }
        }
    }

    private void onHandleStatus(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                removePlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                startPlay();
                break;
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                mStatus = STATUS_STARTED;
                startPlay();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE:
                mStatus = STATUS_PAUSED;
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAY_RESUME:
                mStatus = STATUS_STARTED;
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                sendPlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                mStatus = STATUS_STOPPED;
                removePlayMsg();
                break;
        }
    }

    public void sendPlayMsg() {
        removePlayMsg();
        mHandler.sendEmptyMessageDelayed(MSG.MSG_CODE_PLAYING,1000);
    }

    protected void startPlay(){
        removePlayMsg();
        mHandler.sendEmptyMessage(MSG.MSG_CODE_PLAYING);
    }

    public void removePlayMsg() {
        mHandler.removeMessages(MSG.MSG_CODE_PLAYING);
    }

    @Override
    protected void onErrorEvent(int eventCode, Bundle bundle){
        onHandleErrorStatus(eventCode, bundle);
        super.onErrorEvent(eventCode, bundle);
        callBackErrorEventListener(eventCode, bundle);
    }

    private void callBackErrorEventListener(int eventCode, Bundle bundle) {
        if(mErrorEventListenerList==null)
            return;
        Iterator<OnErrorListener> iterator = mErrorEventListenerList.iterator();
        while (iterator.hasNext()){
            OnErrorListener onErrorListener = iterator.next();
            if(onErrorListener!=null){
                onErrorListener.onError(eventCode,bundle);
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
        this.mPlayerEventListenerList.add(onPlayerEventListener);
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mErrorEventListenerList.add(onErrorListener);
    }

    public void removePlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        mPlayerEventListenerList.remove(onPlayerEventListener);
    }

    public void removeErrorEventListener(OnErrorListener onErrorListener){
        mErrorEventListenerList.remove(onErrorListener);
    }

    private void clearEventListener(){
        mPlayerEventListenerList.clear();
        mErrorEventListenerList.clear();
    }

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

    public void updatePlayerType(int playerType){
        if(mPlayerType!=playerType){
            this.mPlayerType = playerType;
            onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_SWITCH_PLAYER_TYPE,null);
            notifyPlayerWidget(mAppContext);
        }
    }

    public int getPlayerType() {
        return mPlayerType;
    }

    @Override
    public void setScreenOrientationLandscape(boolean landscape) {
        ((Activity)mAppContext).setRequestedOrientation(landscape? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE:ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public boolean isLandscape() {
        return mAppContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public void destroy() {
        super.destroy();
        removePlayMsg();
        clearEventListener();
    }
}
