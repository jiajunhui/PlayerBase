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

package com.kk.taurus.playerbase.assist;

/**
 * Created by Taurus on 2018/5/21.
 */
public interface InterEvent {

    int CODE_REQUEST_PAUSE = -66001;
    int CODE_REQUEST_RESUME = -66003;
    int CODE_REQUEST_SEEK = -66005;
    int CODE_REQUEST_STOP = -66007;
    int CODE_REQUEST_RESET = -66009;
    int CODE_REQUEST_RETRY = -660011;
    int CODE_REQUEST_REPLAY = -66013;
    int CODE_REQUEST_PLAY_DATA_SOURCE = -66014;
    int CODE_REQUEST_NOTIFY_TIMER = -66015;
    int CODE_REQUEST_STOP_TIMER = -66016;

}
