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

package com.taurus.playerbaselibrary.cover;

import android.content.Context;
import android.os.Bundle;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultPlayerControllerCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;

/**
 * Created by Taurus on 2017/12/2.
 */

public class AppControllerCover extends DefaultPlayerControllerCover {

    public AppControllerCover(Context context) {
        super(context);
    }

    public AppControllerCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_ON_RECEIVER_COLLECTIONS_NEW_BIND:
                if(isPlaying()){
                    setPlayState(true);
                }
                break;
        }
    }

    @Override
    protected void switchControllerState() {
        if(isVisibilityGone()){
            setControllerState(true);
            setBottomContainerState(true);
        }else{
            setControllerState(false);
        }
        setTopContainerState(false);
    }
}
