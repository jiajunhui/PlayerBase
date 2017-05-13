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

import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.Rate;

import java.util.List;

/**
 * Created by Taurus on 2017/4/20.
 */

public interface IPlayerCoverHandle {
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void rePlay(int msc);
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getBufferPercentage();
    int getStatus();
    Rate getCurrentDefinition();
    List<Rate> getVideoDefinitions();
    void changeVideoDefinition(Rate rate);
    void setAspectRatio(AspectRatio aspectRatio);
    void sendPlayMsg();
    void removePlayMsg();
}
