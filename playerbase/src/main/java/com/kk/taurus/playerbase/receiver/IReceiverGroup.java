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

package com.kk.taurus.playerbase.receiver;

import java.util.Comparator;

/**
 * Created by Taurus on 2018/4/14.
 *
 * Used to manage receivers
 *
 */

public interface IReceiverGroup {

    /**
     * add a onReceiverGroupChangeListener listen Receiver item change.
     * @param onReceiverGroupChangeListener
     */
    void addOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener);

    /**
     * When you don't need onReceiverGroupChangeListener to remove it
     * @param onReceiverGroupChangeListener
     */
    void removeOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener);

    /**
     * add a receiver, you need put a unique key for this receiver.
     * @param key
     * @param receiver
     */
    void addReceiver(String key, IReceiver receiver);

    /**
     * remove a receiver by key.
     * @param key
     */
    void removeReceiver(String key);

    /**
     * sort group data
     * @param comparator
     */
    void sort(Comparator<IReceiver> comparator);

    /**
     * loop all receivers
     * @param onLoopListener
     */
    void forEach(OnLoopListener onLoopListener);

    /**
     * loop all receivers by a receiver filter.
     * @param filter
     * @param onLoopListener
     */
    void forEach(OnReceiverFilter filter, OnLoopListener onLoopListener);

    /**
     * get receiver by key.
     * @param key
     * @param <T>
     * @return
     */
    <T extends IReceiver> T getReceiver(String key);

    /**
     * get the ReceiverGroup group value.
     * @return
     */
    GroupValue getGroupValue();

    /**
     * clean receivers.
     */
    void clearReceivers();

    interface OnReceiverGroupChangeListener{
        void onReceiverAdd(String key, IReceiver receiver);
        void onReceiverRemove(String key, IReceiver receiver);
    }

    interface OnLoopListener{
        void onEach(IReceiver receiver);
    }

    interface OnReceiverFilter{
        boolean filter(IReceiver receiver);
    }

    interface OnGroupValueUpdateListener{
        String[] filterKeys();
        void onValueUpdate(String key, Object value);
    }

}
