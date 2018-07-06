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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Taurus on 2018/3/17.
 */

public final class ReceiverGroup implements IReceiverGroup{

    private Map<String, IReceiver> mReceivers;
    private Set<String> mKeySet;
    private List<OnReceiverGroupChangeListener> mOnReceiverGroupChangeListeners;

    private GroupValue mGroupValue;

    public ReceiverGroup(){
        this(null);
    }

    public ReceiverGroup(GroupValue groupValue){
        mReceivers = new ConcurrentHashMap<>(16);
        mOnReceiverGroupChangeListeners = new CopyOnWriteArrayList<>();
        if(groupValue==null){
            mGroupValue = new GroupValue();
        }else{
            mGroupValue = groupValue;
        }
    }

    @Override
    public void addOnReceiverGroupChangeListener(
            OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        if(mOnReceiverGroupChangeListeners.contains(onReceiverGroupChangeListener))
            return;
        mOnReceiverGroupChangeListeners.add(onReceiverGroupChangeListener);
    }

    @Override
    public void removeOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        mOnReceiverGroupChangeListeners.remove(onReceiverGroupChangeListener);
    }

    void callBackOnReceiverAdd(String key, IReceiver receiver){
        for(OnReceiverGroupChangeListener listener:mOnReceiverGroupChangeListeners){
            listener.onReceiverAdd(key, receiver);
        }
    }

    void callBackOnReceiverRemove(String key, IReceiver receiver){
        for(OnReceiverGroupChangeListener listener:mOnReceiverGroupChangeListeners){
            listener.onReceiverRemove(key, receiver);
        }
    }

    @Override
    public void addReceiver(String key, IReceiver receiver){
        ((BaseReceiver)receiver).setKey(key);
        receiver.bindGroup(this);
        //call back method onReceiverBind().
        receiver.onReceiverBind();
        mReceivers.put(key, receiver);
        mKeySet = mReceivers.keySet();
        //call back on receiver add
        callBackOnReceiverAdd(key, receiver);
    }

    @Override
    public void removeReceiver(String key) {
        IReceiver receiver = mReceivers.remove(key);
        if(receiver!=null){
            //call back on receiver remove
            callBackOnReceiverRemove(key, receiver);
            //call back method onReceiverUnBind().
            receiver.onReceiverUnBind();
        }
    }

    @Override
    public void forEach(OnLoopListener onLoopListener) {
        forEach(null, onLoopListener);
    }

    @Override
    public void forEach(OnReceiverFilter filter, OnLoopListener onLoopListener) {
        if(mKeySet==null)
            return;
        for (String s : mKeySet) {
            IReceiver receiver = mReceivers.get(s);
            if(filter==null || filter.filter(receiver))
                onLoopListener.onEach(receiver);
        }
    }

    @Override
    public <T extends IReceiver> T getReceiver(String key) {
        if(mReceivers!=null)
            return (T) mReceivers.get(key);
        return null;
    }

    @Override
    public GroupValue getGroupValue() {
        return mGroupValue;
    }

    @Override
    public void clearReceivers(){
        if(mKeySet==null)
            return;
        for (String key : mKeySet) {
            removeReceiver(key);
        }
        mReceivers.clear();
    }

}
