package com.kk.taurus.playerbase.cover.base;

import android.content.Context;

import com.kk.taurus.playerbase.inter.ICoverCollections;
import com.kk.taurus.playerbase.setting.CoverData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseCoverCollections implements ICoverCollections {

    protected Context mContext;
    protected CoverData mData;
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

    }

    @Override
    public <T> T getCover(String key) {
        return (T) mCoverMap.get(key);
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

    public void refreshData(CoverData data){
        this.mData = data;
        if(mCoverMap!=null){
            for(String key:mCoverMap.keySet()){
                mCoverMap.get(key).onRefreshCoverData(data);
            }
        }
    }

}
