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

    public static List<VideoBean> getVideoList() {
        List<VideoBean> videoList = new ArrayList<>();
        videoList.add(new VideoBean("幸福是什么",
                "https://cms-bucket.nosdn.127.net/171956fc3b0f493482424654b6fb14a520180418140011.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2015/12/31/SBB7M663L_hd.flv"));

        videoList.add(new VideoBean("群体性孤独",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2015/11/26/SB8EEJKNH_hd.flv"));

        videoList.add(new VideoBean("被拒100天",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2017/01/16/SC9VQFO3E_hd.flv"));

        videoList.add(new VideoBean("什么让我们更热爱自己的工作?",
                "https://cms-bucket.nosdn.127.net/e2af1d563faa46d0aa19da87f83159fd20180418131040.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2014/01/03/S9GO68OJG_sd.flv"));

        videoList.add(new VideoBean("为什么健康的生活方式几乎把我害死",
                "https://cms-bucket.nosdn.127.net/f90b03a4bac34419b4bc0b22f4d989b420180411100506.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2013/08/05/S94IVFMKI_hd.flv"));

        videoList.add(new VideoBean("如何掌控你的自由时间？",
                "https://cms-bucket.nosdn.127.net/aae5c06c35d94f45ae3c3108dcb493e520180408212733.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2017/01/03/SC8U8K7BC_hd.flv"));

        videoList.add(new VideoBean("韩雪：积极的悲观主义者",
                "https://cms-bucket.nosdn.127.net/b963028024f847fe903b1638b05516a120180408212720.jpeg",
                "http://mov.bn.netease.com/open-movie/nos/flv/2017/07/24/SCP786QON_hd.flv"));

        return videoList;
    }


}
