package com.kk.taurus.playerbase.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public abstract class BaseCoverBindPlayerObserver extends BaseBindCover implements IPlayer {

    private boolean mNetError;
    private NetChangeReceiver mNetChangeReceiver;

    public BaseCoverBindPlayerObserver(@NonNull Context context) {
        super(context);
    }

    public BaseCoverBindPlayerObserver(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCoverBindPlayerObserver(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initBaseInfo(Context context) {
        super.initBaseInfo(context);
        registerNetChangeReceiver();
    }

    @Override
    protected void onCoversHasInit(Context context) {
        super.onCoversHasInit(context);
        onBindPlayer(this,this);
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
    public void destroy() {
        onPlayerEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY,null);
        unRegisterNetChangeReceiver();
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
