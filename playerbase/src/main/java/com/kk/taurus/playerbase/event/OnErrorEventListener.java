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

package com.kk.taurus.playerbase.event;

import android.os.Bundle;

/**
 * Created by Taurus on 2018/3/17.
 */

public interface OnErrorEventListener {

    int ERROR_EVENT_DATA_PROVIDER_ERROR = -88000;

    //A error that causes a play to terminate
    int ERROR_EVENT_COMMON = -88001;

    int ERROR_EVENT_UNKNOWN = -88002;

    int ERROR_EVENT_SERVER_DIED = -88003;

    int ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = -88004;

    int ERROR_EVENT_IO = -88005;

    int ERROR_EVENT_MALFORMED = -88006;

    int ERROR_EVENT_UNSUPPORTED = -88007;

    int ERROR_EVENT_TIMED_OUT = -88008;

    void onErrorEvent(int eventCode, Bundle bundle);

}
