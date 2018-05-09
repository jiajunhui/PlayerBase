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

package com.kk.taurus.playerbase.utils;

import android.text.TextUtils;

/**
 * ------------------------------------
 * Created by Taurus on 2016/8/3.
 * ------------------------------------
 */
public class TimeUtil {

    private static final long SECONDS_ONE_HOUR = 60*60;

    public static final String TIME_FORMAT_01 = "%02d:%02d";
    public static final String TIME_FORMAT_02 = "%02d:%02d:%02d";

    public static String getTimeFormat1(long timeMs){
        return getTime(TIME_FORMAT_01, timeMs);
    }

    public static String getTimeFormat2(long timeMs){
        return getTime(TIME_FORMAT_02, timeMs);
    }

    public static String getTimeSmartFormat(long timeMs){
        int totalSeconds = (int) (timeMs / 1000);
        if(totalSeconds >= SECONDS_ONE_HOUR){
            return getTimeFormat2(timeMs);
        }else{
            return getTimeFormat1(timeMs);
        }
    }

    public static String getFormat(long maxTimeMs){
        int totalSeconds = (int) (maxTimeMs / 1000);
        if(totalSeconds >= SECONDS_ONE_HOUR){
            return TIME_FORMAT_02;
        }
        return TIME_FORMAT_01;
    }

    public static String getTime(String format, long time){
        if(time <= 0)
            time = 0;
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if(TIME_FORMAT_01.equals(format)){
            return String.format(format, minutes, seconds);
        }else if(TIME_FORMAT_02.equals(format)){
            return String.format(format, hours, minutes, seconds);
        }
        if(TextUtils.isEmpty(format)){
            format = TIME_FORMAT_02;
        }
        return String.format(format, hours, minutes, seconds);
    }

}
