package com.kk.taurus.playerbase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.kk.taurus.playerbase.widget.BaseSinglePlayer;

/**
 * Created by Taurus on 2017/3/28.
 */

public class DefaultPlayer extends BasePlayer {

    private final String TAG = "DefaultPlayer";

    public DefaultPlayer(@NonNull Context context) {
        super(context);
    }

    public DefaultPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getPlayerWidget(Context context) {
        destroyInternalPlayer();
        Log.d(TAG,"init player : " + PlayerType.getInstance().getPlayerPath(getPlayerType()));
        mInternalPlayer = (BaseSinglePlayer) ConfigLoader.getPlayerInstance(mAppContext,getPlayerType());
        if(mInternalPlayer !=null){
            mInternalPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode, Bundle bundle) {
                    onErrorEvent(errorCode,bundle);
                }
            });
            mInternalPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    DefaultPlayer.this.onPlayerEvent(eventCode,bundle);
                }
            });
        }else{
            return new FrameLayout(mAppContext);
        }
        return mInternalPlayer;
    }
}
