package com.kk.taurus.playerbase.touch;

/**
 * @ClassName TouchEventIndicator
 * @Description
 * @Author Taurus
 * @Date 2020/9/1 9:26 PM
 */
public interface TouchEventIndicator {

    /**
     * If you don't want to receive a touch event, the method returns true
     *
     * @return
     */
    boolean disallowReceiveTouchEvent();

}
