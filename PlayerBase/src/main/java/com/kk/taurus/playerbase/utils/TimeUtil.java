package com.kk.taurus.playerbase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ------------------------------------
 * Created by Taurus on 2016/8/3.
 * ------------------------------------
 */
public class TimeUtil {

    /**
     * return time format , for example 00:08:19
     * @param time
     * @return
     */
    public static String getTime(long time){
        if(time <= 0)
            return "00:00:00";
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String getNowTime(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date());
    }

}
