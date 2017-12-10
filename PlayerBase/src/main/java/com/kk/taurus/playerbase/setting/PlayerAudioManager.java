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

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Taurus on 2017/12/8.
 *
 * 播放器音量模式管理。包含静音的设置，音量的恢复。
 *
 */

public class PlayerAudioManager {

    private AudioManager mAudioManager;
    private static PlayerAudioManager instance;
    private PlayerAudioManager(){}

    public static PlayerAudioManager get(){
        if(null==instance){
            synchronized (PlayerAudioManager.class){
                if(null==instance){
                    instance = new PlayerAudioManager();
                }
            }
        }
        return instance;
    }

    private boolean silent = false;

    private int mStreamMusicVolume;

    public void init(Context context){
        if(mAudioManager==null){
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
    }

    private int requestAudioFocus(AudioManager.OnAudioFocusChangeListener l, int streamType, int durationHint) {
        return mAudioManager.requestAudioFocus(l, streamType, durationHint);
    }

    private int abandonAudioFocus(AudioManager.OnAudioFocusChangeListener l) {
        return mAudioManager.abandonAudioFocus(l);
    }

    public int getStreamMaxVolume(int streamType) {
        return mAudioManager.getStreamMaxVolume(streamType);
    }

    private int getStreamVolume(int streamType) {
        return mAudioManager.getStreamVolume(streamType);
    }

    private int getStreamMusicVolume(){
        return getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void setStreamVolume(int streamType, int index, int flags) {
        mAudioManager.setStreamVolume(streamType, index, flags);
    }

    private void closeVolume(){
        setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
    }

    public void recoveryVolume(boolean requestAudioFocus){
        if(requestAudioFocus){
            requestAudioFocus();
        }
        setStreamVolume(AudioManager.STREAM_MUSIC,mStreamMusicVolume,0);
    }

    public void setVolumeMode(boolean silent){
        boolean change = this.silent!=silent;
        this.silent = silent;
        if(!change)
            return;
        if(silent){
            mStreamMusicVolume = getStreamMusicVolume();
            abandonAudioFocus(onAudioFocusChangeListener);
            closeVolume();
        }else{
            recoveryVolume(true);
        }
    }

    private void requestAudioFocus(){
        requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

        }
    };

}
