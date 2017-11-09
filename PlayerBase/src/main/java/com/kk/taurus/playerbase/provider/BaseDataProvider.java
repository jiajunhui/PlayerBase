package com.kk.taurus.playerbase.provider;

import android.os.Bundle;

import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.widget.BasePlayer;

/**
 * Created by mtime on 2017/10/19.
 */

public abstract class BaseDataProvider implements IDataProvider {

    protected OnProviderListener mOnProviderListener;
    protected BasePlayer mPlayer;

    public void bindPlayer(BasePlayer player){
        this.mPlayer = player;
    }

    @Override
    public void setOnProviderListener(OnProviderListener onProviderListener) {
        this.mOnProviderListener = onProviderListener;
    }

    protected void sendPlayerEvent(int eventCode, Bundle bundle){
        if(mPlayer!=null){
            mPlayer.sendEvent(eventCode, bundle);
        }
    }

}
