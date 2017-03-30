package com.taurus.playerbaselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Taurus on 2017/3/30.
 */

public class Setting {

    public static final String configName = "setting";
    private static Setting instance = new Setting();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String fileName;

    private Setting(){
    }

    public synchronized static Setting getInstance(){
        fileName = configName;
        return instance;
    }

    public synchronized static Setting getInstance(String name){
        fileName = name;
        return instance;
    }

    public synchronized void save(Context context, String key,int value){
        sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public synchronized int getInt(Context context,String key, int defaultValue){
        sharedPreferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,defaultValue);
    }

}
