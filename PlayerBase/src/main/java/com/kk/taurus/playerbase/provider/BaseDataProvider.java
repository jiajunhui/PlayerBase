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

package com.kk.taurus.playerbase.provider;

import android.os.Bundle;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.inter.IEventBinder;

/**
 * Created by mtime on 2017/10/19.
 */

public abstract class BaseDataProvider implements IDataProvider {

    protected OnProviderListener mOnProviderListener;
    private IEventBinder eventBinder;

    @Override
    public void setEventBinder(IEventBinder eventBinder) {
        this.eventBinder = eventBinder;
    }

    @Override
    public void handleSourceData(VideoData data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_SERIALIZABLE_DATA,data);
        sendPlayerEvent(EVENT_CODE_START_HANDLE_SOURCE_DATA,bundle);
    }

    @Override
    public void setOnProviderListener(OnProviderListener onProviderListener) {
        this.mOnProviderListener = onProviderListener;
    }

    protected void sendPlayerEvent(int eventCode, Bundle bundle){
        if(eventBinder!=null){
            eventBinder.onBindPlayerEvent(eventCode, bundle);
        }
    }

    protected void sendErrorEvent(int errorCode, Bundle bundle){
        if(eventBinder!=null){
            eventBinder.onBindErrorEvent(errorCode, bundle);
        }
    }

}
