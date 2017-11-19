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

import android.content.Context;

import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.lang.reflect.Constructor;

/**
 * Created by Taurus on 2016/11/16.
 */

public class ConfigLoader {

    private static int mWidgetMode = BasePlayer.WIDGET_MODE_VIDEO_VIEW;

    public static void setDefaultWidgetMode(int widgetMode){
        mWidgetMode = widgetMode;
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
