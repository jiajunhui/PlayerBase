package com.kk.taurus.playerbase.setting;

/**
 * ------------------------------------
 * Created by Taurus on 2016/8/2.
 * ------------------------------------
 */
public enum DecodeMode {
    SOFT("软解码"),
    MEDIA_PLAYER("原生解码"),
    HARD("硬解码"),
    EXO_PLAYER("ExoPlayer");

    String value;

    DecodeMode(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
