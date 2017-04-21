package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public class DefaultPlayerAdCover extends BaseAdCover {
    public DefaultPlayerAdCover(Context context) {
        super(context);
    }

    public DefaultPlayerAdCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_ad_cover,null);
    }

    @Override
    public void onGestureSingleTab(MotionEvent event) {
        super.onGestureSingleTab(event);
        if(onAdCoverClickListener!=null){
            onAdCoverClickListener.onAdCoverClick(adVideo);
        }
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        super.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        if(adListFinish)
            return;
        onNotifyAdTimer(curr, duration, bufferPercentage);
    }

    protected void onNotifyAdTimer(int curr, int duration, int bufferPercentage) {
        if(duration > 0 && duration > curr){
            setAdTimerState(true);
            setAdTimerText(String.valueOf((duration - curr)/1000) + "s");
        }else{
            setAdTimerState(false);
        }
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        handlePlayEvent(eventCode,bundle);
    }

    protected void handlePlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                setAdTimerState(false);
                break;
        }
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        super.onNotifyAdPrepared(adVideos);
        setAdCoverState(true);
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        super.onNotifyAdFinish(data, isAllFinish);
        if(isAllFinish){
            setAdCoverState(false);
        }
    }
}
