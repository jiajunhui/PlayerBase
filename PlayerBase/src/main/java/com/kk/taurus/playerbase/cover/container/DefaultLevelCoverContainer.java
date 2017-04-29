package com.kk.taurus.playerbase.cover.container;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICover;

/**
 * Created by Taurus on 2017/4/29.
 */

public class DefaultLevelCoverContainer extends BaseLevelCoverContainer {

    /**
     * the business covers container .
     */
    private FrameLayout mLevelLowCoverContainer;
    /**
     * the state covers container .
     */
    private FrameLayout mLevelMediumCoverContainer;
    /**
     * the extend covers container .
     */
    private FrameLayout mLevelHighCoverContainer;

    public DefaultLevelCoverContainer(Context context) {
        super(context);
    }

    @Override
    protected void initLevelContainers(Context context) {
        //add low cover container
        mLevelLowCoverContainer = new FrameLayout(context);
        mLevelLowCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelLowCoverContainer,null);

        //add medium cover container
        mLevelMediumCoverContainer = new FrameLayout(context);
        mLevelMediumCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelMediumCoverContainer,null);

        //add high cover container
        mLevelHighCoverContainer = new FrameLayout(context);
        mLevelHighCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelHighCoverContainer,null);
    }

    @Override
    protected void onAvailableCoverAdd(BaseCover cover) {
        super.onAvailableCoverAdd(cover);
        switch (cover.getCoverLevel()){
            case ICover.COVER_LEVEL_LOW:
                mLevelLowCoverContainer.addView(cover.getView(),getNewMatchLayoutParams());
                break;
            case ICover.COVER_LEVEL_MEDIUM:
                mLevelMediumCoverContainer.addView(cover.getView(),getNewMatchLayoutParams());
                break;
            case ICover.COVER_LEVEL_HIGH:
                mLevelHighCoverContainer.addView(cover.getView(),getNewMatchLayoutParams());
                break;
        }
    }

    @Override
    protected void onAvailableCoverRemove(BaseCover cover) {
        super.onAvailableCoverRemove(cover);
        mLevelLowCoverContainer.removeView(cover.getView());
        mLevelMediumCoverContainer.removeView(cover.getView());
        mLevelHighCoverContainer.removeView(cover.getView());
    }

    @Override
    protected void onCoversRemoveAll() {
        super.onCoversRemoveAll();
        mLevelLowCoverContainer.removeAllViews();
        mLevelMediumCoverContainer.removeAllViews();
        mLevelHighCoverContainer.removeAllViews();
        removeAllLevelContainers();
    }

    private ViewGroup.LayoutParams getNewMatchLayoutParams(){
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
