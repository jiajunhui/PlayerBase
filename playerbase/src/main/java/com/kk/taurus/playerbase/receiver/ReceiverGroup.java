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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Taurus on 2018/3/17.
 */

public final class ReceiverGroup implements IReceiverGroup{

    private Map<String, IReceiver> mReceivers;
    private Set<String> mKeySet;

    private OnReceiverGroupChangeListener mOnReceiverGroupChangeListener;

    private GroupValue mGroupValue;

    public ReceiverGroup(){
        this(null);
    }

    public ReceiverGroup(GroupValue groupValue){
        mReceivers = new ConcurrentHashMap<>(16);
        if(groupValue==null){
            mGroupValue = new GroupValue();
        }else{
            mGroupValue = groupValue;
        }
    }

    @Override
    public void setOnReceiverGroupChangeListener(
            OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        this.mOnReceiverGroupChangeListener = onReceiverGroupChangeListener;
    }

    @Override
    public void addReceiver(String key, IReceiver receiver){
        receiver.bindGroup(this);
        //call back method onReceiverBind().
        receiver.onReceiverBind();
        mReceivers.put(key, receiver);
        mKeySet = mReceivers.keySet();
        if(mOnReceiverGroupChangeListener!=null)
            mOnReceiverGroupChangeListener.onReceiverAdd(key, receiver);
    }

    @Override
    public void removeReceiver(String key) {
        IReceiver receiver = mReceivers.remove(key);
        //call back method onReceiverUnBind().
        if(receiver!=null)
            receiver.onReceiverUnBind();
        if(mOnReceiverGroupChangeListener!=null)
            mOnReceiverGroupChangeListener.onReceiverRemove(key, receiver);
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
