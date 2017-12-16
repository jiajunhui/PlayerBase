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

package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BasePlayerLoadingCover;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.utils.PLog;

/**
 * Created by Taurus on 2017/3/25.
 */

public class DefaultPlayerLoadingCover extends BasePlayerLoadingCover {

    private final String TAG = "_LoadingCover";

    public DefaultPlayerLoadingCover(Context context) {
        super(context);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_player_loading_cover,null);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
            case IDataProvider.EVENT_CODE_START_HANDLE_SOURCE_DATA:
                setLoadingState(true);
                break;
            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START:
                PLog.d(TAG,"on intent to start......");
                setLoadingState(true);
                break;
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                PLog.d(TAG,"on render start......");
                setLoadingState(false);
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                PLog.d(TAG,"buffering start......");
                setLoadingState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                PLog.d(TAG,"buffering end......");
                setLoadingState(false);
                break;

            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                PLog.d(TAG,"on stopped......");
                setLoadingState(false);
                break;

            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_SWITCH_PLAYER_TYPE:
                setLoadingState(false);
                break;
        }
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        super.onCoverEvent(eventCode, bundle);
        switch (eventCode){
            case OnCoverEventListener.EVENT_CODE_ON_PLAYER_CONTROLLER_SHOW:
                PLog.d(TAG,"controller show......");
                break;
            case OnCoverEventListener.EVENT_CODE_ON_PLAYER_CONTROLLER_HIDDEN:
                PLog.d(TAG,"controller hidden......");
                break;
        }
    }
}
