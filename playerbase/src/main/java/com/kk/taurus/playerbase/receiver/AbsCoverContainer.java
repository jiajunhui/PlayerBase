/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.receiver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class AbsCoverContainer implements ICoverStrategy {

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
    public ViewGroup getContainerView() {
        return mContainerRoot;
    }

    protected abstract ViewGroup initContainerRootView();
}
