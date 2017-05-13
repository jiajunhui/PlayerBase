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
