package com.taurus.playerbaselibrary;

import com.kk.taurus.playerbase.setting.CoverData;

import java.io.Serializable;

/**
 * Created by Taurus on 2017/3/27.
 */

public class TestPlayData implements CoverData,Serializable {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
