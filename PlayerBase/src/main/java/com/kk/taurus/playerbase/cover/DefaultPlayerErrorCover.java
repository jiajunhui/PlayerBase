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
import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;

/**
 * Created by Taurus on 2017/3/27.
 */

public class DefaultPlayerErrorCover extends BasePlayerErrorCover {
    public DefaultPlayerErrorCover(Context context) {
        super(context);
    }

    public DefaultPlayerErrorCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_player_error_state_cover,null);
    }

    @Override
    protected void findView() {
        super.findView();
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRetry();
            }
        });
    }

    private void onClickRetry() {
        if(player==null)
            return;
        rePlay(0);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        handlePlayEvent(eventCode, bundle);
    }

    protected void handlePlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                setErrorState(false);
                break;
        }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        super.onNotifyErrorEvent(eventCode, bundle);
        handleErrorEvent(eventCode, bundle);
    }

    protected void handleErrorEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnErrorListener.ERROR_CODE_COMMON:
                setErrorState(true);
                break;
        }
    }

    @Override
    public void setErrorState(boolean state) {
        super.setErrorState(state);
        notifyCoverEvent(state? OnCoverEventListener.EVENT_CODE_ON_PLAYER_ERROR_SHOW
                :OnCoverEventListener.EVENT_CODE_ON_PLAYER_ERROR_HIDDEN,null);
    }
}
