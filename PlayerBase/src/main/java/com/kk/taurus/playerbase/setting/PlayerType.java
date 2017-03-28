package com.kk.taurus.playerbase.setting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2016/11/14.
 */

public class PlayerType {

    private Map<Integer,String> playerTypes;

    private static PlayerType instance;

    private PlayerType(){
        playerTypes = new HashMap<>();
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

    public PlayerType addPlayerType(int playerType,String playerPath){
        playerTypes.put(playerType,playerPath);
        return this;
    }

    public String getPlayerPath(int playerType){
        return playerTypes.get(playerType);
    }

}
