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

package com.kk.taurus.playerbase.event;

import android.os.Bundle;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.receiver.IReceiverGroup;

/**
 * Created by Taurus on 2018/4/15.
 */

public interface IEventDispatcher {
    void dispatchPlayEvent(int eventCode, Bundle bundle);
    void dispatchErrorEvent(int eventCode, Bundle bundle);
    void dispatchReceiverEvent(int eventCode, Bundle bundle);
    void dispatchReceiverEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter);
    void dispatchProducerEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter);
    void dispatchProducerData(String key, Object data, IReceiverGroup.OnReceiverFilter onReceiverFilter);


    void dispatchTouchEventOnSingleTabUp(MotionEvent event);
    void dispatchTouchEventOnLongPress(MotionEvent event);
    void dispatchTouchEventOnDoubleTabUp(MotionEvent event);
    void dispatchTouchEventOnDown(MotionEvent event);
    void dispatchTouchEventOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    void dispatchTouchEventOnEndGesture();
}
