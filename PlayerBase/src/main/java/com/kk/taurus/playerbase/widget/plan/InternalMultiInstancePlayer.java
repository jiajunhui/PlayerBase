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

package com.kk.taurus.playerbase.widget.plan;

import android.content.Context;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.inter.IPlayCommon;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.setting.ViewType;

import java.util.List;

/**
 * Created by mtime on 2017/12/13.
 */

public class InternalMultiInstancePlayer implements IPlayer , OnPlayerEventListener, OnErrorListener{

    private Context mContext;
    private IPlayCommon mPlayerCore;

    private int mWidgetMode;

    private OnInternalInstanceListener onInternalInstanceListener;

    public InternalMultiInstancePlayer(Context context, int widgetMode, int type){
        this.mContext = context;
        this.mWidgetMode = widgetMode;
        initPlayerCore(context, type);
    }

    public void setOnInternalInstanceListener(OnInternalInstanceListener onInternalInstanceListener) {
        this.onInternalInstanceListener = onInternalInstanceListener;
    }

    public View getPlayerWidget(){
        if(mPlayerCore instanceof MixRenderWidget){
            return (View) mPlayerCore;
        }
        return null;
    }

    private void initPlayerCore(Context context, int type) {
        destroy();
        if(mWidgetMode==WIDGET_MODE_DECODER){
            mPlayerCore = new MixMediaPlayer(context, type);
            ((MixMediaPlayer)mPlayerCore).setOnPlayerEventListener(this);
            ((MixMediaPlayer)mPlayerCore).setOnErrorListener(this);
        }else if(mWidgetMode==WIDGET_MODE_VIDEO_VIEW){
            mPlayerCore = new MixRenderWidget(context, type);
            ((MixRenderWidget)mPlayerCore).setOnPlayerEventListener(this);
            ((MixRenderWidget)mPlayerCore).setOnErrorListener(this);
        }else{
            throw new RuntimeException("please init correct widget mode !");
        }
    }

    public void updateWidget(int widgetMode, int type){
        this.mWidgetMode = widgetMode;
        initPlayerCore(mContext, type);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        if(onInternalInstanceListener!=null){
            onInternalInstanceListener.onInternalPlayerEvent(eventCode, bundle);
        }
    }

    @Override
    public void onError(int errorCode, Bundle bundle) {
        if(onInternalInstanceListener!=null){
            onInternalInstanceListener.onInternalErrorEvent(errorCode, bundle);
        }
    }

    private boolean isAvailablePlayCore(){
        return mPlayerCore!=null;
    }

    @Override
    public void setDataSource(VideoData data) {
        if(isAvailablePlayCore()){
            mPlayerCore.setDataSource(data);
        }
    }

    @Override
    public void rePlay(int msc) {
        if(mPlayerCore instanceof MixMediaPlayer){
            ((MixMediaPlayer) mPlayerCore).rePlay(msc);
        }else if(mPlayerCore instanceof MixRenderWidget){
            ((MixRenderWidget) mPlayerCore).rePlay(msc);
        }
    }

    @Override
    public void setDataProvider(IDataProvider dataProvider) {
        if(mPlayerCore instanceof MixMediaPlayer){
            ((MixMediaPlayer) mPlayerCore).setDataProvider(dataProvider);
        }else if(mPlayerCore instanceof MixRenderWidget){
            ((MixRenderWidget) mPlayerCore).setDataProvider(dataProvider);
        }
    }

    @Override
    public void updatePlayerType(int type) {
        if(mPlayerCore instanceof MixMediaPlayer){
            ((MixMediaPlayer) mPlayerCore).setDecoderType(type);
        }else if(mPlayerCore instanceof MixRenderWidget){
            ((MixRenderWidget) mPlayerCore).setRenderType(type);
        }
    }

    @Override
    public int getPlayerType() {
        if(mPlayerCore instanceof MixMediaPlayer){
            return ((MixMediaPlayer) mPlayerCore).getDecoderType();
        }else if(mPlayerCore instanceof MixRenderWidget){
            return ((MixRenderWidget) mPlayerCore).getRenderType();
        }
        return 0;
    }

    @Override
    public void setViewType(ViewType viewType) {
        if(mPlayerCore instanceof MixRenderWidget){
            ((MixRenderWidget) mPlayerCore).setViewType(viewType);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(mPlayerCore instanceof MixRenderWidget){
            ((MixRenderWidget) mPlayerCore).setAspectRatio(aspectRatio);
        }
    }

    @Override
    public ViewType getViewType() {
        if(mPlayerCore instanceof MixRenderWidget){
            return ((MixRenderWidget) mPlayerCore).getViewType();
        }
        return null;
    }

    @Override
    public AspectRatio getAspectRatio() {
        if(mPlayerCore instanceof MixRenderWidget){
            return ((MixRenderWidget) mPlayerCore).getAspectRatio();
        }
        return null;
    }

    @Override
    public View getRenderView() {
        if(mPlayerCore instanceof MixRenderWidget){
            return ((MixRenderWidget) mPlayerCore).getRenderView();
        }
        return null;
    }

    @Override
    public void start() {
        if(isAvailablePlayCore()){
            mPlayerCore.start();
        }
    }

    @Override
    public void start(int msc) {
        if(isAvailablePlayCore()){
            mPlayerCore.start(msc);
        }
    }

    @Override
    public void pause() {
        if(isAvailablePlayCore()){
            mPlayerCore.pause();
        }
    }

    @Override
    public void resume() {
        if(isAvailablePlayCore()){
            mPlayerCore.resume();
        }
    }

    @Override
    public void seekTo(int msc) {
        if(isAvailablePlayCore()){
            mPlayerCore.seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(isAvailablePlayCore()){
            mPlayerCore.stop();
        }
    }

    @Override
    public void reset() {
        if(isAvailablePlayCore()){
            mPlayerCore.reset();
        }
    }

    @Override
    public boolean isPlaying() {
        if(isAvailablePlayCore()){
            return mPlayerCore.isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getAudioSessionId() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getAudioSessionId();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getStatus();
        }
        return 0;
    }

    @Override
    public int getVideoWidth() {
        if(mPlayerCore instanceof MixMediaPlayer){
            return ((MixMediaPlayer) mPlayerCore).getVideoWidth();
        }
        return 0;
    }

    @Override
    public int getVideoHeight() {
        if(mPlayerCore instanceof MixMediaPlayer){
            return ((MixMediaPlayer) mPlayerCore).getVideoHeight();
        }
        return 0;
    }

    @Override
    public void setDecodeMode(DecodeMode decodeMode) {
        if(isAvailablePlayCore()){
            mPlayerCore.setDecodeMode(decodeMode);
        }
    }

    @Override
    public DecodeMode getDecodeMode() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getDecodeMode();
        }
        return null;
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        if(mPlayerCore instanceof MixMediaPlayer){
            ((MixMediaPlayer) mPlayerCore).setVolume(leftVolume, rightVolume);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(mPlayerCore instanceof MixMediaPlayer){
            ((MixMediaPlayer) mPlayerCore).setDisplay(surfaceHolder);
        }
    }

    @Override
    public void setSurface(Surface surface) {
        if(mPlayerCore instanceof MixMediaPlayer){
            ((MixMediaPlayer) mPlayerCore).setSurface(surface);
        }
    }

    @Override
    public Rate getCurrentDefinition() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(isAvailablePlayCore()){
            return mPlayerCore.getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(isAvailablePlayCore()){
            mPlayerCore.changeVideoDefinition(rate);
        }
    }

    @Override
    public void destroy() {
        if(isAvailablePlayCore()){
            mPlayerCore.destroy();
        }
    }

    public interface OnInternalInstanceListener{
        void onInternalPlayerEvent(int eventCode, Bundle bundle);
        void onInternalErrorEvent(int eventCode, Bundle bundle);
    }

}
