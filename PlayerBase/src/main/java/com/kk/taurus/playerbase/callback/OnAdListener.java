package com.kk.taurus.playerbase.callback;

import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BaseAdPlayer;

/**
 * Created by Taurus on 2017/3/31.
 */

public interface OnAdListener {
    void onAdPlay(BaseAdPlayer adPlayer, BaseAdVideo adVideo);
    void onAdPlayComplete(BaseAdVideo adVideo,boolean isAllComplete);
    void onVideoStart(BaseAdPlayer adPlayer, VideoData data);
}
