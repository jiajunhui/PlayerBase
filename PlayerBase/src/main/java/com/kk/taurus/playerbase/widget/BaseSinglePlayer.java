package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.ISinglePlayer;
import com.kk.taurus.playerbase.setting.DecodeMode;
import com.kk.taurus.playerbase.setting.ViewType;

/**
 * Created by Taurus on 2017/3/25.
 */

public abstract class BaseSinglePlayer extends FrameLayout implements ISinglePlayer {

    protected int mStatus = STATUS_IDLE;
    private OnErrorListener mOnErrorListener;
    private OnPlayerEventListener mOnPlayerEventListener;
    protected int startSeekPos;
    private DecodeMode mDecodeMode;
    private ViewType mViewType;

    public BaseSinglePlayer(Context context) {
        super(context);
        init(context);
    }

    protected void onStartSeek() {
        if(startSeekPos > 0){
            seekTo(startSeekPos);
            startSeekPos = -1;
        }
    }

    protected void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initPlayerView(context);
    }

    private void initPlayerView(Context context) {
        addView(getPlayerView(context),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public abstract View getPlayerView(Context context);

    public void setDecodeMode(DecodeMode mDecodeMode){
        this.mDecodeMode = mDecodeMode;
    }

    public void setViewType(ViewType mViewType) {
        this.mViewType = mViewType;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    public void onClickResume() {

    }

    public void setOnErrorListener(OnErrorListener onErrorListener){
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    protected void onErrorEvent(int eventCode, Bundle bundle){
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(eventCode,bundle);
        }
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        if(mOnPlayerEventListener!=null){
            mOnPlayerEventListener.onPlayerEvent(eventCode,bundle);
        }
    }

    public int getStatus() {
        return mStatus;
    }

    @Override
    public void destroy() {
        mOnErrorListener = null;
        mOnPlayerEventListener = null;
    }

}
