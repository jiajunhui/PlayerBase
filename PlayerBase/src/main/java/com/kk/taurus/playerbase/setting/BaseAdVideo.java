package com.kk.taurus.playerbase.setting;


/**
 * Created by Taurus on 2016/9/29.
 */
public class BaseAdVideo extends VideoData {

    private String redirectUrl;

    public BaseAdVideo(String data) {
        super(data);
    }

    public BaseAdVideo(String data, Rate rate) {
        super(data, rate);
    }

    public BaseAdVideo(String data, int playerType, Rate rate) {
        super(data, playerType, rate);
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
