package com.taurus.playerbaselibrary.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverObserver;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.callback.OnCompleteListener;
import com.taurus.playerbaselibrary.inter.IPlayCompleteCover;

/**
 * Created by Taurus on 2017/3/30.
 */

public class PlayCompleteCover extends BaseCover implements IPlayCompleteCover{

    public static final String KEY = "complete_cover";
    private OnCompleteListener onCompleteListener;
    private TextView mTvReplay;

    public PlayCompleteCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        mTvReplay = findViewById(R.id.tv_replay);
        mTvReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCompleteListener!=null){
                    onCompleteListener.onReplay(PlayCompleteCover.this);
                }
            }
        });
    }

    @Override
    public View initCoverLayout(Context context) {
        return View.inflate(context, R.layout.layout_play_complete_cover,null);
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                if(getPlayer()!=null){
                    setCompleteCoverState(true);
                }
                break;
        }
    }

    @Override
    public void setCompleteCoverState(boolean state) {
        setCoverVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
}
