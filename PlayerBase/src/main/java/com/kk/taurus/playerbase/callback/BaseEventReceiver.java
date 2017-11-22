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

package com.kk.taurus.playerbase.callback;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.inter.IBindPlayer;
import com.kk.taurus.playerbase.inter.IRefreshData;
import com.kk.taurus.playerbase.inter.ITools;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.setting.VideoData;
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

public abstract class BaseEventReceiver implements IBindPlayer, PlayerObserver, OnCoverEventListener ,IRefreshData, ITools {

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
        player.clear();
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
    public void onNotifyNetWorkChanged(int networkType) {
        isWifi = (networkType==PlayerObserver.NETWORK_TYPE_WIFI);
    }

    @Override
    public void onNotifyNetWorkError() {
        isNetError = true;
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        adListFinish = false;
    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {

    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        adListFinish = isAllFinish;
    }

    public static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
