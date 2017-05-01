package com.taurus.playerbaselibrary.engine;

/**
 * Created by Taurus on 2017/4/30.
 */

public class API {

    /**
     * http://c.3g.163.com/nc/video/list/V9LG4CHOR/n/10-10.html 视频
     */
    public static final String VIDEO = "http://c.m.163.com/nc/video/list/";
    // 热点视频
    public static final String VIDEO_TYPE_HOT_ID = "V9LG4B3A0";
    // 娱乐视频
    public static final String VIDEO_TYPE_ENTERTAINMENT_ID = "V9LG4CHOR";
    // 搞笑视频
    public static final String VIDEO_TYPE_FUN_ID = "V9LG4E6VR";
    // 精品视频
    public static final String VIDEO_TYPE_CHOICE_ID = "00850FRB";

    public static String getUrl(String videoType, int pageIndex){
        return VIDEO + videoType + "/n/" + (pageIndex*10) + "-10.html";
    }

}
