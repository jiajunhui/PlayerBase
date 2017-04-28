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
    protected LinkedHashMap<String,BaseEventReceiver> mReceiverMap = new LinkedHashMap<>();

    public BaseReceiverCollections(Context context){
        this.mContext = context;
    }

    protected void putReceiver(String key, BaseEventReceiver receiver){
        mReceiverMap.put(key, receiver);
    }

    public BaseReceiverCollections build(){
        onReceiversHasInit(mContext);
        return this;
    }

    protected void onReceiversHasInit(Context context) {

    }

    @Override
    public <T> T getReceiver(String key) {
        return (T) mReceiverMap.get(key);
    }

    @Override
    public List<BaseEventReceiver> getReceivers() {
        List<BaseEventReceiver> receivers = new ArrayList<>();
        if(mReceiverMap !=null){
            for(String key: mReceiverMap.keySet()){
                BaseEventReceiver receiver = mReceiverMap.get(key);
                receivers.add(receiver);
            }
        }
        return receivers;
    }

    public void refreshDataAdapter(BaseVideoDataAdapter dataAdapter){
        if(mReceiverMap !=null){
            for(String key: mReceiverMap.keySet()){
                mReceiverMap.get(key).onRefreshDataAdapter(dataAdapter);
            }
        }
    }

    public void refreshData(CoverData data){
        refreshData(data,null);
    }

    public void refreshData(CoverData data,BaseEventReceiver[] receivers){
        if(receivers!=null && receivers.length>0){
            for(BaseEventReceiver receiver : receivers){
                receiver.onRefreshCoverData(data);
            }
        }else{
            if(mReceiverMap !=null){
                for(String key: mReceiverMap.keySet()){
                    mReceiverMap.get(key).onRefreshCoverData(data);
                }
            }
        }
    }

    public void clear(){
        if(mReceiverMap !=null){
            mReceiverMap.clear();
            mContext = null;
        }
    }

}
