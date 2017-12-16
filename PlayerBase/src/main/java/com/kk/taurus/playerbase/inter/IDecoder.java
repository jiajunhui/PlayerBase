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

package com.kk.taurus.playerbase.inter;

import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * Created by Taurus on 2017/11/17.
 */

public interface IDecoder extends IPlayCommon{

    int STATUS_END = -2;
    int STATUS_ERROR = -1;
    int STATUS_IDLE = 0;
    int STATUS_INITIALIZED = 1;
    int STATUS_PREPARED = 2;
    int STATUS_STARTED = 3;
    int STATUS_PAUSED = 4;
    int STATUS_STOPPED = 5;
    int STATUS_PLAYBACK_COMPLETE = 6;

    int getVideoWidth();
    int getVideoHeight();
    void setVolume(float leftVolume, float rightVolume);
    void setDisplay(SurfaceHolder surfaceHolder);
    void setSurface(Surface surface);

}
