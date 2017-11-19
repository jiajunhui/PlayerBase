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
import com.kk.taurus.playerbase.widget.plan.IDecoder;

/**
 * Created by Taurus on 2016/8/29.
 */
public interface IPlayer extends IDecoder{

    int WIDGET_MODE_VIDEO_VIEW = 2;
    int WIDGET_MODE_DECODER = 4;

    void setDataProvider(IDataProvider dataProvider);
    void updatePlayerType(int type);
    void setViewType(ViewType viewType);
    void setAspectRatio(AspectRatio aspectRatio);
    View getRenderView();
}
