package com.kk.taurus.avplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.EventConstant;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.receiver.ICover;

/**
 * Created by Taurus on 2018/4/20.
 */

public class ErrorCover extends BaseCover {

    private TextView mRetry;

    private boolean mErrorShow;

    public ErrorCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        mRetry = findViewById(R.id.tv_retry);

        mRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorState(false);
                notifyReceiverEvent(EventConstant.EVENT_CODE_ERROR_REQUEST_RETRY, null);
            }
        });
    }

    private void setErrorState(boolean state){
        mErrorShow = state;
        setCoverVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        if(!mErrorShow){
            setErrorState(true);
            notifyReceiverEvent(EventConstant.EVENT_CODE_ERROR_SHOW, null);
        }
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onPrivateEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_error_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_HIGH;
    }
}
