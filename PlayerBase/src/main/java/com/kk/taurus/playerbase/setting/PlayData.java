package com.kk.taurus.playerbase.setting;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public class PlayData implements Serializable {

    private VideoData data;
    private List<BaseAdVideo> adVideos;

    public PlayData(VideoData data) {
        this.data = data;
    }

    public PlayData(VideoData data, List<BaseAdVideo> adVideos) {
        this.data = data;
        this.adVideos = adVideos;
    }

    public VideoData getData() {
        return data;
    }

    public void setData(VideoData data) {
        this.data = data;
    }

    public List<BaseAdVideo> getAdVideos() {
        return adVideos;
    }

    public void setAdVideos(List<BaseAdVideo> adVideos) {
        this.adVideos = adVideos;
    }

    public boolean isNeedAdPlay(){
        return adVideos!=null && adVideos.size()>0;
    }
}
