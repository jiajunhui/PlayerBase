package com.kk.taurus.playerbase.callback;

import android.content.Context;
import android.view.View;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface CoverObserver<T> {
    View initCustomCoverView(Context context);
    void onCoverViewInit(View coverView);
    void onCoverVisibilityChange(View coverView, int visibility);
    void onDataChange(T data);
}
