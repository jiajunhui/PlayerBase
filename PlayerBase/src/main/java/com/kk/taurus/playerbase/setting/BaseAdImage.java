package com.kk.taurus.playerbase.setting;

import java.io.Serializable;

/**
 * Created by Taurus on 2017/4/24.
 */

public class BaseAdImage implements Serializable {

    public static final long DEFAULT_DISPLAY_TIME = 5000;
    private String imageUrl;
    private long displayTime = DEFAULT_DISPLAY_TIME;

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
