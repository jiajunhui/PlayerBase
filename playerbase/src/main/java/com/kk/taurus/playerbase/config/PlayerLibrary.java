package com.kk.taurus.playerbase.config;

import android.app.Application;

public class PlayerLibrary {

    public static void init(Application application){
        AppContextAttach.attach(application);
    }

}
