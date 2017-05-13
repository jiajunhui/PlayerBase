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
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseCoverObserver<T extends CoverData> implements CoverObserver ,PlayerObserver {

    protected Context mContext;
    protected BaseCover mCover;
    protected BaseVideoDataAdapter mDataAdapter;

    public BaseCoverObserver(Context context){
        this.mContext = context;
    }

    @Override
    public View initCustomCoverView(Context context) {
        return null;
    }

    @Override
    public void onCoverViewInit(View coverView) {

    }

    @Override
    public void onCoverVisibilityChange(View coverView, int visibility) {

    }

    @Override
    public void onBindCover(BaseCover cover) {
        this.mCover = cover;
    }

    public void onRefreshDataAdapter(BaseVideoDataAdapter dataAdapter){
        this.mDataAdapter = dataAdapter;
    }

    public void onDataChange(T data) {

    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {

    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {

    }

    @Override
    public void onNotifyNetWorkError() {

    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {

    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {

    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {

    }
}
