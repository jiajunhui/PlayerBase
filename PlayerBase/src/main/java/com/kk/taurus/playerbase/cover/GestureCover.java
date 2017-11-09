package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.view.GestureLayout;

/**
 * Created by mtime on 2017/10/18.
 */

public class GestureCover extends BaseCover {

    private GestureLayout mGestureLayout;

    public GestureCover(Context context, OnPlayerGestureListener onPlayerGestureListener) {
        super(context);
        mGestureLayout.setPlayerGestureListener(onPlayerGestureListener);
    }

    public GestureLayout getGestureLayout(){
        return mGestureLayout;
    }

    @Override
    protected void setDefaultGone() {

    }

    @Override
    protected void findView() {

    }

    @Override
    protected void afterFindView() {
        super.afterFindView();
    }

    @Override
    public View initCoverLayout(Context context) {
        mGestureLayout = new GestureLayout(context);
        return mGestureLayout;
    }

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_MEDIUM;
    }
}
