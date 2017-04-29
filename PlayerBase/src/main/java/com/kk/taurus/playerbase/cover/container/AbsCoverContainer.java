package com.kk.taurus.playerbase.cover.container;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICoverContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class AbsCoverContainer implements ICoverContainer {

    protected Context mContext;
    private List<BaseCover> mCovers;
    private ViewGroup mContainerRoot;

    public AbsCoverContainer(Context context){
        this.mContext = context;
        mCovers = new ArrayList<>();
        mContainerRoot = initContainerRootView();
    }

    @Override
    public void addCover(BaseCover cover) {
        onCoverAdd(cover);
        if(isAvailableCover(cover)){
            mCovers.add(cover);
            onAvailableCoverAdd(cover);
        }
    }

    protected abstract void onCoverAdd(BaseCover cover);

    protected abstract void onAvailableCoverAdd(BaseCover cover);

    @Override
    public void removeCover(BaseCover cover) {
        onCoverRemove(cover);
        if(isAvailableCover(cover)){
            mCovers.remove(cover);
            onAvailableCoverRemove(cover);
        }
    }

    protected abstract void onCoverRemove(BaseCover cover);

    protected abstract void onAvailableCoverRemove(BaseCover cover);

    @Override
    public void removeAllCovers() {
        mCovers.clear();
        onCoversRemoveAll();
    }

    protected abstract void onCoversRemoveAll();

    protected boolean isAvailableCover(BaseCover cover){
        return cover!=null && cover.getView()!=null;
    }

    @Override
    public boolean isContainsCover(BaseCover cover) {
        if(!isAvailableCover(cover))
            return false;
        int index = rootIndexOfChild(cover.getView());
        if(index!=-1){
            return true;
        }
        int childCount = getRootChildCount();
        if(childCount<=0)
            return false;
        boolean result = false;
        for(int i=0;i<childCount;i++){
            View view = rootGetChildAt(i);
            if(view instanceof ViewGroup && ((ViewGroup)view).indexOfChild(cover.getView())!=-1){
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public int getCoverCount() {
        if(mCovers==null)
            return 0;
        return mCovers.size();
    }

    protected int rootIndexOfChild(View view){
        if(mContainerRoot==null)
            return -1;
        return mContainerRoot.indexOfChild(view);
    }

    protected int getRootChildCount(){
        if(mContainerRoot==null)
            return 0;
        return mContainerRoot.getChildCount();
    }

    protected View rootGetChildAt(int index){
        if(mContainerRoot==null)
            return null;
        return mContainerRoot.getChildAt(index);
    }

    @Override
    public ViewGroup getContainerRoot() {
        return mContainerRoot;
    }

    protected abstract ViewGroup initContainerRootView();
}
