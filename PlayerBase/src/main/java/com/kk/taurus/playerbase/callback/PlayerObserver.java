package com.kk.taurus.playerbase.callback;

import android.content.res.Configuration;
import android.os.Bundle;

import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface PlayerObserver {

    int NETWORK_TYPE_MOBILE = 1;
    int NETWORK_TYPE_WIFI = 2;

    void onNotifyConfigurationChanged(Configuration newConfig);
    void onNotifyPlayEvent(int eventCode, Bundle bundle);
    void onNotifyErrorEvent(int eventCode, Bundle bundle);
    void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage);
    void onNotifyNetWorkConnected(int networkType);
    void onNotifyNetWorkError();
    void onNotifyAdPrepared(List<BaseAdVideo> adVideos);
    void onNotifyAdStart(BaseAdVideo adVideo);
    void onNotifyAdFinish(VideoData data,boolean isAllFinish);

}
