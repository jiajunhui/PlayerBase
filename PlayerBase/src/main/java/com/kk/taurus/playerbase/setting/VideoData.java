package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by Taurus on 2017/3/25.
 */

public class VideoData implements Serializable {

    private String data;
    private String name;
    private int playerType;
    private Rate rate;
    private int startPos;

    public VideoData(String data) {
        this.data = data;
    }

    public VideoData(String data, Rate rate) {
        this.data = data;
        this.rate = rate;
    }

    public VideoData(String data, int playerType, Rate rate) {
        this.data = data;
        this.playerType = playerType;
        this.rate = rate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }
}
