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
 * Created by Taurus on 2016/8/30.
 */
public interface OnPlayerEventListener {
    /** when player ready to start*/
    int EVENT_CODE_ON_INTENT_TO_START = 90041001;
    /** when player prepared*/
    int EVENT_CODE_PREPARED = 90041002;
    /** when video info ready , such as video rate info and so on.*/
    int EVENT_CODE_VIDEO_INFO_READY = 90041003;
    /** when player start render screen*/
    int EVENT_CODE_RENDER_START = 90041004;
    /** when the network is not good, start buffering*/
    int EVENT_CODE_BUFFERING_START = 90041005;
    /** buffering end*/
    int EVENT_CODE_BUFFERING_END = 90041006;
    /** when seek over call this event code*/
    int EVENT_CODE_SEEK_COMPLETE = 90041007;
    /** when setDataSource video complete*/
    int EVENT_CODE_PLAY_COMPLETE = 90041008;
    /** when player paused*/
    int EVENT_CODE_PLAY_PAUSE = 90041009;
    /** when player resumed*/
    int EVENT_CODE_PLAY_RESUME = 90041010;
    /** when change video definition*/
    int EVENT_CODE_PLAYER_CHANGE_DEFINITION = 90041011;
    /** when seek to */
    int EVENT_CODE_PLAYER_SEEK_TO = 90041012;
    /** when player set data*/
    int EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE = 90041013;
    /** when set video data*/
    int EVENT_CODE_PLAYER_ON_SET_VIDEO_DATA = 90041014;
    /** when set ad video data*/
    int EVENT_CODE_PLAYER_ON_SET_AD_DATA = 90041015;
    /** when set play data include video data and ad data*/
    int EVENT_CODE_PLAYER_ON_SET_PLAY_DATA = 90041016;
    /** when player stop*/
    int EVENT_CODE_PLAYER_ON_STOP = 90041017;
    /** when player destroy*/
    int EVENT_CODE_PLAYER_ON_DESTROY = 90041018;

    /** when ready to switch player type*/
    int EVENT_CODE_ON_INTENT_TO_SWITCH_PLAYER_TYPE = 90041019;

    int EVENT_CODE_PLAYER_FULL_SCREEN = 90041020;
    int EVENT_CODE_PLAYER_QUIT_FULL_SCREEN = 90041021;
    int EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS = 90041022;


    String BUNDLE_KEY_POSITION = "position";
    String BUNDLE_KEY_VIDEO_DATA = "video_data";
    String BUNDLE_KEY_RATE_DATA = "rate_data";

    void onPlayerEvent(int eventCode, Bundle bundle);
}
