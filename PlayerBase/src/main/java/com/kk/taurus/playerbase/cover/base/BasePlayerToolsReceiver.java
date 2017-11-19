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

package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.os.Bundle;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IPlayerCoverHandle;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.Rate;

import java.util.List;

/**
 * Created by Taurus on 2017/4/20.
 */

public abstract class BasePlayerToolsReceiver extends BaseEventReceiver implements IPlayerCoverHandle{

    public BasePlayerToolsReceiver(Context context) {
        super(context);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
            case OnPlayerEventListener.EVENT_CODE_PLAYER_CONTAINER_ON_DESTROY:
                onDestroy();
                player = null;
                break;
        }
    }

    //--------------------------------------------------------------------------------------------

    //#####################################################################################
    // for player handle
    //#####################################################################################

    @Override
    public void pause() {
        if(getPlayer()!=null){
            getPlayer().pause();
        }
    }

    @Override
    public void resume() {
        if(getPlayer()!=null){
            getPlayer().resume();
        }
    }

    @Override
    public void seekTo(int msc) {
        if(getPlayer()!=null){
            getPlayer().seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(getPlayer()!=null){
            getPlayer().stop();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(getPlayer()!=null){
            getPlayer().rePlay(msc);
        }
    }

    @Override
    public boolean isPlaying() {
        if(getPlayer()!=null){
            return getPlayer().isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(getPlayer()!=null){
            return getPlayer().getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(getPlayer()!=null){
            return getPlayer().getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(getPlayer()!=null){
            return getPlayer().getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(getPlayer()!=null){
            return getPlayer().getStatus();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(getPlayer()!=null){
            return getPlayer().getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(getPlayer()!=null){
            return getPlayer().getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(getPlayer()!=null){
            getPlayer().changeVideoDefinition(rate);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(getPlayer()!=null){
            getPlayer().setAspectRatio(aspectRatio);
        }
    }

}
