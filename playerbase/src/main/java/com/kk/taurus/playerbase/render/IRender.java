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
import com.kk.taurus.playerbase.widget.BaseVideoView;

/**
 * Created by Taurus on 2017/11/19.
 *
 * frame rendering view,
 * using the method can refer to BaseVideoView {@link BaseVideoView}
 *
 */

public interface IRender {

    //use TextureView for render
    int RENDER_TYPE_TEXTURE_VIEW = 0;

    //use SurfaceView for render
    int RENDER_TYPE_SURFACE_VIEW = 1;

    void setRenderCallback(IRenderCallback renderCallback);

    /**
     * update video rotation, such as some video maybe rotation 90 degree.
     * @param degree
     */
    void setVideoRotation(int degree);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    /**
     * update video show aspect ratio
     *
     * see also
     * {@link AspectRatio#AspectRatio_16_9}
     * {@link AspectRatio#AspectRatio_4_3}
     * {@link AspectRatio#AspectRatio_FIT_PARENT}
     * {@link AspectRatio#AspectRatio_FILL_PARENT}
     * {@link AspectRatio#AspectRatio_MATCH_PARENT}
     * {@link AspectRatio#AspectRatio_ORIGIN}
     *
     * @param aspectRatio
     */
    void updateAspectRatio(AspectRatio aspectRatio);

    /**
     * update video size ,width and height.
     * @param videoWidth
     * @param videoHeight
     */
    void updateVideoSize(int videoWidth, int videoHeight);

    View getRenderView();

    /**
     * release render,the render will become unavailable
     */
    void release();

    /**
     * render is released ?
     * @return
     */
    boolean isReleased();

    /**
     * IRenderHolder is responsible for associate the decoder with rendering views.
     *
     * see also
     * {@link RenderSurfaceView.InternalRenderHolder#bindPlayer(IPlayer)}
     * {@link RenderTextureView.InternalRenderHolder#bindPlayer(IPlayer)}
     *
     */
    interface IRenderHolder{
        void bindPlayer(IPlayer player);
    }

    /**
     *
     * see also
     * {@link RenderSurfaceView.IRenderCallback}
     * {@link RenderTextureView.IRenderCallback}
     *
     */
    interface IRenderCallback{
        void onSurfaceCreated(IRenderHolder renderHolder, int width, int height);
        void onSurfaceChanged(IRenderHolder renderHolder, int format, int width, int height);
        void onSurfaceDestroy(IRenderHolder renderHolder);
    }

}
