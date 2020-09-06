package com.kk.taurus.playerbase.extension;

import android.os.Bundle;

/**
 * @ClassName EventCallback
 * @Description
 * @Author Taurus
 * @Date 2020/9/6 6:16 PM
 */
public interface EventCallback {

    void onPlayerEvent(int eventCode, Bundle data);

    void onErrorEvent(int eventCode, Bundle data);

    void onReceiverEvent(int eventCode, Bundle data);

}
