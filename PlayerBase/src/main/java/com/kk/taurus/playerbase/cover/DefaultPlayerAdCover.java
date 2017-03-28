package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.cover.base.BaseAdCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public class DefaultPlayerAdCover extends BaseAdCover {

    public DefaultPlayerAdCover(Context context, CoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_ad_cover,null);
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        super.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        if(duration > 0 && duration > curr){
            setAdTimerText(String.valueOf((duration - curr)/1000) + "s");
        }
    }

    @Override
    public void onNotifyAdPreparedStart(List<BaseAdVideo> adVideos) {
        super.onNotifyAdPreparedStart(adVideos);
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
