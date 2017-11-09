package com.kk.taurus.playerbase.utils;

import android.os.Bundle;
import android.util.Log;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;

/**
 * Created by mtime on 2017/11/7.
 */

public class EventLog {

    private static final String TAG_PLAYER_EVENT = "PlayerEventLog";
    private static final String TAG_TIMER_COUNTER = "TimerEventLog";

    public static void onNotifyPlayerEvent(int eventCode, Bundle bundle){
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_ON_PLAYER_PREPARING:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_ON_PLAYER_PREPARING");
                break;
            /** when player ready to start*/
            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_ON_INTENT_TO_START");
                break;
            /** when player prepared*/
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PREPARED");
                break;
            /** when video info ready , such as video rate info and so on.*/
            case OnPlayerEventListener.EVENT_CODE_VIDEO_INFO_READY:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_VIDEO_INFO_READY");
                break;
            /** when player start render screen*/
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_RENDER_START");
                break;
            /** when the network is not good, start buffering*/
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_BUFFERING_START");
                break;
            /** buffering end*/
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_BUFFERING_END");
                break;
            /** when seek over call this event code*/
            case OnPlayerEventListener.EVENT_CODE_SEEK_COMPLETE:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_SEEK_COMPLETE");
                break;
            /** when setDataSource video complete*/
            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAY_COMPLETE");
                break;
            /** when player paused*/
            case OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAY_PAUSE");
                break;
            /** when player resumed*/
            case OnPlayerEventListener.EVENT_CODE_PLAY_RESUME:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAY_RESUME");
                break;
            /** when change video definition*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_CHANGE_DEFINITION:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_CHANGE_DEFINITION");
                break;
            /** when seek to */
            case OnPlayerEventListener.EVENT_CODE_PLAYER_SEEK_TO:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_SEEK_TO");
                break;
            /** when player set data*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE");
                break;
            /** when set video data*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_VIDEO_DATA:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_ON_SET_VIDEO_DATA");
                break;
            /** when set ad video data*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_AD_DATA:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_ON_SET_AD_DATA");
                break;
            /** when set play data include video data and ad data*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_PLAY_DATA:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_ON_SET_PLAY_DATA");
                break;
            /** when player stop*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_ON_STOP");
                break;
            /** when player destroy*/
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_ON_DESTROY");
                break;
            /** when ready to switch player type*/
            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_SWITCH_PLAYER_TYPE:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_ON_INTENT_TO_SWITCH_PLAYER_TYPE");
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_FULL_SCREEN:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_FULL_SCREEN");
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_QUIT_FULL_SCREEN:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_QUIT_FULL_SCREEN");
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS");
                break;
            case OnPlayerEventListener.EVENT_CODE_ON_DEFINITION_LIST_READY:
                Log.d(TAG_PLAYER_EVENT,"EVENT_CODE_ON_DEFINITION_LIST_READY");
                break;
            default:
                Log.d(TAG_PLAYER_EVENT,String.valueOf(eventCode));
                break;
        }
    }

    public static void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage){
        Log.d(TAG_TIMER_COUNTER,"curr = " + curr + " duration = " + duration + " bufferPercentagePos = " + bufferPercentage);
    }
    
}
