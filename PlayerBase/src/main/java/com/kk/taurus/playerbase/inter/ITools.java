package com.kk.taurus.playerbase.inter;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Taurus on 2017/4/27.
 */

public interface ITools {
    int getScreenOrientation();
    Activity getActivity();
    Bundle getBundle();
    String getString(int resId);
}
