/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.config;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.widget.plan.InternalPlayerManager;
import com.kk.taurus.playerbase.setting.PlayerType;

import java.lang.reflect.Constructor;

/**
 * Created by Taurus on 2016/11/16.
 */

public class ConfigLoader {

    private static final String TAG = "ConfigLoader";

    private static int mWidgetMode = IPlayer.WIDGET_MODE_DECODER;

    public static void setDefaultWidgetMode(Application application, int widgetMode){
        mWidgetMode = widgetMode;
        //当组件类型为decoder时，可直接初始化decoder解码器
        if(mWidgetMode==IPlayer.WIDGET_MODE_DECODER){
            InternalPlayerManager.get().updateWidgetMode(application.getApplicationContext(),mWidgetMode);
        }
    }

    public static int getWidgetMode(){
        return mWidgetMode;
    }

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
            Log.d(TAG,"init VideoView instance ...");
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    public static Object getDecoderInstance(Context context, int decoderType){
        Object instance = null;
        try{
            Class clz = getSDKClass(DecoderType.getInstance().getDecoderPath(decoderType));
            if(clz!=null){
                Constructor constructor = getConstructor(clz);
                if(constructor!=null){
                    instance = constructor.newInstance(context);
                }
            }
            Log.d(TAG,"init Decoder instance ...");
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
