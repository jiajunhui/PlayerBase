package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;

/**
 * Created by Taurus on 2017/3/27.
 */

public class DefaultPlayerErrorCover extends BasePlayerErrorCover {

    public DefaultPlayerErrorCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_player_error_state_cover,null);
    }

    @Override
    protected void findView() {
        super.findView();
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRetry();
            }
        });
    }

    private void onClickRetry() {
        if(player==null)
            return;
        player.rePlay(0);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                setErrorState(false);
                break;
        }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        super.onNotifyErrorEvent(eventCode, bundle);
        switch (eventCode){
            case OnErrorListener.ERROR_CODE_COMMON:
                setErrorState(true);
                break;
        }
    }

    @Override
    public void setErrorState(boolean state) {
        super.setErrorState(state);
        notifyCoverEvent(state? OnCoverEventListener.EVENT_CODE_ON_PLAYER_ERROR_SHOW
                :OnCoverEventListener.EVENT_CODE_ON_PLAYER_ERROR_HIDDEN,null);
    }
}
