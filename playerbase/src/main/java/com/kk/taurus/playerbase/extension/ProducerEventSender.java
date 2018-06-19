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

package com.kk.taurus.playerbase.extension;

import android.os.Bundle;

import com.kk.taurus.playerbase.receiver.IReceiverGroup;

public final class ProducerEventSender implements ReceiverEventSender {
    
    private DelegateReceiverEventSender mEventSender;

    public ProducerEventSender(DelegateReceiverEventSender eventSender){
        this.mEventSender = eventSender;
    }

    @Override
    public void sendEvent(int eventCode, Bundle bundle) {
        sendEvent(eventCode, bundle, null);
    }

    @Override
    public void sendEvent(final int eventCode, final Bundle bundle, IReceiverGroup.OnReceiverFilter receiverFilter) {
        if(mEventSender==null)
            return;
        mEventSender.sendEvent(eventCode, bundle, receiverFilter);
    }

    @Override
    public void sendBoolean(String key, boolean value) {
        sendObject(key, value);
    }

    @Override
    public void sendInt(String key, int value) {
        sendObject(key, value);
    }

    @Override
    public void sendString(String key, String value) {
        sendObject(key, value);
    }

    @Override
    public void sendFloat(String key, float value) {
        sendObject(key, value);
    }

    @Override
    public void sendLong(String key, long value) {
        sendObject(key, value);
    }

    @Override
    public void sendDouble(String key, double value) {
        sendObject(key, value);
    }

    @Override
    public void sendObject(String key, Object value) {
        sendObject(key, value, null);
    }

    @Override
    public void sendBoolean(String key, boolean value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendInt(String key, int value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendString(String key, String value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendFloat(String key, float value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendLong(String key, long value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendDouble(String key, double value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        sendObject(key, value, receiverFilter);
    }

    @Override
    public void sendObject(String key, Object value, IReceiverGroup.OnReceiverFilter receiverFilter) {
        if(mEventSender==null)
            return;
        mEventSender.sendObject(key, value, receiverFilter);
    }
}
