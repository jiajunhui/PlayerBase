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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.kk.taurus.playerbase.log.PLog;

/**
 * Created by Taurus on 2018/3/17.
 */

public abstract class BaseReceiver implements IReceiver {

    private Context mContext;
    private OnReceiverEventListener mOnReceiverEventListener;

    private IReceiverGroup mHostGroup;

    /**
     * If you want to use Activity related functions or features,
     * please pass in the context of Activity.
     * Use it carefully to avoid memory leaks
     *
     * @param context
     *
     */
    public BaseReceiver(Context context){
        this.mContext = context;
    }

    /**
     * When the receiver is added to the group, callback this method.
     */
    public void onReceiverBind(){

    }

    /**
     * When the receiver is removed in the group, callback this method.
     */
    @Override
    public void onReceiverUnBind() {

    }

    /**
     * Bind the ReceiverGroup. This method is called by the inside of the framework.
     * Please do not call this method.
     *
     * @param receiverGroup
     */
    @Override
    public final void bindGroup(IReceiverGroup receiverGroup) {
        this.mHostGroup = receiverGroup;
    }

    protected final GroupValue getGroupValue(){
        return mHostGroup.getGroupValue();
    }

    /**
     * Bind the listener. This method is called by the inside of the framework.
     * Please do not call this method.
     *
     * @param onReceiverEventListener
     */
    @Override
    public final void bindReceiverEventListener(OnReceiverEventListener onReceiverEventListener) {
        this.mOnReceiverEventListener = onReceiverEventListener;
    }

    /**
     * A receiver sends an event, and the receiver in the same group can receive the event.
     * @param eventCode
     * @param bundle
     */
    protected final void notifyReceiverEvent(int eventCode, Bundle bundle){
        if(mOnReceiverEventListener!=null)
            mOnReceiverEventListener.onReceiverEvent(eventCode, bundle);
    }

    /**
     * Send an event to the specified receiver,
     * make sure that the key value you imported is correct.
     *
     * @param key The unique value of a receiver can be found.
     * @param eventCode
     * @param bundle
     */
    protected final void notifyReceiverPrivateEvent(
            @NonNull String key, int eventCode, Bundle bundle){
        if(mHostGroup!=null && !TextUtils.isEmpty(key)){
            IReceiver receiver = mHostGroup.getReceiver(key);
            if(receiver!=null){
                receiver.onPrivateEvent(eventCode, bundle);
            }else{
                PLog.e("BaseReceiver",
                        "not found receiver use you incoming key.");
            }
        }
    }

    protected final Context getContext(){
        return mContext;
    }

    //default tag is class simple name
    public Object getTag(){
        return this.getClass().getSimpleName();
    }

}
