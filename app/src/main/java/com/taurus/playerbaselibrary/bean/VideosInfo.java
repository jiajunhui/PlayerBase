package com.taurus.playerbaselibrary.bean;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.base.HolderData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public class VideosInfo implements HolderData,Serializable {
    private List<VideoItem> videoItems = new ArrayList<>();

    public VideosInfo(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    public List<VideoItem> getVideoItems() {
        return videoItems;
    }

    public void setVideoItems(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }
}
