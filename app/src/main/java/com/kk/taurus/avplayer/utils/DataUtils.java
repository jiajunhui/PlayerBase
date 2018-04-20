package com.kk.taurus.avplayer.utils;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/4/19.
 */

public class DataUtils {

    public static final String VIDEO_URL_01 = "http://jiajunhui.cn/video/kaipao.mp4";
    public static final String VIDEO_URL_02 = "http://jiajunhui.cn/video/kongchengji.mp4";
    public static final String VIDEO_URL_03 = "http://jiajunhui.cn/video/allsharestar.mp4";
    public static final String VIDEO_URL_04 = "http://jiajunhui.cn/video/edwin_rolling_in_the_deep.flv";
    public static final String VIDEO_URL_05 = "http://jiajunhui.cn/video/crystalliz.flv";
    public static final String VIDEO_URL_06 = "http://jiajunhui.cn/video/big_buck_bunny.mp4";
    public static final String VIDEO_URL_07 = "http://jiajunhui.cn/video/trailer.mp4";

    public static String[] urls = new String[]{
            VIDEO_URL_01,
            VIDEO_URL_02,
            VIDEO_URL_03,
            VIDEO_URL_04,
            VIDEO_URL_05,
            VIDEO_URL_06,
            VIDEO_URL_07,
    };

    public static List<VideoItem> getRemoteVideoItems(){
        VideoItem item;
        List<VideoItem> items = new ArrayList<>();
        int len = urls.length;
        for(int i=0;i<len;i++){
            item = new VideoItem();
            item.setPath(urls[i]);
            item.setDisplayName(urls[i]);
            items.add(item);
        }
        return items;
    }

}
