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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Taurus on 2018/4/20.
 *
 * This class is a bridge of data sharing between the receivers.
 *
 */

public final class GroupValue {

    private Map<String,Object> mValueMap;
    private Map<IReceiverGroup.OnGroupValueUpdateListener, String[]> mListenerKeys;

    private List<IReceiverGroup.OnGroupValueUpdateListener> mOnGroupValueUpdateListeners;

    public GroupValue(){
        mValueMap = new ConcurrentHashMap<>();
        mListenerKeys = new ConcurrentHashMap<>();
        mOnGroupValueUpdateListeners = new CopyOnWriteArrayList<>();
    }

    //If you want to listen to changes in some data,
    //you can register a listener to implement it.
    public void registerOnGroupValueUpdateListener(
            IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener){
        if(mOnGroupValueUpdateListeners.contains(onGroupValueUpdateListener))
            return;
        mOnGroupValueUpdateListeners.add(onGroupValueUpdateListener);
        //sort it for Arrays.binarySearch();
        String[] keyArrays = onGroupValueUpdateListener.filterKeys();
        Arrays.sort(keyArrays);
        mListenerKeys.put(onGroupValueUpdateListener, keyArrays);
        //when listener add, if user observe keys in current KeySet, call back it.
        checkCurrentKeySet(onGroupValueUpdateListener);
    }

    private void checkCurrentKeySet(
            IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener) {
        Set<String> keys = mValueMap.keySet();
        for (String key : keys) {
            if (containsKey(mListenerKeys.get(onGroupValueUpdateListener), key)) {
                onGroupValueUpdateListener.onValueUpdate(key, mValueMap.get(key));
            }
        }
    }

    public void unregisterOnGroupValueUpdateListener(
            IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener){
        mListenerKeys.remove(onGroupValueUpdateListener);
        mOnGroupValueUpdateListeners.remove(onGroupValueUpdateListener);
    }

    public void clearOnGroupValueUpdateListeners(){
        mOnGroupValueUpdateListeners.clear();
    }

    public void clearValues(){
        mValueMap.clear();
    }

    public void putBoolean(String key, boolean value){
        put(key, value);
    }

    public void putInt(String key, int value){
        put(key, value);
    }

    public void putString(String key, String value){
        put(key, value);
    }

    public void putFloat(String key, float value){
        put(key, value);
    }

    public void putLong(String key, long value){
        put(key, value);
    }

    public void putDouble(String key, double value){
        put(key, value);
    }

    public void putObject(String key, Object value){
        put(key, value);
    }

    private void put(String key, Object value){
        mValueMap.put(key, value);
        callBackValueUpdate(key, value);
    }

    private void callBackValueUpdate(String key, Object value) {
        List<IReceiverGroup.OnGroupValueUpdateListener> mCallbacks = new ArrayList<>();
        //filter callbacks
        for (IReceiverGroup.OnGroupValueUpdateListener listener : mOnGroupValueUpdateListeners) {
            if (containsKey(mListenerKeys.get(listener), key)) {
                mCallbacks.add(listener);
            }
        }
        //call back
        for(IReceiverGroup.OnGroupValueUpdateListener callback : mCallbacks){
            callback.onValueUpdate(key, value);
        }
    }

    private boolean containsKey(String[] keys, String nowKey){
        if(keys!=null && keys.length>0){
            return Arrays.binarySearch(keys, nowKey) >= 0;
        }
        return false;
    }

    public <T> T get(String key){
        Object o = mValueMap.get(key);
        try {
            if(o!=null){
                return (T) o;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue){
        Boolean bool = get(key);
        if(bool==null)
            return defaultValue;
        return bool;
    }

    public int getInt(String key){
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue){
        Integer integer = get(key);
        if(integer==null)
            return defaultValue;
        return integer;
    }

    public String getString(String key){
        String s = get(key);
        return s;
    }

    public float getFloat(String key){
        return getFloat(key, 0f);
    }

    public float getFloat(String key, float defaultValue){
        Float f = get(key);
        if(f==null)
            return defaultValue;
        return f;
    }

    public long getLong(String key){
        return getLong(key, 0);
    }

    public long getLong(String key, long defaultValue){
        Long l = get(key);
        if(l==null)
            return defaultValue;
        return l;
    }

    public double getDouble(String key){
        return getDouble(key, 0);
    }

    public double getDouble(String key, double defaultValue){
        Double d = get(key);
        if(d==null)
            return defaultValue;
        return d;
    }

}
