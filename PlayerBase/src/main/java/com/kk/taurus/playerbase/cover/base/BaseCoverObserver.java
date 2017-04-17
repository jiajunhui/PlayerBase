package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseCoverObserver<T extends CoverData> implements CoverObserver ,PlayerObserver {

    protected Context mContext;
    protected BaseCover mCover;
    protected BasePlayer mPlayer;

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
    public void onBindCover(BaseCover cover, BasePlayer player) {
        this.mCover = cover;
        this.mPlayer = player;
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
