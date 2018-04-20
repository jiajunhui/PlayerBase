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

package com.kk.taurus.playerbase.render;

import android.view.View;

import com.kk.taurus.playerbase.player.IPlayer;

/**
 * Created by Taurus on 2017/11/19.
 */

public interface IRender {

    void setRenderCallback(IRenderCallback renderCallback);

    void setVideoRotation(int degree);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    void updateAspectRatio(AspectRatio aspectRatio);

    void updateVideoSize(int videoWidth, int videoHeight);

    View getRenderView();

    interface IRenderHolder{
        void bindPlayer(IPlayer player);
    }

    interface IRenderCallback{
        void onSurfaceCreated(IRenderHolder renderHolder, int width, int height);
        void onSurfaceChanged(IRenderHolder renderHolder, int format, int width, int height);
        void onSurfaceDestroy(IRenderHolder renderHolder);
    }

}
