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
import android.util.Log;

/**
 * if you want get app context, you need call attach method to init it.
 */
public class AppContextAttach {

    private static Context mAppContext;

    static void attach(Context context){
        mAppContext = context.getApplicationContext();
    }

    public static Context getApplicationContext(){
        if(mAppContext==null){
            Log.e("AppContextAttach", "app context not init !!!");
            throw new RuntimeException("if you need context for using decoder, you must call PlayerLibrary.init(context).");
        }
        return mAppContext;
    }

}
