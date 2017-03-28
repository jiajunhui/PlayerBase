package com.kk.taurus.playerbase.config;

import android.content.Context;

import com.kk.taurus.playerbase.setting.PlayerType;

import java.lang.reflect.Constructor;

/**
 * Created by Taurus on 2016/11/16.
 */

public class ConfigLoader {

    public static Object getPlayerInstance(Context context, int playerType){
        Object instance = null;
        try{
            Class clz = getSDKClass(PlayerType.getInstance().getPlayerPath(playerType));
            if(clz!=null){
                Constructor constructor = getConstructor(clz);
                if(constructor!=null){
                    instance = constructor.newInstance(context);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    private static Constructor getConstructor(Class clz){
        Constructor result = null;
        try{
            result = clz.getConstructor(Context.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private static Class getSDKClass(String classPath){
        Class result = null;
        try {
            result = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
