package com.taurus.playerbaselibrary;

import com.kk.taurus.baseframe.FrameApplication;
import com.kk.taurus.baseframe.manager.SharedPrefer;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.PlayerTypeEntity;

/**
 * Created by Taurus on 2017/3/28.
 */

public class MApp extends FrameApplication {

    @Override
    public void onCreateInAppMainProcess() {
        super.onCreateInAppMainProcess();
        PlayerType.getInstance().addPlayerType(1,new PlayerTypeEntity("IJK播放器","com.kk.taurus.ijkplayer.IJKSinglePlayer"));
        PlayerType.getInstance().setDefaultPlayerType(SharedPrefer.getInstance().getInt(getApplicationContext(),"player_type",0));
    }

}
