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

import android.os.Bundle;

public interface ICoverHandle {

    void requestPause(Bundle bundle);
    void requestResume(Bundle bundle);
    void requestSeek(Bundle bundle);
    void requestStop(Bundle bundle);
    void requestReset(Bundle bundle);
    void requestRetry(Bundle bundle);
    void requestReplay(Bundle bundle);
    void requestPlayDataSource(Bundle bundle);

    void requestNotifyTimer();
    void requestStopTimer();

}
