package com.kk.taurus.playerbase.entity;

import java.io.Serializable;

/**
 * Created by Taurus on 2018/12/11.
 */
public class TimedTextSource implements Serializable {

    private String path;
    private String mimeType;
    private int flag;

    public TimedTextSource(String path) {
        this.path = path;
    }

    public TimedTextSource(String path, String mimeType) {
        this.path = path;
        this.mimeType = mimeType;
    }

    public TimedTextSource(String path, String mimeType, int flag) {
        this.path = path;
        this.mimeType = mimeType;
        this.flag = flag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
