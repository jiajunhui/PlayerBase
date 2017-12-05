/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kk.taurus.ijkplayer.media;


public class Settings {

    private boolean enableBackgroundPlay;
    private int playerType = PV_PLAYER__IjkMediaPlayer;
    private boolean usingMediaCodec;
    private boolean usingMediaCodecAutoRotate;
    private boolean mediaCodecHandleResolutionChange;
    private boolean usingOpenSLES;
    private String pixelFormat;
    private boolean enableNoView;
    private boolean enableSurfaceView;
    private boolean enableTextureView;
    private boolean enableDetachedSurfaceTextureView;
    private boolean usingMediaDataSource;

    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public Settings() {
    }

    public Settings(int playerType, boolean usingMediaCodec, boolean enableSurfaceView, boolean enableTextureView) {
        this.playerType = playerType;
        this.usingMediaCodec = usingMediaCodec;
        this.enableSurfaceView = enableSurfaceView;
        this.enableTextureView = enableTextureView;
    }

    public boolean getEnableBackgroundPlay() {
        return enableBackgroundPlay;
    }

    public int getPlayer() {
        return playerType;
    }

    public boolean getUsingMediaCodec() {
        return usingMediaCodec;
    }

    public boolean getUsingMediaCodecAutoRotate() {
        return usingMediaCodecAutoRotate;
    }

    public boolean getMediaCodecHandleResolutionChange() {
        return mediaCodecHandleResolutionChange;
    }

    public boolean getUsingOpenSLES() {
        return usingOpenSLES;
    }

    public String getPixelFormat() {
        return pixelFormat;
    }

    public boolean getEnableNoView() {
        return enableNoView;
    }

    public boolean getEnableSurfaceView() {
        return enableSurfaceView;
    }

    public boolean getEnableTextureView() {
        return enableTextureView;
    }

    public boolean getEnableDetachedSurfaceTextureView() {
        return enableDetachedSurfaceTextureView;
    }

    public boolean getUsingMediaDataSource() {
        return usingMediaDataSource;
    }

    public void setEnableBackgroundPlay(boolean enableBackgroundPlay) {
        this.enableBackgroundPlay = enableBackgroundPlay;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public void setUsingMediaCodec(boolean usingMediaCodec) {
        this.usingMediaCodec = usingMediaCodec;
    }

    public void setUsingMediaCodecAutoRotate(boolean usingMediaCodecAutoRotate) {
        this.usingMediaCodecAutoRotate = usingMediaCodecAutoRotate;
    }

    public void setMediaCodecHandleResolutionChange(boolean mediaCodecHandleResolutionChange) {
        this.mediaCodecHandleResolutionChange = mediaCodecHandleResolutionChange;
    }

    public void setUsingOpenSLES(boolean usingOpenSLES) {
        this.usingOpenSLES = usingOpenSLES;
    }

    public void setPixelFormat(String pixelFormat) {
        this.pixelFormat = pixelFormat;
    }

    public void setEnableNoView(boolean enableNoView) {
        this.enableNoView = enableNoView;
    }

    public void setEnableSurfaceView(boolean enableSurfaceView) {
        this.enableSurfaceView = enableSurfaceView;
        this.enableTextureView = !this.enableSurfaceView;
    }

    public void setEnableTextureView(boolean enableTextureView) {
        this.enableTextureView = enableTextureView;
        this.enableSurfaceView = !this.enableTextureView;
    }

    public void setEnableDetachedSurfaceTextureView(boolean enableDetachedSurfaceTextureView) {
        this.enableDetachedSurfaceTextureView = enableDetachedSurfaceTextureView;
    }

    public void setUsingMediaDataSource(boolean usingMediaDataSource) {
        this.usingMediaDataSource = usingMediaDataSource;
    }

}
