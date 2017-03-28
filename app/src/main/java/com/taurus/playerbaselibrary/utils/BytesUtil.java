package com.taurus.playerbaselibrary.utils;

import java.math.BigDecimal;

/**
 * ------------------------------------
 * Created by Taurus on 2016/8/3.
 * ------------------------------------
 */
public class BytesUtil {

    private static final String UNIT_KB = "KB";
    private static final String UNIT_MB = "MB";
    private static final String UNIT_GB = "GB";

    private static final long VALUE_KB_BYTES = 1024;
    private static final long VALUE_MB_BYTES = 1024*1024;
    private static final long VALUE_GB_BYTES = 1024*1024*1024;

    public static String formatBytes(long bytes){
        if(bytes<VALUE_MB_BYTES){
            float KB = (float) (bytes*1.0/VALUE_KB_BYTES);
            return getDecimalPrice(KB) + UNIT_KB;
        }else if(bytes<VALUE_GB_BYTES){
            float MB = (float) (bytes*1.0/VALUE_MB_BYTES);
            return getDecimalPrice(MB) + UNIT_MB;
        }else{
            float GB = (float) (bytes*1.0/VALUE_GB_BYTES);
            return getDecimalPrice(GB) + UNIT_GB;
        }
    }

    public static String getDecimalPrice(float price){
        BigDecimal bigDecimal = new BigDecimal(price);
        return bigDecimal.setScale(1, BigDecimal.ROUND_DOWN).toString();
    }
}
