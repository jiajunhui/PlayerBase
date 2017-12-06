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

import com.kk.taurus.playerbase.callback.BaseGestureCallbackHandler;
import com.kk.taurus.playerbase.cover.container.DefaultLevelCoverContainer;
import com.kk.taurus.playerbase.cover.DefaultPlayerGestureOperationCover;
import com.kk.taurus.playerbase.inter.ICoverContainer;
import com.kk.taurus.playerbase.setting.BaseExtendEventBox;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.kk.taurus.playerbase.inter.IEventBinder;

/**
 *
 * Created by Taurus on 2017/3/28.
 *
 */

public class DefaultPlayer extends BasePlayer implements IEventBinder {

    public DefaultPlayer(Context context) {
        super(context);
    }

    public DefaultPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        return new BaseGestureCallbackHandler(getPlayerGestureListener());
    }

    @Override
    public void onBindPlayerEvent(int eventCode, Bundle bundle) {
        sendEvent(eventCode, bundle);
    }

    @Override
    public void onBindErrorEvent(int eventCode, Bundle bundle) {
        onErrorEvent(eventCode, bundle);
    }

}
