package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by Taurus on 2017/4/24.
 */

public class BaseAdImage implements Serializable {
    private String imageUrl;
    private long displayTime;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
    }
}
