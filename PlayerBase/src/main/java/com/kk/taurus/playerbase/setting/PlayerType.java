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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2016/11/14.
 */

public class PlayerType {

    public static final int TYPE_PLAYER_DEFAULT = 0;

    private Map<Integer,PlayerTypeEntity> playerTypes;

    private int defaultPlayerType;

    private static PlayerType instance;

    private PlayerType(){
        playerTypes = new HashMap<>();
//        addPlayerType(TYPE_PLAYER_DEFAULT,new PlayerTypeEntity("原生解码","com.kk.taurus.playerbase.player.MediaSinglePlayer"));
        addPlayerType(TYPE_PLAYER_DEFAULT,new PlayerTypeEntity("原生解码","com.kk.taurus.playerbase.player.DefaultSinglePlayer"));
    }

    public static PlayerType getInstance(){
        if(null==instance){
            synchronized (PlayerType.class){
                if(null==instance){
                    instance = new PlayerType();
                }
            }
        }
        return instance;
    }

    public PlayerType setDefaultPlayerType(int playerType){
        this.defaultPlayerType = playerType;
        return this;
    }

    public int getDefaultPlayerType() {
        return defaultPlayerType;
    }

    public PlayerType addPlayerType(int playerType, PlayerTypeEntity entity){
        playerTypes.put(playerType,entity);
        return this;
    }

    public Map<Integer,PlayerTypeEntity> getPlayerTypes(){
        return playerTypes;
    }

    public String getPlayerPath(int playerType){
        PlayerTypeEntity entity = playerTypes.get(playerType);
        if(entity==null)
            return null;
        return entity.getPlayerClassPath();
    }

}
