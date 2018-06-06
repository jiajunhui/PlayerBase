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

package com.kk.taurus.playerbase.receiver;

import android.os.Bundle;

import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;

public class StateGetterImpl implements StateGetter {

    private int mState = IPlayer.STATE_IDLE;

    private boolean isBuffering;

    public void proxyPlayerEvent(int eventCode, Bundle bundle){
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                mState = bundle==null?IPlayer.STATE_IDLE:bundle.getInt(EventKey.INT_DATA);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
                isBuffering = true;
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
                isBuffering = false;
                break;
        }
    }

    @Override
    public PlayerStateGetter getPlayerStateGetter() {
        return mInternalPlayerStateGetter;
    }

    private PlayerStateGetter mInternalPlayerStateGetter =
            new PlayerStateGetter() {
        @Override
        public int getState() {
            return mState;
        }
        @Override
        public boolean isBuffering() {
            return isBuffering;
        }
    };

}
