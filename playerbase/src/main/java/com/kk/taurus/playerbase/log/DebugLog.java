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

package com.kk.taurus.playerbase.log;

import android.os.Bundle;

import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;

/**
 * Created by Taurus on 2018/4/15.
 */

public class DebugLog {

    static final String EVENT_TAG_PLAY_EVENT = "frameEvent_play";
    static final String EVENT_TAG_ERROR_EVENT = "frameEvent_error";

    public static void onPlayEventLog(int eventCode, Bundle bundle){
        if(!PLog.LOG_OPEN)
            return;
        String value = "";
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                value = "PLAYER_EVENT_ON_DATA_SOURCE_SET";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE:
                value = "PLAYER_EVENT_ON_SURFACE_HOLDER_UPDATE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_SURFACE_UPDATE:
                value = "PLAYER_EVENT_ON_SURFACE_UPDATE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_START:
                value = "PLAYER_EVENT_ON_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
                value = "PLAYER_EVENT_ON_PAUSE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
                value = "PLAYER_EVENT_ON_RESUME";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
                value = "PLAYER_EVENT_ON_STOP";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESET:
                value = "PLAYER_EVENT_ON_RESET";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY:
                value = "PLAYER_EVENT_ON_DESTROY";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
                value = "PLAYER_EVENT_ON_BUFFERING_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
                value = "PLAYER_EVENT_ON_BUFFERING_END";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_UPDATE:
                value = "PLAYER_EVENT_ON_BUFFERING_UPDATE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_TO:
                value = "PLAYER_EVENT_ON_SEEK_TO";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                value = "PLAYER_EVENT_ON_SEEK_COMPLETE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RENDER_START:
                value = "PLAYER_EVENT_ON_RENDER_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                value = "PLAYER_EVENT_ON_PLAY_COMPLETE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE:
                value = "PLAYER_EVENT_ON_VIDEO_SIZE_CHANGE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                value = "PLAYER_EVENT_ON_PREPARED";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                value = "PLAYER_EVENT_ON_TIMER_UPDATE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START:
                value = "PLAYER_EVENT_ON_PROVIDER_DATA_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_SUCCESS:
                value = "PLAYER_EVENT_ON_PROVIDER_DATA_SUCCESS";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR:
                value = "PLAYER_EVENT_ON_PROVIDER_DATA_ERROR";
                break;
        }
        PLog.d(EVENT_TAG_PLAY_EVENT, value);
    }

    public static void onErrorEventLog(int eventCode, Bundle bundle) {
        if (!PLog.LOG_OPEN)
            return;
        String value;
        switch (eventCode){
            case OnErrorEventListener.ERROR_EVENT_COMMON:
                value = "ERROR_EVENT_COMMON";
                break;
            default:
                value = "unKnow error";
                break;
        }
        PLog.e(EVENT_TAG_ERROR_EVENT, value);
    }

}
