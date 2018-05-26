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

package com.kk.taurus.playerbase.assist;

import android.os.Bundle;

import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.event.EventKey;

public class OnAVPlayerEventHandler extends BaseEventAssistHandler<AVPlayer> {

    @Override
    public void requestPause(AVPlayer player, Bundle bundle) {
        if(isInPlaybackState(player)){
            player.pause();
        }else{
            player.stop();
            player.reset();
        }
    }

    @Override
    public void requestResume(AVPlayer player, Bundle bundle) {
        if(isInPlaybackState(player)){
            player.resume();
        }else{
            player.rePlay(0);
        }
    }

    @Override
    public void requestSeek(AVPlayer player, Bundle bundle) {
        int pos = 0;
        if(bundle!=null){
            pos = bundle.getInt(EventKey.INT_DATA);
        }
        player.seekTo(pos);
    }

    @Override
    public void requestStop(AVPlayer player, Bundle bundle) {
        player.stop();
    }

    @Override
    public void requestReset(AVPlayer player, Bundle bundle) {
        player.reset();
    }

    @Override
    public void requestRetry(AVPlayer player, Bundle bundle) {
        int pos = 0;
        if(bundle!=null){
            pos = bundle.getInt(EventKey.INT_DATA);
        }
        player.rePlay(pos);
    }

    @Override
    public void requestReplay(AVPlayer player, Bundle bundle) {
        player.rePlay(0);
    }

    private boolean isInPlaybackState(AVPlayer player) {
        int state = player.getState();
        return state!= AVPlayer.STATE_END
                && state!= AVPlayer.STATE_ERROR
                && state!= AVPlayer.STATE_IDLE
                && state!= AVPlayer.STATE_INITIALIZED
                && state!= AVPlayer.STATE_STOPPED;
    }

}
