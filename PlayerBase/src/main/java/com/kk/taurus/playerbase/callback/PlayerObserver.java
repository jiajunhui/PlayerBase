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

/**
 * Created by Taurus on 2017/3/24.
 */

public interface PlayerObserver {

    int NETWORK_TYPE_MOBILE = 1;
    int NETWORK_TYPE_WIFI = 2;

    /**
     * on configuration changed,notify some receivers.
     * @param newConfig
     */
    void onNotifyConfigurationChanged(Configuration newConfig);

    /**
     * notify some event to receivers.
     * @param eventCode event identify.
     * @param bundle include some data on this event code.
     */
    void onNotifyPlayEvent(int eventCode, Bundle bundle);

    /**
     * notify error event on player occur error.
     * @param eventCode
     * @param bundle
     */
    void onNotifyErrorEvent(int eventCode, Bundle bundle);

    /**
     * notify progress timer.
     * @param curr current time millions.
     * @param duration video length millions.
     * @param bufferPercentage video buffered percentage.
     */
    void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage);

    /**
     * on network connected,notify receivers.
     * @param networkType network type.
     */
    void onNotifyNetWorkConnected(int networkType);

    /**
     * on network occur error.
     */
    void onNotifyNetWorkError();

}
