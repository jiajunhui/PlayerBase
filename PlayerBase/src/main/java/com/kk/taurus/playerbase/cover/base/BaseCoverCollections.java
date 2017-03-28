package com.kk.taurus.playerbase.cover.base;

import android.content.Context;

import com.kk.taurus.playerbase.inter.ICoverCollections;
import com.kk.taurus.playerbase.callback.CoverObserver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseCoverCollections<T> implements ICoverCollections {

    protected Context mContext;
    protected T mData;
    protected List<CoverObserver<T>> mCoverObservers = new ArrayList<>();
    protected LinkedHashMap<String,BaseCover> mCoverMap = new LinkedHashMap<>();

    public BaseCoverCollections(Context context){
        this.mContext = context;
    }

    protected void putCover(String key, BaseCover cover){
        mCoverMap.put(key, cover);
    }

    public BaseCoverCollections build(){
        onCoversHasInit(mContext);
        return this;
    }

    protected void onCoversHasInit(Context context) {
        if(mCoverMap!=null){
            for(String key:mCoverMap.keySet()){
                BaseCover cover = mCoverMap.get(key);
                if(cover!=null){
                    mCoverObservers.add(cover.getCoverObserver());
                }
            }
        }
    }

    @Override
    public BaseCover getCover(String key) {
        return mCoverMap.get(key);
    }

    @Override
    public List<BaseCover> getCovers() {
        List<BaseCover> covers = new ArrayList<>();
        if(mCoverMap!=null){
            for(String key:mCoverMap.keySet()){
                BaseCover cover = mCoverMap.get(key);
                covers.add(cover);
            }
        }
        return covers;
    }

    public void refreshData(T data){
        this.mData = data;
        for(CoverObserver observer : mCoverObservers){
            if(observer!=null){
                observer.onDataChange(data);
            }
        }
    }

}
