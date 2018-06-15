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

package com.kk.taurus.playerbase.player;

import com.kk.taurus.playerbase.AVPlayer;

/**
 * Created by Taurus on 2018/4/15.
 *
 * in AVPlayer default open timer proxy, you can use update callback to refresh UI.
 * if you close timer proxy{@link AVPlayer#setUseTimerProxy(boolean)},
 * you will not receive this timer update callback.
 * if timer open , the call back called per second.
 * in some scene, you can close it to improve battery performance.
 *
 */
public interface OnTimerUpdateListener {
    void onTimerUpdate(int curr, int duration, int bufferPercentage);
}
