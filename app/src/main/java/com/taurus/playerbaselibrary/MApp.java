package com.taurus.playerbaselibrary;

import android.os.Environment;

import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.config.VideoCacheProxy;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.DecoderTypeEntity;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.PlayerTypeEntity;
import com.kk.taurus.playerbase.utils.PLog;
import com.kk.taurus.uiframe.FrameApplication;
import com.taurus.playerbaselibrary.callback.TestCacheFileNameGenerator;
import com.taurus.playerbaselibrary.holder.SettingHolder;
import com.taurus.playerbaselibrary.utils.SharedPrefer;
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


        //是否输出播放框架内部事件日志
        PLog.LOG_OPEN = BuildConfig.DEBUG;

        //边播边缓存配置
        VideoCacheProxy.get().initHttpProxyCacheServer(
                new VideoCacheProxy.Builder(this)
                        .setCacheDirectory(Environment.getExternalStorageDirectory())
                        .setFileNameGenerator(new TestCacheFileNameGenerator()));
        if(SharedPrefer.getInstance().getBoolean(this,SettingHolder.KEY_PLAYER_VIDEO_CACHE,false)){
            //开启边播边缓存功能
            VideoCacheProxy.get().setVideoCacheState(true);
        }

        /**
         *
         * 如果要实现无缝续播的功能，不能使用VideoView类型的方案，组件模式必须设置为WIDGET_MODE_DECODER
         *
         * **/
        //使用系统MediaPlayer,IjkMediaPlayer融合方案
        DecoderType.getInstance().addDecoderType(1,new DecoderTypeEntity("ijkplayer","com.kk.taurus.ijkplayer.IJkDecoderPlayer"));
        int type = SharedPrefer.getInstance().getInt(this, SettingHolder.KEY_PLAYER_TYPE, 0);
        DecoderType.getInstance().setDefaultDecoderType(type);
        ConfigLoader.setDefaultWidgetMode(this,IPlayer.WIDGET_MODE_DECODER);

//        //使用系统的VideoView方案
//        PlayerType.getInstance().setDefaultPlayerType(0);
//        ConfigLoader.setDefaultWidgetMode(this, IPlayer.WIDGET_MODE_VIDEO_VIEW);
//
//        //使用IJKPlayer的IjkVideoView方案
//        PlayerType.getInstance().addPlayerType(1,new PlayerTypeEntity("IJK播放器","com.kk.taurus.ijkplayer.IJKVideoViewPlayer"));
//        PlayerType.getInstance().setDefaultPlayerType(1);
//        ConfigLoader.setDefaultWidgetMode(this,IPlayer.WIDGET_MODE_VIDEO_VIEW);
//
//        //使用IJKPlayer的IjkMediaPlayer方案
//        DecoderType.getInstance().addDecoderType(1,new DecoderTypeEntity("ijkplayer","com.kk.taurus.ijkplayer.IJkDecoderPlayer"));
//        DecoderType.getInstance().setDefaultDecoderType(1);
//        ConfigLoader.setDefaultWidgetMode(this, IPlayer.WIDGET_MODE_DECODER);
    }

}
