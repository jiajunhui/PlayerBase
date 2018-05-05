package com.kk.taurus.playerbase.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * if you want get app context, you need call attach method to init it.
 */
public class AppContextAttach {

    private static Context mAppContext;

    public static void attach(Application application){
        mAppContext = application.getApplicationContext();
    }

    public static Context getApplicationContext(){
        if(mAppContext==null){
            Log.e("AppContextAttach", "app context not init !!!");
        }
        return mAppContext;
    }

}
