package com.kk.taurus.playerbase.inter;

import android.app.Activity;

/**
 * Created by Taurus on 2017/4/27.
 */

public interface ITools {
    int getScreenOrientation();
    Activity getActivity();
    String getString(int resId);
}
