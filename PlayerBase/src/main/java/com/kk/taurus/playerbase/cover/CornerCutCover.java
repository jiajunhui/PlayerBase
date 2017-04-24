package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.view.CornerCutView;

/**
 * Created by Taurus on 2017/3/31.
 */

public class CornerCutCover extends BaseCover {

    public static final String KEY = "corner_cut_cover";
    private CornerCutView mCornerView;

    public CornerCutCover(Context context){
        super(context);
    }

    public CornerCutCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void setDefaultGone() {

    }

    @Override
    protected void findView() {

    }

    public void setCornerBgColor(int color){
        mCornerView.setCornerBgColor(color);
    }

    public void setCornerRadius(int radius){
        mCornerView.setCornerRadius(radius);
    }

    @Override
    public View initCoverLayout(Context context) {
        return mCornerView = new CornerCutView(context);
    }

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_HIGH;
    }

}
