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

import android.os.Bundle;

/**
 * Created by Taurus on 2017/3/27.
 */

public interface OnCoverEventListener {

    String KEY_INT_DATA = "int_data";

    int EVENT_CODE_ON_PLAYER_CONTROLLER_SHOW = -50001;
    int EVENT_CODE_ON_PLAYER_CONTROLLER_HIDDEN = -50002;

    int EVENT_CODE_ON_PLAYER_ERROR_SHOW = -50003;
    int EVENT_CODE_ON_PLAYER_ERROR_HIDDEN = -50004;

    int EVENT_CODE_ON_SEEK_BAR_PROGRESS_CHANGE = -50005;
    int EVENT_CODE_ON_SEEK_BAR_START_TRACKING_TOUCH = -50006;
    int EVENT_CODE_ON_SEEK_BAR_STOP_TRACKING_TOUCH = -50007;

    void onCoverEvent(int eventCode, Bundle bundle);
}
