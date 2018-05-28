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

package com.kk.taurus.playerbase.widget;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.render.IRender;

/**
 * Created by Taurus on 2018/3/17.
 */

public interface IVideoView {

    void setDataSource(DataSource dataSource);

    void setRenderType(int renderType);
    void setAspectRatio(AspectRatio aspectRatio);
    boolean switchDecoder(int decoderPlanId);

    void setVolume(float left, float right);
    void setSpeed(float speed);

    IRender getRender();

    boolean isInPlaybackState();
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getAudioSessionId();
    int getBufferPercentage();
    int getState();

    void start();
    void start(int msc);
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void stopPlayback();

}
