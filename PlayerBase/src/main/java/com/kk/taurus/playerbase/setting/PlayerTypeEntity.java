package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by Taurus on 2017/3/30.
 */

public class PlayerTypeEntity implements Serializable {
    private String playerName;
    private String playerClassPath;

    public PlayerTypeEntity(String playerName, String playerClassPath) {
        this.playerName = playerName;
        this.playerClassPath = playerClassPath;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerClassPath() {
        return playerClassPath;
    }

    public void setPlayerClassPath(String playerClassPath) {
        this.playerClassPath = playerClassPath;
    }
}
