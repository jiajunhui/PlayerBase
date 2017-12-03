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

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.VideoCacheProxy;
import com.kk.taurus.playerbase.inter.ITimerGetter;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.TimerCounterProxy;
import com.kk.taurus.playerbase.setting.TimerData;
import com.kk.taurus.playerbase.setting.VideoData;

/**
 * Created by mtime on 2017/11/17.
 */

public abstract class BaseMediaPlayer implements IDecoder ,IEventBinder, ITimerGetter, TimerCounterProxy.OnTimerHandlerListener {

    protected Context mContext;
    protected BaseDecoder mDecoder;
    protected VideoData mDataSource;
    private DecodeMode mDecodeMode;
    private int mDecoderType;
    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorListener mOnErrorListener;

    private TimerCounterProxy timerCounterProxy;
    private Bundle mBundle;

    public Bundle getBundle(){
        if(mBundle==null){
            mBundle = new Bundle();
        }
        mBundle.clear();
        return mBundle;
    }

    public BaseMediaPlayer(Context context){
        this.mContext = context;
        mDecoderType = DecoderType.getInstance().getDefaultPlayerType();
        mDecoder = initDecoder(context);
        timerCounterProxy = new TimerCounterProxy(this);
        timerCounterProxy.setOnTimerHandlerListener(this);
    }

    protected abstract BaseDecoder initDecoder(Context context);

    @Override
    public void setDataSource(VideoData data) {
        if(VideoCacheProxy.get().isVideoCacheOpen()){
            String sourceUrl = data.getData();
            data.setData(VideoCacheProxy.get().proxyVideoUrl(mContext,sourceUrl));
        }
        this.mDataSource = data;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    @Override
    public void onBindPlayerEvent(int eventCode, Bundle bundle) {
        timerCounterProxy.proxyPlayerEvent(eventCode, bundle);
        if(mOnPlayerEventListener!=null){
            mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
        }
    }

    @Override
    public void onBindErrorEvent(int eventCode, Bundle bundle) {
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(eventCode, bundle);
        }
    }

    @Override
    public int getTimerCurrentPosition() {
        return getCurrentPosition();
    }

    @Override
    public int getTimerDuration() {
        return getDuration();
    }

    @Override
    public int getTimerBufferPercentage() {
        return getBufferPercentage();
    }

    public void setDecoderType(int decoderType){
        boolean needUpdateDecoder = mDecoderType!=decoderType;
        this.mDecoderType = decoderType;
        if(needUpdateDecoder){
            initDecoder(mContext);
        }
    }

    public int getDecoderType(){
        return mDecoderType;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public void setDecodeMode(DecodeMode mDecodeMode) {
        this.mDecodeMode = mDecodeMode;
    }

    @Override
    public void onTimerCounter(TimerData timerData) {
        Bundle bundle = getBundle();
        bundle.putInt(ITimerGetter.KEY_TIMER_CURRENT_POSITION,timerData.getCurrentPosition());
        bundle.putInt(ITimerGetter.KEY_TIMER_DURATION,timerData.getDuration());
        bundle.putInt(ITimerGetter.KEY_TIMER_BUFFER_POSITION,timerData.getBufferPosition());
        onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_PLAYER_TIMER_UPDATE,bundle);
    }
}
