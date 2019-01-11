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

import com.kk.taurus.playerbase.event.EventKey;
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
        String value;
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
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                value = "PLAYER_EVENT_ON_VIDEO_RENDER_START";
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
                value = "PLAYER_EVENT_ON_TIMER_UPDATE"
                        + (bundle!=null?(", curr = " + String.valueOf(bundle.getInt(EventKey.INT_ARG1))
                        + ",duration = " + String.valueOf(bundle.getInt(EventKey.INT_ARG2))
                        + ",bufferPercentage = " + String.valueOf(bundle.getInt(EventKey.INT_ARG3))):"");
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED:
                value = "PLAYER_EVENT_ON_VIDEO_ROTATION_CHANGED";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_DECODER_START:
                value = "PLAYER_EVENT_ON_AUDIO_DECODER_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_RENDER_START:
                value = "PLAYER_EVENT_ON_AUDIO_RENDER_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_SEEK_RENDERING_START:
                value = "PLAYER_EVENT_ON_AUDIO_SEEK_RENDERING_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_NETWORK_BANDWIDTH:
                value = "PLAYER_EVENT_ON_NETWORK_BANDWIDTH";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_BAD_INTERLEAVING:
                value = "PLAYER_EVENT_ON_BAD_INTERLEAVING";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_NOT_SEEK_ABLE:
                value = "PLAYER_EVENT_ON_NOT_SEEK_ABLE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_METADATA_UPDATE:
                value = "PLAYER_EVENT_ON_METADATA_UPDATE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMED_TEXT_ERROR:
                value = "PLAYER_EVENT_ON_TIMED_TEXT_ERROR";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE:
                value = "PLAYER_EVENT_ON_UNSUPPORTED_SUBTITLE";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT:
                value = "PLAYER_EVENT_ON_SUBTITLE_TIMED_OUT";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                value = "PLAYER_EVENT_ON_STATUS_CHANGE";
                break;


            //--------------------------------provider event-----------------------------

            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_START:
                value = "PLAYER_EVENT_ON_PROVIDER_DATA_START";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_SUCCESS:
                value = "PLAYER_EVENT_ON_PROVIDER_DATA_SUCCESS";
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PROVIDER_DATA_ERROR:
                value = "PLAYER_EVENT_ON_PROVIDER_DATA_ERROR";
                break;
            default:
                value = "UNKNOWN EVENT, maybe from provider, maybe from user custom code.";
                break;
        }
        PLog.d(EVENT_TAG_PLAY_EVENT, value);
    }

    public static void onErrorEventLog(int eventCode, Bundle bundle) {
        if (!PLog.LOG_OPEN)
            return;
        String value;
        switch (eventCode){
            case OnErrorEventListener.ERROR_EVENT_DATA_PROVIDER_ERROR:
                value = "ERROR_EVENT_DATA_PROVIDER_ERROR";
                break;
            case OnErrorEventListener.ERROR_EVENT_COMMON:
                value = "ERROR_EVENT_COMMON";
                break;
            case OnErrorEventListener.ERROR_EVENT_UNKNOWN:
                value = "ERROR_EVENT_UNKNOWN";
                break;
            case OnErrorEventListener.ERROR_EVENT_SERVER_DIED:
                value = "ERROR_EVENT_SERVER_DIED";
                break;
            case OnErrorEventListener.ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                value = "ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK";
                break;
            case OnErrorEventListener.ERROR_EVENT_IO:
                value = "ERROR_EVENT_IO";
                break;
            case OnErrorEventListener.ERROR_EVENT_MALFORMED:
                value = "ERROR_EVENT_MALFORMED";
                break;
            case OnErrorEventListener.ERROR_EVENT_UNSUPPORTED:
                value = "ERROR_EVENT_UNSUPPORTED";
                break;
            case OnErrorEventListener.ERROR_EVENT_TIMED_OUT:
                value = "ERROR_EVENT_TIMED_OUT";
                break;
            default:
                value = "unKnow code error, maybe user custom errorCode";
                break;
        }
        if(bundle!=null){
            value += "," + bundle.toString();
        }
        PLog.e(EVENT_TAG_ERROR_EVENT, value);
    }

}
