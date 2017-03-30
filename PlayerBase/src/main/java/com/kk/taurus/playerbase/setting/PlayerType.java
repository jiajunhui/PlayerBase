package com.kk.taurus.playerbase.setting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2016/11/14.
 */

public class PlayerType {

    private Map<Integer,PlayerTypeEntity> playerTypes;

    private int defaultPlayerType;

    private static PlayerType instance;

    private PlayerType(){
        playerTypes = new HashMap<>();
        addPlayerType(0,new PlayerTypeEntity("原生解码","com.kk.taurus.playerbase.player.MediaSinglePlayer"));
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
