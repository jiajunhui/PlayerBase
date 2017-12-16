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

import android.view.View;

import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.ViewType;

/**
 * Created by Taurus on 2016/8/29.
 */
public interface IPlayer extends IDecoder{

    /**
     * 使用接入的VideoView类型的播放组件（View的子类），如系统的VideoView或者三方的XXVideoView
     */
    int WIDGET_MODE_VIDEO_VIEW = 2;

    /**
     * 使用接入的MediaPlayer类型的播放组件，如系统的MediaPlayer或者三方的XXMediaPlayer
     * 一般此种类型的播放组件是只包含解码器不包含渲染视图，也就是说需要需要自己去关联一个渲染视图，{@link IRender}
     * SurfaceView或者TextureView，框架提供了设置渲染视图的方法，如果用户没有设置，框架会自动设置一个渲染视图。
     */
    int WIDGET_MODE_DECODER = 4;

    void rePlay(int msc);
    void setDataProvider(IDataProvider dataProvider);
    void updatePlayerType(int type);
    int getPlayerType();
    void setViewType(ViewType viewType);
    void setAspectRatio(AspectRatio aspectRatio);

    ViewType getViewType();
    AspectRatio getAspectRatio();

    View getRenderView();
}
