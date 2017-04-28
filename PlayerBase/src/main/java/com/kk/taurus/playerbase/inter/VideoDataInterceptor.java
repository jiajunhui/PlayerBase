package com.kk.taurus.playerbase.inter;

import com.kk.taurus.playerbase.setting.VideoData;

/**
 * Created by Taurus on 2017/4/28.
 */

public interface VideoDataInterceptor {
    void onSetDataSource(VideoData data);
}
