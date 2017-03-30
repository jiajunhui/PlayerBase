package com.taurus.playerbaselibrary;

import com.kk.taurus.baseframe.FrameApplication;
import com.kk.taurus.playerbase.setting.PlayerType;

/**
 * Created by Taurus on 2017/3/28.
 */

public class MApp extends FrameApplication {

    @Override
    public void onCreateInAppMainProcess() {
        super.onCreateInAppMainProcess();
        PlayerType.getInstance().addPlayerType(1,"com.kk.taurus.ijkplayer.IJKSinglePlayer");
        PlayerType.getInstance().setDefaultPlayerType(1);
    }

}
