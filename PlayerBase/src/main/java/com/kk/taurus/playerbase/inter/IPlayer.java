package com.kk.taurus.playerbase.inter;

import android.content.res.Configuration;

import com.kk.taurus.playerbase.setting.PlayData;

/**
 * Created by Taurus on 2016/8/29.
 */
public interface IPlayer extends ISinglePlayer{
    void playData(PlayData data);
    void doConfigChange(Configuration newConfig);
    void toggleFullScreen();
    boolean isFullScreen();
}
