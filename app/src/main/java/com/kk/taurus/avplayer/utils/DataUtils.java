package com.kk.taurus.avplayer.utils;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.avplayer.bean.VideoBean;

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
    public static final String VIDEO_URL_08 = "https://mov.bn.netease.com/open-movie/nos/mp4/2017/12/04/SD3SUEFFQ_hd.mp4";
    public static final String VIDEO_URL_09 = "https://mov.bn.netease.com/open-movie/nos/mp4/2017/05/31/SCKR8V6E9_hd.mp4";

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

    public static List<VideoBean> transList(List<VideoItem> items){
        List<VideoBean> videoList = new ArrayList<>();
        if(items!=null){
            VideoBean bean;
            for(VideoItem item:items){
                bean = new VideoBean(item.getDisplayName(), null, item.getPath());
                videoList.add(bean);
            }
        }
        return videoList;
    }

    public static List<VideoBean> getVideoList(int index, int offset) {
        List<VideoBean> videoList = getVideoList();
        int size = videoList.size();
        if(index < 0 || index > size-1)
            return new ArrayList<>();
        if(index + offset > size-1){
            offset = size - index;
        }
        return videoList.subList(index, index + offset);
    }

    public static List<VideoBean> getVideoList() {
        List<VideoBean> videoList = new ArrayList<>();
        videoList.add(new VideoBean(
                "你欠缺的也许并不是能力",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2016/06/22/SBP8G92E3_hd.mp4"));

        videoList.add(new VideoBean(
                "坚持与放弃",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2015/08/27/SB13F5AGJ_sd.mp4"));

        videoList.add(new VideoBean(
                "不想从被子里出来",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2018/01/12/SD70VQJ74_sd.mp4"));

        videoList.add(new VideoBean(
                "不耐烦的中国人?",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2017/05/31/SCKR8V6E9_hd.mp4"));

        videoList.add(new VideoBean(
                "神奇的珊瑚",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2016/01/11/SBC46Q9DV_hd.mp4"));

        videoList.add(new VideoBean(
                "怎样经营你的人脉",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2018/04/19/SDEQS1GO6_hd.mp4"));

        videoList.add(new VideoBean(
                "怎么才能不畏将来",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2018/01/25/SD82Q0AQE_hd.mp4"));

        videoList.add(new VideoBean(
                "音乐和艺术如何改变世界",
                "https://mov.bn.netease.com/open-movie/nos/mp4/2017/12/04/SD3SUEFFQ_hd.mp4"));

        return videoList;
    }


}
