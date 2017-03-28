package com.kk.taurus.playerbase.cover.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.inter.MSG;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.utils.CommonUtils;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseCover implements ICover ,View.OnClickListener,PlayerObserver,OnCoverEventListener{

    protected Context mContext;
    private View mCoverView;
    private CoverObserver coverObserver;
    protected int mScreenW,mScreenH;
    protected IPlayer player;
    private OnCoverEventListener onCoverEventListener;
    protected boolean coverEnable = true;
    protected boolean adListFinish = true;

    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            _handleMessage(msg);
        }
    };

    protected void _handleMessage(Message msg){
        switch (msg.what){
            case MSG.MSG_CODE_PLAYING:
                if(player==null)
                    return;
                int curr = player.getCurrentPosition();
                int duration = player.getDuration();
                int bufferPercentage = player.getBufferPercentage();
                int bufferPos = bufferPercentage*duration/100;
                onNotifyPlayTimerCounter(curr,duration,bufferPos);
                sendPlayMsg();
                break;
        }
    }

    public BaseCover(Context context, CoverObserver coverObserver){
        this.mContext = context;
        this.coverObserver = coverObserver;
        initBaseInfo(context);
        handCoverView(context);
        if(this.coverObserver!=null){
            this.coverObserver.onCoverViewInit(mCoverView);
        }
        findView();
    }

    private void handCoverView(Context context){
        if(coverObserver!=null){
            View customView = coverObserver.initCustomCoverView(context);
            if(customView!=null){
                mCoverView = customView;
            }else{
                mCoverView = initCoverLayout(context);
            }
        }else{
            mCoverView = initCoverLayout(context);
        }
    }

    protected void initBaseInfo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenW = displayMetrics.widthPixels;
        mScreenH = displayMetrics.heightPixels;
    }

    protected abstract void findView();

    protected <T> T findViewById(int id){
        return (T) mCoverView.findViewById(id);
    }

    @Override
    public abstract View initCoverLayout(Context context);

    public void setCoverEnable(boolean enable) {
        if(!enable){
            setCoverVisibility(View.GONE);
        }
        this.coverEnable = enable;
    }

    @Override
    public void setCoverVisibility(int visibility) {
        if(!coverEnable)
            return;
        if(mCoverView!=null){
            mCoverView.setVisibility(visibility);
            if(coverObserver!=null){
                coverObserver.onCoverVisibilityChange(getView(),visibility);
            }
        }
    }

    protected boolean isVisibilityGone(){
        return getView().getVisibility()!=View.VISIBLE;
    }

    @Override
    public View getView() {
        return mCoverView;
    }

    @Override
    public CoverObserver getCoverObserver() {
        return coverObserver;
    }

    protected Activity getActivity(){
        if(mContext!=null && mContext instanceof Activity)
            return (Activity) mContext;
        return null;
    }

    @Override
    public int getScreenOrientation() {
        if(mContext==null)
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        if(!(mContext instanceof Activity))
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return CommonUtils.getScreenOrientation((Activity) mContext,mScreenW,mScreenH);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * on receive cover event
     * @param eventCode
     * @param bundle
     */
    public void onCoverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                sendPlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:

                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                sendPlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
                onDestroy();
                player = null;
                break;
        }
    }

    protected void sendPlayMsg() {
        removePlayMsg();
        mHandler.sendEmptyMessageDelayed(MSG.MSG_CODE_PLAYING,1000);
    }

    protected void removePlayMsg() {
        mHandler.removeMessages(MSG.MSG_CODE_PLAYING);
    }

    protected void onDestroy(){
        mHandler.removeCallbacks(null);
        mHandler.removeMessages(MSG.MSG_CODE_PLAYING);
        mHandler.removeMessages(MSG.MSG_CODE_DELAY_HIDDEN_CONTROLLER);
    }

    @Override
    public void onBindPlayer(IPlayer player,OnCoverEventListener onCoverEventListener) {
        this.player = player;
        this.onCoverEventListener = onCoverEventListener;
    }

    protected void notifyCoverEvent(int eventCode, Bundle bundle){
        if(onCoverEventListener!=null){
            onCoverEventListener.onCoverEvent(eventCode, bundle);
        }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {

    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {

    }

    @Override
    public void onNotifyNetWorkError() {

    }

    @Override
    public void onNotifyAdPreparedStart(List<BaseAdVideo> adVideos) {
        adListFinish = false;
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        adListFinish = isAllFinish;
    }
}
