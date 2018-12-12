package com.kk.taurus.playerbase.record;

import com.kk.taurus.playerbase.log.PLog;

/**
 * Created by Taurus on 2018/12/12.
 */
class PlayRecord {

    private final String TAG = "PlayRecord";
    private static PlayRecord i;

    private RecordCache mRecordCache;

    private PlayRecord(){
        mRecordCache = new RecordCache(PlayRecordManager.MaxRecordCount);
    }

    public static PlayRecord get(){
        if(null==i){
            synchronized (PlayRecord.class){
                if(null==i){
                    i = new PlayRecord();
                }
            }
        }
        return i;
    }

    public int record(String key, int record){
        if(key==null)
            return -1;
        PLog.d(TAG,"<<Save>> : record = " + record + " ,key = " + key);
        return mRecordCache.putRecord(key, record);
    }

    public int removeRecord(String key){
        if(key==null)
            return -1;
        return mRecordCache.removeRecord(key);
    }

    public int getRecord(String key){
        if(key==null)
            return -1;
        int record = mRecordCache.getRecord(key);
        PLog.d(TAG,"<<Get>> : record = " + record + ", key = " + key);
        return record;
    }

    public void clearRecord(){
        mRecordCache.clearRecord();
    }

    public void destroy(){
        clearRecord();
        i = null;
    }

}
