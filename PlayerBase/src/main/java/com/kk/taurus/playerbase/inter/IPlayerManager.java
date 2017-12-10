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
import android.view.View;

import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.util.List;

/**
 * Created by Taurus on 2017/12/10.
 *
 * 此接口方法主要用于管理{@link BasePlayer}，当多个容器调用InternalPlayerManager时，
 * 只允许当前宿主IPlayer进行操作，其它容器的调用均为非法。
 *
 */

public interface IPlayerManager {

    void attachPlayer(IPlayer host);
    void detachPlayer(IPlayer host);

    void rePlay(IPlayer host, int msc);
    void updatePlayerType(IPlayer host, int type);
    int getPlayerType(IPlayer host);
    void setViewType(IPlayer host, ViewType viewType);
    void setAspectRatio(IPlayer host, AspectRatio aspectRatio);

    ViewType getViewType(IPlayer host);
    AspectRatio getAspectRatio(IPlayer host);

    View getRenderView(IPlayer host);

    void setDataSource(IPlayer host, VideoData data);
    void start(IPlayer host);
    void start(IPlayer host, int msc);
    void pause(IPlayer host);
    void resume(IPlayer host);
    void seekTo(IPlayer host, int msc);
    void stop(IPlayer host);
    void reset(IPlayer host);
    boolean isPlaying(IPlayer host);
    int getCurrentPosition(IPlayer host);
    int getDuration(IPlayer host);
    int getBufferPercentage(IPlayer host);
    int getAudioSessionId(IPlayer host);
    int getStatus(IPlayer host);
    int getVideoWidth(IPlayer host);
    int getVideoHeight(IPlayer host);
    void setVolume(IPlayer host, float leftVolume, float rightVolume);
    void setDecodeMode(IPlayer host, DecodeMode decodeMode);
    DecodeMode getDecodeMode(IPlayer host);
    void setDisplay(IPlayer host, SurfaceHolder surfaceHolder);
    void setSurface(IPlayer host, Surface surface);
    /** get current playing video definition*/
    Rate getCurrentDefinition(IPlayer host);
    /** get current playing data source all definitions*/
    List<Rate> getVideoDefinitions(IPlayer host);
    /** change playing video definition*/
    void changeVideoDefinition(IPlayer host, Rate rate);
}
