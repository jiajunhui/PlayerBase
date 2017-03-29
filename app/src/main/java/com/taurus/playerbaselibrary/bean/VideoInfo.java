package com.taurus.playerbaselibrary.bean;

import com.kk.taurus.playerbase.setting.CoverData;

import java.io.Serializable;

/**
 * Created by Taurus on 2017/3/29.
 */

public class VideoInfo implements Serializable,CoverData {
    private String videoName;

    public VideoInfo() {
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}
