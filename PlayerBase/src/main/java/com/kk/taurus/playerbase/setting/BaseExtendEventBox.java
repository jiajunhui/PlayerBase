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

package com.kk.taurus.playerbase.setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.utils.CommonUtils;
import com.kk.taurus.playerbase.inter.IEventBinder;

/**
 * Created by Taurus on 2017/11/18.
 */

public class BaseExtendEventBox {

    private Context mContext;
    private IEventBinder mEventBinder;
    private NetChangeReceiver mNetChangeReceiver;

    public BaseExtendEventBox(Context context, IEventBinder eventBinder){
        this.mContext = context;
        this.mEventBinder = eventBinder;
        onRegisterExtendEvent();
    }

    protected void onRegisterExtendEvent() {
        registerNetChangeReceiver();
    }

    private void registerNetChangeReceiver() {

        if(mContext!=null){
            mNetChangeReceiver = new NetChangeReceiver(mContext,mEventBinder);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(mNetChangeReceiver,intentFilter);
        }
    }

    public void destroyExtendBox(){
        unRegisterNetChangeReceiver();
    }

    private void unRegisterNetChangeReceiver(){
        try {
            if(mContext!=null && mNetChangeReceiver !=null){
                mNetChangeReceiver.destroy();
                mContext.unregisterReceiver(mNetChangeReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        if(mEventBinder!=null){
            mEventBinder.onBindPlayerEvent(eventCode, bundle);
        }
    }

    public static class NetChangeReceiver extends BroadcastReceiver {

        private boolean mNetError;
        private IEventBinder eventBinder;
        private Bundle mBundle;

        public NetChangeReceiver(Context context, IEventBinder eventBinder){
            this.eventBinder = eventBinder;
            mNetError = !CommonUtils.isNetworkConnected(context.getApplicationContext());
        }

        private void onPlayerEvent(int eventCode, Bundle bundle){
            if(eventBinder!=null){
                eventBinder.onBindPlayerEvent(eventCode, bundle);
            }
        }

        public void destroy(){
            eventBinder = null;
        }

        protected Bundle getBundle(){
            if(mBundle==null){
                mBundle = new Bundle();
            }
            mBundle.clear();
            return mBundle;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("NetChangeReceiver","CONNECTIVITY_ACTION");
                if(CommonUtils.isNetworkConnected(context)){
                    int networkType = CommonUtils.isWifi(context)? PlayerObserver.NETWORK_TYPE_WIFI:PlayerObserver.NETWORK_TYPE_MOBILE;
                    if(mNetError){
                        mNetError = false;
                        Bundle bundle = getBundle();
                        bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_INT_DATA,networkType);
                        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_NETWORK_CONNECTED,bundle);
                    }else{
                        Bundle bundle = getBundle();
                        bundle.putInt(OnPlayerEventListener.BUNDLE_KEY_INT_DATA,networkType);
                        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_NETWORK_CHANGE,bundle);
                    }
                }else{
                    mNetError = true;
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_NETWORK_ERROR,null);
                }
            }
        }
    }

}
