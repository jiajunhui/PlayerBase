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

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 * The producer of the event. It is usually added by the users themselves,
 * such as the system's power change events or network change events.
 * The framework adds the network change event producer by default.
 *
 */
public interface EventProducer {

    void onAdded();

    void onRemoved();

    ReceiverEventSender getSender();

    void destroy();

}
