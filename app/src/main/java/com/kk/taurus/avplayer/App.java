package com.kk.taurus.avplayer;

import android.app.Application;

import com.kk.taurus.ijkplayer.IjkPlayer;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.log.PLog;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Taurus on 2018/4/15.
 */

public class App extends Application {

    public static final int PLAN_ID_IJK = 1;

    private static App instance;

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

        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_IJK, IjkPlayer.class.getName(), "IjkPlayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_IJK);
    }

}
