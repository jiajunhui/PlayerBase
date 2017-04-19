package com.kk.taurus.playerbase.adapter;

import com.kk.taurus.playerbase.inter.IDataAdapter;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/4/19.
 */

public abstract class BaseVideoDataAdapter<V> implements IDataAdapter<VideoData> {

    protected List<V> mModels;
    protected boolean loop;
    protected int mIndex;

    public BaseVideoDataAdapter(List<V> models, int startIndex){
        this.mModels = models;
        this.mIndex = startIndex;
    }

    @Override
    public int getIndex() {
        return mIndex;
    }

    @Override
    public void setIndex(int index) {
        this.mIndex = index;
    }

    protected int nextIndex(){
        if(getCount() > 0){
            mIndex++;
            mIndex = mIndex%getCount();
            return mIndex;
        }
        return -1;
    }

    public V getItemModel(){
        if(getCount() > 0)
            return mModels.get(mIndex);
        return null;
    }

    protected abstract VideoData modelTransEntity(V model);

    @Override
    public int getCount() {
        if(mModels==null)
            return 0;
        return mModels.size();
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
