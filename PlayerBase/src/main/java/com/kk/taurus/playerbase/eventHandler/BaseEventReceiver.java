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

package com.kk.taurus.playerbase.eventHandler;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.inter.IBindPlayer;
import com.kk.taurus.playerbase.inter.IPlayerCoverHandle;
import com.kk.taurus.playerbase.inter.IRefreshData;
import com.kk.taurus.playerbase.inter.ITools;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.utils.CommonUtils;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Taurus on 2017/4/27.
 *
 * 播放器事件传递基类
 *
 */

public abstract class BaseEventReceiver implements IBindPlayer, PlayerObserver,
        OnCoverEventListener,IRefreshData, ITools, IPlayerCoverHandle {

    protected Context mContext;
    protected int mScreenW,mScreenH;
    protected WeakReference<BasePlayer> player;
    private OnCoverEventListener onCoverEventListener;
    protected boolean adListFinish = true;
    protected boolean isNetError = false;
    protected boolean isOccurError = false;
    protected boolean isWifi;
    private Bundle mBundle;

    protected MyHandler mHandler = new MyHandler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            _handleMessage(msg);
        }
    };

    protected void _handleMessage(Message msg){

    }

    public BaseEventReceiver(Context context){
        this.mContext = context;
        initBaseInfo(context);
    }

    protected void initBaseInfo(Context context) {
        mBundle = new Bundle();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenW = displayMetrics.widthPixels;
        mScreenH = displayMetrics.heightPixels;
    }

    public void onRefreshDataAdapter(BaseVideoDataAdapter dataAdapter){

    }

    public void onRefreshCoverData(CoverData data) {

    }

    @Override
    public String getString(int resId) {
        if(mContext!=null){
            return mContext.getString(resId);
        }
        return null;
    }

    public Activity getActivity(){
        if(mContext!=null && mContext instanceof Activity)
            return (Activity) mContext;
        return null;
    }

    @Override
    public Bundle getBundle() {
        mBundle.clear();
        return mBundle;
    }

    @Override
    public int getScreenOrientation() {
        if(mContext==null)
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        if(!(mContext instanceof Activity))
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return CommonUtils.getScreenOrientation((Activity) mContext,mScreenW,mScreenH);
    }

    protected void onDestroy(){
        mHandler.removeCallbacks(null);
    }

    @Override
    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        this.player = new WeakReference<>(player);
        this.onCoverEventListener = onCoverEventListener;
    }

    protected BasePlayer getPlayer(){
        if(player!=null){
            return player.get();
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------

    //*************************************************************************************
    // for some event
    //*************************************************************************************

    /**
     * on receive cover event
     * @param eventCode
     * @param bundle
     */
    public void onCoverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                isOccurError = false;
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
            case OnPlayerEventListener.EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY:
                onDestroy();
                break;
        }
    }

    protected void notifyCoverEvent(int eventCode, Bundle bundle){
        if(onCoverEventListener!=null){
            onCoverEventListener.onCoverEvent(eventCode, bundle);
        }
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        isOccurError = true;
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {

    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        isWifi = (networkType==PlayerObserver.NETWORK_TYPE_WIFI);
        isNetError = false;
    }

    @Override
    public void onNotifyNetWorkError() {
        isNetError = true;
    }

    public static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    //--------------------------------------------------------------------------------------------

    //#####################################################################################
    // for player handle
    //#####################################################################################

    @Override
    public void pause() {
        if(getPlayer()!=null){
            getPlayer().pause();
        }
    }

    @Override
    public void resume() {
        if(getPlayer()!=null){
            getPlayer().resume();
        }
    }

    @Override
    public void seekTo(int msc) {
        if(getPlayer()!=null){
            getPlayer().seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(getPlayer()!=null){
            getPlayer().stop();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(getPlayer()!=null){
            getPlayer().rePlay(msc);
        }
    }

    @Override
    public boolean isPlaying() {
        if(getPlayer()!=null){
            return getPlayer().isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(getPlayer()!=null){
            return getPlayer().getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(getPlayer()!=null){
            return getPlayer().getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(getPlayer()!=null){
            return getPlayer().getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(getPlayer()!=null){
            return getPlayer().getStatus();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(getPlayer()!=null){
            return getPlayer().getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(getPlayer()!=null){
            return getPlayer().getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(getPlayer()!=null){
            getPlayer().changeVideoDefinition(rate);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(getPlayer()!=null){
            getPlayer().setAspectRatio(aspectRatio);
        }
    }
}
