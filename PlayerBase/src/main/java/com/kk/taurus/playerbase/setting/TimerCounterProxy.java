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

package com.kk.taurus.playerbase.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.ITimerGetter;

import static com.kk.taurus.playerbase.inter.MSG.MSG_CODE_PLAYING;

/**
 * Created by Taurus on 2017/11/18.
 */

public class TimerCounterProxy {

    private ITimerGetter timerGetter;
    private OnTimerHandlerListener onTimerHandlerListener;
    private TimerData timerData;

    private TimerHandler mHandler = new TimerHandler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_PLAYING:
                    int curr = timerGetter.getTimerCurrentPosition();
                    int duration = timerGetter.getTimerDuration();
                    int bufferPercentage = timerGetter.getTimerBufferPercentage();
                    int bufferPos = bufferPercentage*duration/100;
                    notifyPlayTimerCounter(curr,duration,bufferPos);
                    if(duration > 0 && curr >=0){
                        sendPlayMsg();
                    }else{
                        removePlayMsg();
                    }
                    break;
            }
        }
    };

    private void notifyPlayTimerCounter(int curr, int duration, int bufferPos) {
        timerData = new TimerData();
        timerData.setCurrentPosition(curr);
        timerData.setDuration(duration);
        timerData.setBufferPosition(bufferPos);
        if(onTimerHandlerListener!=null){
            onTimerHandlerListener.onTimerCounter(timerData);
        }
    }

    public TimerCounterProxy(ITimerGetter timerGetter){
        this.timerGetter = timerGetter;
    }

    public void setOnTimerHandlerListener(OnTimerHandlerListener onTimerHandlerListener) {
        this.onTimerHandlerListener = onTimerHandlerListener;
    }

    public void sendPlayMsg() {
        removePlayMsg();
        mHandler.sendEmptyMessageDelayed(MSG_CODE_PLAYING,1000);
    }

    protected void startPlay(){
        removePlayMsg();
        mHandler.sendEmptyMessage(MSG_CODE_PLAYING);
    }

    public void removePlayMsg() {
        mHandler.removeMessages(MSG_CODE_PLAYING);
    }

    public void proxyPlayerEvent(int eventCode, Bundle bundle){
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                removePlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                startPlay();
                break;
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                startPlay();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAY_PAUSE:
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAY_RESUME:
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                sendPlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                removePlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
                removePlayMsg();
                break;
        }
    }

    public void proxyErrorEvent(int eventCode, Bundle bundle){

    }

    public interface OnTimerHandlerListener{
        void onTimerCounter(TimerData timerData);
    }

    private static class TimerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

}
