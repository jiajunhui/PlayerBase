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
import com.kk.taurus.playerbase.cover.DefaultPlayerGestureOperationCover;
import com.kk.taurus.playerbase.inter.ICoverContainer;
import com.kk.taurus.playerbase.setting.BaseExtendEventBox;
import com.kk.taurus.playerbase.setting.InternalPlayerManager;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.kk.taurus.playerbase.widget.plan.IEventBinder;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.DecoderType;

/**
 *
 * Created by Taurus on 2017/3/28.
 *
 */

public class DefaultPlayer extends BasePlayer implements IEventBinder, InternalPlayerManager.OnInternalPlayerListener {

    public DefaultPlayer(Context context) {
        super(context);
    }

    public DefaultPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 该方法会返回一个View（仅当组件类型WIDGET_MODE为VideoView类型时）。
     *
     * 通过{@link InternalPlayerManager}统一控制。
     *
     * 通过{@link PlayerType}进行设置VideoView类型。
     * WIDGET_MODE为VideoView类型时，通过InternalPlayerManager会初始化你设置好的VideoView类型的对象，并返回给上层。
     *
     * 通过{@link DecoderType}进行设置Decoder类型。
     * WIDGET_MODE为DECODER类型时，InternalPlayerManager只是初始化了你设置好的解码器类型。并没有View返回。
     * @param context
     * @return
     */
    protected View getPlayerWidget(Context context) {
        InternalPlayerManager.get().updateWidgetMode(context,getWidgetMode());
        return InternalPlayerManager.get().getRenderView();
    }

    @Override
    protected void onDataSourceAvailable() {
        super.onDataSourceAvailable();
        InternalPlayerManager.get().setOnInternalPlayerListener(this);
    }

    /**
     * 该方法会返回一个用于处理cover放置规则的容器。
     *
     * 参见接口{@link ICoverContainer}
     *
     * 默认返回框架内部的cover容器处理器。{@link DefaultLevelCoverContainer}
     *
     * @param context
     * @return {@link ICoverContainer}
     */
    protected ICoverContainer getCoverContainer(Context context) {
        return new DefaultLevelCoverContainer(context);
    }

    /**
     * 该方法会返回一个用于处理扩展事件的盒子
     * （扩展事件盒子，用于处理与播放解码无关的且又需要的事件，比如网络变化事件、电池电量变化的事件等）
     * 默认返回框架内部的仅添加了网络变化事件监听的功能。{@link BaseExtendEventBox}
     * @return {@link BaseExtendEventBox}
     */
    protected BaseExtendEventBox getExtendEventBox() {
        return new BaseExtendEventBox(mAppContext,this);
    }

    /**
     * 该方法返回一个默认的手势处理器，用于处理诸如通过手势调节音量、亮度、快进等操作。
     * 具体的默认操作请参见{@link BaseGestureCallbackHandler}和{@link DefaultPlayerGestureOperationCover}
     * @return
     */
    protected BaseGestureCallbackHandler getGestureCallBackHandler() {
        return new BaseGestureCallbackHandler(this);
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
