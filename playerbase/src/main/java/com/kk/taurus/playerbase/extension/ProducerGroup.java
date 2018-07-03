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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 */
public final class ProducerGroup implements IProducerGroup {

    private ReceiverEventSender mEventSender;

    private List<BaseEventProducer> mEventProducers;

    public ProducerGroup(ReceiverEventSender eventSender){
        this.mEventSender = eventSender;
        mEventProducers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addEventProducer(BaseEventProducer eventProducer) {
        if(!mEventProducers.contains(eventProducer)){
            eventProducer.attachSender(mEventSender);
            mEventProducers.add(eventProducer);
            eventProducer.onAdded();
        }
    }

    @Override
    public boolean removeEventProducer(BaseEventProducer eventProducer) {
        boolean remove = mEventProducers.remove(eventProducer);
        if(eventProducer!=null){
            eventProducer.onRemoved();
            eventProducer.attachSender(null);
        }
        return remove;
    }

    @Override
    public void destroy() {
        for(BaseEventProducer eventProducer : mEventProducers){
            eventProducer.onRemoved();
            eventProducer.destroy();
            eventProducer.attachSender(null);
        }
        mEventProducers.clear();
    }
}
