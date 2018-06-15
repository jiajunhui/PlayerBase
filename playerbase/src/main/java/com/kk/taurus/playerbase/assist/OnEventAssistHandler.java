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

package com.kk.taurus.playerbase.assist;

import android.os.Bundle;

/**
 *
 * Created by Taurus on 2018/5/21.
 *
 * This interface is used to handle the basic playback
 * operation event issued by the caller. Such as pause,
 * fast forward and other operations.
 *
 * @param <T> Play master controller, maybe AVPlayer or AssistPlay or other packaging classes.
 *
 */
public interface OnEventAssistHandler<T> {

    void onAssistHandle(T assist, int eventCode, Bundle bundle);

    void requestPause(T assist, Bundle bundle);
    void requestResume(T assist, Bundle bundle);
    void requestSeek(T assist, Bundle bundle);
    void requestStop(T assist, Bundle bundle);
    void requestReset(T assist, Bundle bundle);
    void requestRetry(T assist, Bundle bundle);
    void requestReplay(T assist, Bundle bundle);
    void requestPlayDataSource(T assist, Bundle bundle);

}
