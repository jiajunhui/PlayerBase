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
 *
 * 播放相关事件，为防止与自添加事件值冲突，
 * 故框架内部播放事件码以-904开头的7位数字。
 * 以及{@link OnCoverEventListener}中的事件码是以-704开头的7位数字。
 * {@link OnErrorListener}中的事件码是以-804开头的7位数字。
 *
 */
public interface OnPlayerEventListener {

    int EVENT_CODE_ON_PLAYER_PREPARING = -9041000;
    /** when player ready to start*/
    int EVENT_CODE_ON_INTENT_TO_START = -9041001;
    /** when player prepared*/
    int EVENT_CODE_PREPARED = -9041002;
    /** when video info ready , such as video rate info and so on.*/
    int EVENT_CODE_VIDEO_INFO_READY = -9041003;
    /** when player start render screen*/
    int EVENT_CODE_RENDER_START = -9041004;
    /** when the network is not good, start buffering*/
    int EVENT_CODE_BUFFERING_START = -9041005;
    /** buffering end*/
    int EVENT_CODE_BUFFERING_END = -9041006;
    /** when seek over call this event code*/
    int EVENT_CODE_SEEK_COMPLETE = -9041007;
    /** when setDataSource video complete*/
    int EVENT_CODE_PLAY_COMPLETE = -9041008;
    /** when player paused*/
    int EVENT_CODE_PLAY_PAUSE = -9041009;
    /** when player resumed*/
    int EVENT_CODE_PLAY_RESUME = -9041010;
    /** when change video definition*/
    int EVENT_CODE_PLAYER_CHANGE_DEFINITION = -9041011;
    /** when seek to */
    int EVENT_CODE_PLAYER_SEEK_TO = -9041012;
    /** when player set data*/
    int EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE = -9041013;
    /** when set video data*/
    int EVENT_CODE_PLAYER_ON_SET_VIDEO_DATA = -9041014;
    /** when set ad video data*/
    int EVENT_CODE_PLAYER_ON_SET_AD_DATA = -9041015;
    /** when set play data include video data and ad data*/
    int EVENT_CODE_PLAYER_ON_SET_PLAY_DATA = -9041016;
    /** when player stop*/
    int EVENT_CODE_PLAYER_ON_STOP = -9041017;
    /** when player destroy*/
    int EVENT_CODE_PLAYER_ON_DESTROY = -9041018;

    /** when ready to switch player type*/
    int EVENT_CODE_ON_INTENT_TO_SWITCH_PLAYER_TYPE = -9041019;

    int EVENT_CODE_PLAYER_FULL_SCREEN = -9041020;
    int EVENT_CODE_PLAYER_QUIT_FULL_SCREEN = -9041021;

    int EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS = -9041022;

    /** on user player video definition list get*/
    int EVENT_CODE_ON_DEFINITION_LIST_READY = -9041023;

    /** when screen orientation portrait*/
    int EVENT_CODE_ON_INTENT_SET_SCREEN_ORIENTATION_PORTRAIT = -9041024;

    /** when screen orientation landscape*/
    int EVENT_CODE_ON_INTENT_SET_SCREEN_ORIENTATION_LANDSCAPE = -9041025;

    /** when player has bind receiver collections*/
    int EVENT_CODE_ON_RECEIVER_COLLECTIONS_NEW_BIND = -9041026;

    /** on decoder render surface holder update*/
    int EVENT_CODE_ON_SURFACE_HOLDER_UPDATE = -9041027;

    /** on player timer counter update*/
    int EVENT_CODE_ON_PLAYER_TIMER_UPDATE = -9041028;

    /** on player container destroy, contain cover receiver collections*/
    int EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY = -9041032;

    /** on decoder render surface update*/
    int EVENT_CODE_ON_SURFACE_UPDATE = -9041033;

    int EVENT_CODE_ON_RENDER_VIEW_TYPE_UPDATE = -9041034;
    int EVENT_CODE_ON_RENDER_ASPECT_RATIO_UPDATE = -9041035;

    int EVENT_CODE_ON_VIDEO_SIZE_CHANGE = -9041036;
    int EVENT_CODE_ON_VIDEO_ROTATION_CHANGED = -9041037;

    /** network event code*/
    int EVENT_CODE_ON_NETWORK_ERROR = -9042000;
    int EVENT_CODE_ON_NETWORK_CONNECTED = -9042002;
    /** network event code*/


    String BUNDLE_KEY_VIDEO_WIDTH = "video_width";
    String BUNDLE_KEY_VIDEO_HEIGHT = "video_height";


    String BUNDLE_KEY_INT_DATA = "int_data";
    String BUNDLE_KEY_STRING_DATA = "string_data";
    String BUNDLE_KEY_SERIALIZABLE_DATA = "serializable_data";
    String BUNDLE_KEY_POSITION = "position";
    String BUNDLE_KEY_VIDEO_DATA = "video_data";
    String BUNDLE_KEY_RATE_DATA = "rate_data";

    void onPlayerEvent(int eventCode, Bundle bundle);
}
