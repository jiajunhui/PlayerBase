package com.taurus.playerbaselibrary;

import android.app.Application;

import com.kk.taurus.playerbase.setting.PlayerType;

/**
 * Created by Taurus on 2017/3/28.
 */

public class MApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PlayerType.getInstance().addPlayerType(0,"com.kk.taurus.ijkplayer.IJKSinglePlayer");
    }
}
