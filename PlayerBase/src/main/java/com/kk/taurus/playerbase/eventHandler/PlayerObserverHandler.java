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

package com.kk.taurus.playerbase.eventHandler;

import android.content.res.Configuration;
import android.os.Bundle;

import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;

/**
 * Created by Taurus on 2017/10/15.
 */

public class PlayerObserverHandler implements PlayerObserver {
    
    private BaseReceiverCollections mReceiverCollections;
    
    public PlayerObserverHandler(BaseReceiverCollections receiverCollections){
        this.mReceiverCollections = receiverCollections;
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyConfigurationChanged(newConfig);
                }
            }
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyPlayEvent(eventCode, bundle);
                }
            }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyErrorEvent(eventCode, bundle);
                }
            }
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
                }
            }
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyNetWorkConnected(networkType);
                }
            }
    }

    @Override
    public void onNotifyNetWorkError() {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyNetWorkError();
                }
            }
    }
    
}
