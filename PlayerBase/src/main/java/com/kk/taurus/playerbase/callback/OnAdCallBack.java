package com.kk.taurus.playerbase.callback;

import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BaseAdPlayer;

/**
 * Created by Taurus on 2017/3/31.
 */

public abstract class OnAdCallBack implements OnAdListener {
    @Override
    public void onAdPlay(BaseAdPlayer adPlayer, BaseAdVideo adVideo) {
        adPlayer.startPlayAdVideo(adVideo);
    }

    @Override
    public void onAdPlayComplete(BaseAdVideo adVideo, boolean isAllComplete) {

    }

    @Override
    public void onVideoStart(BaseAdPlayer adPlayer, VideoData data) {
        adPlayer.setAndStartVideo(data);
    }
}
