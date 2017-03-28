package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BasePlayerLoadingCover;

/**
 * Created by Taurus on 2017/3/25.
 */

public class DefaultPlayerLoadingCover extends BasePlayerLoadingCover {

    private final String TAG = "_LoadingCover";

    public DefaultPlayerLoadingCover(Context context, CoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_player_loading_cover,null);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_ON_INTENT_TO_START:
                Log.d(TAG,"on intent to start......");
                setLoadingState(true);
                break;
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                Log.d(TAG,"on render start......");
                setLoadingState(false);
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                Log.d(TAG,"buffering start......");
                setLoadingState(true);
                break;

            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                Log.d(TAG,"buffering end......");
                setLoadingState(false);
                break;
        }
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        super.onCoverEvent(eventCode, bundle);
        switch (eventCode){
            case OnCoverEventListener.EVENT_CODE_ON_PLAYER_CONTROLLER_SHOW:
                Log.d(TAG,"controller show......");
                break;
            case OnCoverEventListener.EVENT_CODE_ON_PLAYER_CONTROLLER_HIDDEN:
                Log.d(TAG,"controller hidden......");
                break;
        }
    }
}
