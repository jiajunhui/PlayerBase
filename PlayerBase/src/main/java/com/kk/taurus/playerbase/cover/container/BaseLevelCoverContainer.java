package com.kk.taurus.playerbase.cover.container;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class BaseLevelCoverContainer extends BaseCoverContainer {

    protected List<View> mLevelContainer = new ArrayList<>();

    public BaseLevelCoverContainer(Context context) {
        super(context);
        initLevelContainers(context);
    }

    protected abstract void initLevelContainers(Context context);

    protected void addLevelContainerView(ViewGroup container, ViewGroup.LayoutParams layoutParams){
        if(getContainerRoot()!=null){
            if(layoutParams==null)
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getContainerRoot().addView(container,layoutParams);
            mLevelContainer.add(container);
        }
    }

    protected void removeLevelContainerView(ViewGroup container){
        if(getContainerRoot()!=null){
            getContainerRoot().removeView(container);
            mLevelContainer.remove(container);
        }
    }

    protected void removeAllLevelContainers(){
        if(getContainerRoot()!=null){
            getContainerRoot().removeAllViews();
            mLevelContainer.clear();
        }
    }

}
