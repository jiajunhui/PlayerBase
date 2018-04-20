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

package com.kk.taurus.playerbase.log;

import android.util.Log;

/**
 * Created by Taurus on 2018/3/17.
 */

public class PLog {

    public static boolean LOG_OPEN = false;

    public static void d(String tag, String message){
        if(!LOG_OPEN)
            return;
        Log.d(tag,message);
    }

    public static void w(String tag, String message){
        if(!LOG_OPEN)
            return;
        Log.w(tag,message);
    }

    public static void e(String tag, String message){
        if(!LOG_OPEN)
            return;
        Log.e(tag,message);
    }

}
