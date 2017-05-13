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

package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 *
 * 覆盖层基类，默认显示状态为GONE。
 *
 */

public abstract class BaseCover extends BasePlayerToolsReceiver implements ICover , View.OnClickListener{

    private View mCoverView;
    private BaseCoverObserver coverObserver;
    protected boolean coverEnable = true;

    public BaseCover(Context context){
        this(context,null);
    }

    public BaseCover(Context context, BaseCoverObserver coverObserver){
        super(context);
        this.coverObserver = coverObserver;
        handCoverView(context);
        if(this.coverObserver!=null){
            this.coverObserver.onCoverViewInit(mCoverView);
        }
        setDefaultGone();
        findView();
        afterFindView();
    }

    protected void afterFindView() {

    }

    protected void setDefaultGone() {
        setCoverVisibility(View.GONE);
    }

    private void handCoverView(Context context){
        if(coverObserver!=null){
            View customView = coverObserver.initCustomCoverView(context);
            if(customView!=null){
                mCoverView = customView;
            }else{
                mCoverView = initCoverLayout(context);
            }
        }else{
            mCoverView = initCoverLayout(context);
        }
    }

    protected abstract void findView();

    protected <V> V findViewById(int id){
        return (V) mCoverView.findViewById(id);
    }

    @Override
    public abstract View initCoverLayout(Context context);

    public void setCoverEnable(boolean enable) {
        if(!enable){
            setCoverVisibility(View.GONE);
        }
        this.coverEnable = enable;
    }

    @Override
    public void setCoverVisibility(int visibility) {
        if(!coverEnable)
            return;
        if(mCoverView!=null){
            mCoverView.setVisibility(visibility);
            if(coverObserver!=null){
                coverObserver.onCoverVisibilityChange(getView(),visibility);
            }
        }
    }

    public void onRefreshDataAdapter(BaseVideoDataAdapter dataAdapter){
        super.onRefreshDataAdapter(dataAdapter);
        if(coverObserver!=null){
            coverObserver.onRefreshDataAdapter(dataAdapter);
        }
    }

    public void onRefreshCoverData(CoverData data) {
        super.onRefreshCoverData(data);
        if(coverObserver!=null){
            coverObserver.onDataChange(data);
        }
    }

    protected boolean isVisibilityGone(){
        return getView().getVisibility()!=View.VISIBLE;
    }

    @Override
    public View getView() {
        return mCoverView;
    }

    @Override
    public int getCoverLevel() {
        return COVER_LEVEL_LOW;
    }

    @Override
    public CoverObserver getCoverObserver() {
        return coverObserver;
    }

    @Override
    public void onClick(View v) {

    }

    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        super.onBindPlayer(player, onCoverEventListener);
        if(coverObserver!=null){
            coverObserver.onBindCover(this);
        }
    }

    protected void releaseFocusToDpadCover(){
        if(getPlayer()!=null){
            getPlayer().dPadRequestFocus();
        }
    }


    //--------------------------------------------------------------------------------------------

    //*************************************************************************************
    // for some event
    //*************************************************************************************

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        if(coverObserver!=null){
            coverObserver.onNotifyPlayEvent(eventCode, bundle);
        }
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {
        super.onNotifyConfigurationChanged(newConfig);
        if(coverObserver!=null){
            coverObserver.onNotifyConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        super.onNotifyErrorEvent(eventCode, bundle);
        if(coverObserver!=null){
            coverObserver.onNotifyErrorEvent(eventCode, bundle);
        }
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        super.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        if(coverObserver!=null){
            coverObserver.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        }
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        super.onNotifyNetWorkConnected(networkType);
        if(coverObserver!=null){
            coverObserver.onNotifyNetWorkConnected(networkType);
        }
    }

    @Override
    public void onNotifyNetWorkError() {
        super.onNotifyNetWorkError();
        if(coverObserver!=null){
            coverObserver.onNotifyNetWorkError();
        }
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        super.onNotifyAdPrepared(adVideos);
        if(coverObserver!=null){
            coverObserver.onNotifyAdPrepared(adVideos);
        }
    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {
        super.onNotifyAdStart(adVideo);
        if(coverObserver!=null){
            coverObserver.onNotifyAdStart(adVideo);
        }
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        super.onNotifyAdFinish(data, isAllFinish);
        if(coverObserver!=null){
            coverObserver.onNotifyAdFinish(data, isAllFinish);
        }
    }
}
