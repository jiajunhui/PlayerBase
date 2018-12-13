package com.kk.taurus.avplayer;

import android.app.Application;
import android.os.Environment;

import com.kk.taurus.exoplayer.ExoMediaPlayer;
import com.kk.taurus.ijkplayer.IjkPlayer;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.record.PlayRecordManager;
import com.squareup.leakcanary.LeakCanary;
import com.xapp.jjh.logtools.config.XLogConfig;
import com.xapp.jjh.logtools.logger.LogLevel;
import com.xapp.jjh.logtools.tools.XLog;

import java.io.File;

/**
 * Created by Taurus on 2018/4/15.
 */

public class App extends Application {

    public static final int PLAN_ID_IJK = 1;
    public static final int PLAN_ID_EXO = 2;

    private static App instance;

    public static boolean ignoreMobile;

    public static App get(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PLog.LOG_OPEN = true;

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        XLog.init(getApplicationContext(),
                new XLogConfig()
                        //loglevel FULL为显示log ，NONE为不显示log
                        .setLogLevel(LogLevel.FULL)
                        //文件日志以及崩溃日志文件的目录
                        .setLogDir(new File(Environment.getExternalStorageDirectory(),"TestXLog"))
                        //崩溃日志文件标记名称
                        .setCrashLogTag("CrashLogTag")
                        //是否云保存文件日志（非crash日志）
                        .setFileLogAllow(true)
                        //普通文件日志标记名称
                        .setNormalLogTag("NormalLogTag")
                        //日志文件扩展名，默认.txt
                        .setFileExtensionName(XLogConfig.DEFAULT_FILE_EXTENSION_NAME)
                        //日志文件定期清理周期（单位毫秒），默认为一周（7*24*60*60*1000）
                        .setFileClearCycle(XLogConfig.DEFAULT_FILE_CLEAR_CYCLE)
                        //是否保存崩溃日志
                        .setSaveCrashLog(true)
                        //是否为普通日志信息添加消息框
                        .setMessageTable(true));

        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_IJK, IjkPlayer.class.getName(), "IjkPlayer"));
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_EXO, ExoMediaPlayer.class.getName(), "ExoPlayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_IJK);

        //use default NetworkEventProducer.
        PlayerConfig.setUseDefaultNetworkEventProducer(true);

        PlayerConfig.playRecord(true);

        PlayRecordManager.setRecordConfig(
                new PlayRecordManager.RecordConfig.Builder()
                        .setMaxRecordCount(100).build());

        PlayerLibrary.init(this);
    }

}
