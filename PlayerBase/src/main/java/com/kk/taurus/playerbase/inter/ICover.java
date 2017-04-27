package com.kk.taurus.playerbase.inter;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.setting.CoverData;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface ICover {

    int COVER_LEVEL_LOW = 0;
    int COVER_LEVEL_MEDIUM = 1;
    int COVER_LEVEL_HIGH = 2;

    View initCoverLayout(Context context);
    void setCoverEnable(boolean enable);
    void setCoverVisibility(int visibility);
    void onRefreshCoverData(CoverData data);
    View getView();
    int getCoverLevel();
    CoverObserver getCoverObserver();
}
