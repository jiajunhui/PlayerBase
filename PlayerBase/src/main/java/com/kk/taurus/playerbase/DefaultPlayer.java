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

package com.kk.taurus.playerbase;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.kk.taurus.playerbase.callback.BaseGestureCallbackHandler;
import com.kk.taurus.playerbase.cover.container.DefaultLevelCoverContainer;
import com.kk.taurus.playerbase.inter.ICoverContainer;
import com.kk.taurus.playerbase.setting.BaseExtendEventBox;
import com.kk.taurus.playerbase.setting.InternalPlayerManager;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.kk.taurus.playerbase.widget.plan.IEventBinder;

/**
 *
 * Created by Taurus on 2017/3/28.
 *
 */

public class DefaultPlayer extends BasePlayer implements IEventBinder, InternalPlayerManager.OnInternalPlayerListener {

    private final String TAG = "DefaultPlayer";
    private BaseExtendEventBox extendEventBox;

    public DefaultPlayer(Context context) {
        super(context);
    }

    public DefaultPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        extendEventBox = getExtendEventBox();
    }

    @Override
    protected View getPlayerWidget(Context context) {
        InternalPlayerManager.get().updateWidgetMode(context,getWidgetMode());
        return InternalPlayerManager.get().getRenderView();
    }

    @Override
    protected void onDataSourceAvailable() {
        super.onDataSourceAvailable();
        InternalPlayerManager.get().setOnInternalPlayerListener(this);
    }

    @Override
    protected ICoverContainer getCoverContainer(Context context) {
        return new DefaultLevelCoverContainer(context);
    }

    protected BaseExtendEventBox getExtendEventBox() {
        return new BaseExtendEventBox(mAppContext,this);
    }

    @Override
    protected BaseGestureCallbackHandler getGestureCallBackHandler() {
        return new BaseGestureCallbackHandler(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        if(extendEventBox!=null){
            extendEventBox.destroyExtendBox();
        }
    }

    @Override
    public void onBindPlayerEvent(int eventCode, Bundle bundle) {
        sendEvent(eventCode, bundle);
    }

    @Override
    public void onBindErrorEvent(int eventCode, Bundle bundle) {
        onErrorEvent(eventCode, bundle);
    }

    @Override
    public void onInternalPlayerEvent(int eventCode, Bundle bundle) {
        sendEvent(eventCode, bundle);
    }

    @Override
    public void onInternalErrorEvent(int errorCode, Bundle bundle) {
        onErrorEvent(errorCode, bundle);
    }
}
