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

package com.kk.taurus.playerbase.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.utils.CommonUtils;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseBindPlayerEvent extends BaseBindEventReceiver implements IPlayer {

    private boolean mNetError;
    private NetChangeReceiver mNetChangeReceiver;

    public BaseBindPlayerEvent(@NonNull Context context) {
        super(context);
    }

    public BaseBindPlayerEvent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseBindPlayerEvent(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        registerNetChangeReceiver();
    }

    private void registerNetChangeReceiver() {
        if(mAppContext!=null){
            mNetChangeReceiver = new NetChangeReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mAppContext.registerReceiver(mNetChangeReceiver,intentFilter);
        }
    }

    private void unRegisterNetChangeReceiver(){
        try {
            if(mAppContext!=null && mNetChangeReceiver!=null){
                mAppContext.unregisterReceiver(mNetChangeReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        onNotifyPlayEvent(eventCode, bundle);
    }

    protected void onErrorEvent(int eventCode, Bundle bundle){
        onNotifyErrorEvent(eventCode, bundle);
    }

    @Override
    public void doConfigChange(Configuration newConfig) {
        onNotifyConfigurationChanged(newConfig);
    }

    @Override
    public void destroy() {
        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
        unRegisterNetChangeReceiver();
        unbindCoverCollections();
    }

    private class NetChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if(CommonUtils.isNetworkConnected(context)){
                    if(mNetError){
                        mNetError = false;
                        onNotifyNetWorkConnected(CommonUtils.isWifi(context)? PlayerObserver.NETWORK_TYPE_WIFI:PlayerObserver.NETWORK_TYPE_MOBILE);
                    }
                }else{
                    mNetError = true;
                    onNotifyNetWorkError();
                }
            }
        }
    }
}
