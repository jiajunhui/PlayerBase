package com.kk.taurus.playerbase.cover.container;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.cover.base.BaseCover;


/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class BaseCoverContainer extends AbsCoverContainer {

    private final String TAG = "base_cover_container";

    public BaseCoverContainer(Context context) {
        super(context);
    }

    @Override
    protected void onCoverAdd(BaseCover cover) {

    }

    @Override
    protected void onAvailableCoverAdd(BaseCover cover) {
        Log.d(TAG,"on available cover add : now count = " + getCoverCount());
    }

    @Override
    protected void onCoverRemove(BaseCover cover) {
        Log.d(TAG,"on cover remove : now count = " + getCoverCount());

    }

    @Override
    protected void onAvailableCoverRemove(BaseCover cover) {

    }

    @Override
    protected void onCoversRemoveAll() {
        Log.d(TAG,"on covers remove all ...");

    }

    @Override
    protected ViewGroup initContainerRootView() {
        FrameLayout root = new FrameLayout(mContext);
        root.setBackgroundColor(Color.TRANSPARENT);
        return root;
    }
}
