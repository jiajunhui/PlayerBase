package com.taurus.playerbaselibrary;

import com.kk.taurus.baseframe.FrameApplication;
import com.kk.taurus.baseframe.manager.SharedPrefer;
import com.kk.taurus.http_helper.XHTTP;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.PlayerTypeEntity;
import com.xapp.jjh.logtools.config.XLogConfig;
import com.xapp.jjh.logtools.logger.LogLevel;
import com.xapp.jjh.logtools.tools.XLog;

/**
 * Created by Taurus on 2017/3/28.
 */

public class MApp extends FrameApplication {

    @Override
    public void onCreateInAppMainProcess() {
        super.onCreateInAppMainProcess();

        XLog.init(this,
                new XLogConfig()
                        .setLogLevel(LogLevel.FULL)
                        .setLogDir(getExternalCacheDir())
                        .setFileExtensionName(".txt")
                        .setFileLogAllow(true)
                        .setMessageTable(true));

        XHTTP.init(this,null);
//        ConfigLoader.setDefaultWidgetMode(IPlayer.WIDGET_MODE_VIDEO_VIEW);
//        DecoderType.getInstance().setDefaultDecoderType(0);
        ConfigLoader.setDefaultWidgetMode(this, IPlayer.WIDGET_MODE_DECODER);
//        PlayerType.getInstance().addPlayerType(1,new PlayerTypeEntity("IJK播放器","com.kk.taurus.ijkplayer.IJKRenderWidgetPlayer"));
//        PlayerType.getInstance().setDefaultPlayerType(0);
//        PlayerType.getInstance().setDefaultPlayerType(SharedPrefer.getInstance().getInt(getApplicationContext(),"player_type",0));
    }

}
