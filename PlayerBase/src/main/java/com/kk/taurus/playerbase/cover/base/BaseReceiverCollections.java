package com.kk.taurus.playerbase.cover.base;

import android.content.Context;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.inter.IReceiverCollections;
import com.kk.taurus.playerbase.setting.CoverData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseReceiverCollections implements IReceiverCollections {

    protected Context mContext;
    protected LinkedHashMap<String,BaseEventReceiver> mCoverMap = new LinkedHashMap<>();

    public BaseReceiverCollections(Context context){
        this.mContext = context;
    }

    protected void putCover(String key, BaseEventReceiver cover){
        mCoverMap.put(key, cover);
    }

    public BaseReceiverCollections build(){
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
    public List<BaseEventReceiver> getCovers() {
        List<BaseEventReceiver> covers = new ArrayList<>();
        if(mCoverMap!=null){
            for(String key:mCoverMap.keySet()){
                BaseEventReceiver cover = mCoverMap.get(key);
                covers.add(cover);
            }
        }
        return covers;
    }

    public void refreshDataAdapter(BaseVideoDataAdapter dataAdapter){
        if(mCoverMap!=null){
            for(String key:mCoverMap.keySet()){
                mCoverMap.get(key).onRefreshDataAdapter(dataAdapter);
            }
        }
    }

    public void refreshData(CoverData data){
        if(mCoverMap!=null){
            for(String key:mCoverMap.keySet()){
                mCoverMap.get(key).onRefreshCoverData(data);
            }
        }
    }

    public void refreshData(CoverData data,BaseCover... covers){
        for(BaseCover cover : covers){
            cover.onRefreshCoverData(data);
        }
    }

    public void clear(){
        if(mCoverMap!=null){
            mCoverMap.clear();
            mContext = null;
        }
    }

}
