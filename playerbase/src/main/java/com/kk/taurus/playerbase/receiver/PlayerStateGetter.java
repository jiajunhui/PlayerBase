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

import com.kk.taurus.playerbase.player.IPlayer;

/**
 *
 * player state getter for Receivers.
 *
 * Created by Taurus on 2018/6/8.
 *
 */
public interface PlayerStateGetter {

    /**
     * get player state code.
     *
     * See also
     * {@link IPlayer#STATE_END}
     * {@link IPlayer#STATE_ERROR}
     * {@link IPlayer#STATE_IDLE}
     * {@link IPlayer#STATE_INITIALIZED}
     * {@link IPlayer#STATE_PREPARED}
     * {@link IPlayer#STATE_STARTED}
     * {@link IPlayer#STATE_PAUSED}
     * {@link IPlayer#STATE_STOPPED}
     * {@link IPlayer#STATE_PLAYBACK_COMPLETE}
     *
     * @return state
     */
    int getState();

    /**
     * get player current play progress.
     * @return
     */
    int getCurrentPosition();

    /**
     * get video duration
     * @return
     */
    int getDuration();

    /**
     * get player buffering percentage.
     * @return
     */
    int getBufferPercentage();

    /**
     * the player is in buffering.
     * @return
     */
    boolean isBuffering();

}
