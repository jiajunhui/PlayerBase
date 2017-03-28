package com.kk.taurus.playerbase.callback;

import android.os.Bundle;

/**
 * Created by Taurus on 2016/8/30.
 */
public interface OnErrorListener {

    int ERROR_CODE_COMMON = 30001;
    int ERROR_CODE_NET_ERROR = 30002;

    void onError(int errorCode, Bundle bundle);
}
