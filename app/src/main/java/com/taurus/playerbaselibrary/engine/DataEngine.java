package com.taurus.playerbaselibrary.engine;

import com.kk.taurus.http_helper.XHTTP;
import com.kk.taurus.http_helper.bean.XRequest;
import com.kk.taurus.http_helper.callback.ReqCallBack;

import okhttp3.Call;

/**
 * Created by Taurus on 2017/4/30.
 */

public class DataEngine {

    public static Call loadVideos(String videoType, int pageIndex, ReqCallBack reqCallBack){
        XRequest request = new XRequest();
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
        String url = API.getUrl(videoType, pageIndex);
        request.setUrl(url);
        return XHTTP.newGet(request,reqCallBack);
    }

}
