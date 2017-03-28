package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.cover.base.BaseCoverCollections;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;

/**
 * Created by Taurus on 2017/3/24.
 */

public class DefaultCoverObserver extends BaseCoverObserver<Object> {

    public DefaultCoverObserver(Context context) {
        super(context);
    }

    public DefaultCoverObserver(Context context, BaseCoverCollections coverCollections) {
        super(context, coverCollections);
    }

    @Override
    public void onCoverViewInit(View coverView) {

    }

    @Override
    public void onCoverVisibilityChange(View coverView, int visibility) {

    }

}
