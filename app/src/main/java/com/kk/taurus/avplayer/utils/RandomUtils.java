package com.kk.taurus.avplayer.utils;

import java.util.Random;

/**
 * author:   jiajunhui
 * date:     2019/5/29 9:45
 * email:    junhui.jia@wm-motor.com
 * Description:     随机数或随机字符工具
 */
public class RandomUtils {

    /**
     * 获取指定位数的随机数字
     * @param len
     * @return
     */
    public static String getRandomNumberStr(int len){
        if(len < 1)
            return "";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++){
            sb.append(Character.toChars(getRandomInt(48,57)));
        }
        return sb.toString();
    }

    /**
     * 获取指定位数的随机大写字母
     * @param len
     * @return
     */
    public static String getRandomUpperLetterStr(int len){
        if(len < 1)
            return "";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++){
            sb.append(Character.toChars(getRandomInt(65,90)));
        }
        return sb.toString();
    }

    /**
     * 获取指定位数的随机小写字母
     * @param len
     * @return
     */
    public static String getRandomLowerLetterStr(int len){
        if(len < 1)
            return "";
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<len;i++){
            sb.append(Character.toChars(getRandomInt(97,122)));
        }
        return sb.toString();
    }

    public static int getRandomInt(int min, int max){
        return new Random().nextInt(max - min + 1) + min;
    }

}
