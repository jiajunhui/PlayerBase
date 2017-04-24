package com.kk.taurus.playerbase.cover.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.utils.CommonUtils;
import com.kk.taurus.playerbase.widget.BasePlayer;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 *
 * 覆盖层基类，默认显示状态为GONE。
 *
 */

public abstract class BaseCover implements ICover , View.OnClickListener,PlayerObserver,OnCoverEventListener{

    protected Context mContext;
    private View mCoverView;
    private BaseCoverObserver coverObserver;
    protected int mScreenW,mScreenH;
    protected WeakReference<BasePlayer> player;
    private OnCoverEventListener onCoverEventListener;
    protected boolean coverEnable = true;
    protected boolean adListFinish = true;
    protected boolean isNetError = false;

    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            _handleMessage(msg);
        }
    };

    protected void _handleMessage(Message msg){

    }

    public BaseCover(Context context){
        this(context,null);
    }

    public BaseCover(Context context, BaseCoverObserver coverObserver){
        this.mContext = context;
        this.coverObserver = coverObserver;
        initBaseInfo(context);
        handCoverView(context);
        if(this.coverObserver!=null){
            this.coverObserver.onCoverViewInit(mCoverView);
        }
        setDefaultGone();
        findView();
        afterFindView();
    }

    protected void afterFindView() {

    }

    protected void setDefaultGone() {
        setCoverVisibility(View.GONE);
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

    protected <V> V findViewById(int id){
        return (V) mCoverView.findViewById(id);
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

    public void onRefreshDataAdapter(BaseVideoDataAdapter dataAdapter){
        if(coverObserver!=null){
            coverObserver.onRefreshDataAdapter(dataAdapter);
        }
    }

    public void onRefreshCoverData(CoverData data) {
        if(coverObserver!=null){
            coverObserver.onDataChange(data);
        }
    }

    protected boolean isVisibilityGone(){
        return getView().getVisibility()!=View.VISIBLE;
    }

    @Override
    public String getString(int resId) {
        if(mContext!=null){
            return mContext.getString(resId);
        }
        return null;
    }

    @Override
    public View getView() {
        return mCoverView;
    }

    @Override
    public int getCoverType() {
        return COVER_TYPE_BUSINESS;
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

    protected void onDestroy(){
        player.clear();
        mHandler.removeCallbacks(null);
    }

    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        this.player = new WeakReference<>(player);
        this.onCoverEventListener = onCoverEventListener;
        if(coverObserver!=null){
            coverObserver.onBindCover(this);
        }
    }

    protected void releaseFocusToDpadCover(){
        if(getPlayer()!=null){
            getPlayer().dPadRequestFocus();
        }
    }

    protected BasePlayer getPlayer(){
        if(player!=null){
            return player.get();
        }
        return null;
    }


    //--------------------------------------------------------------------------------------------

    //*************************************************************************************
    // for some event
    //*************************************************************************************

    /**
     * on receive cover event
     * @param eventCode
     * @param bundle
     */
    public void onCoverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        if(coverObserver!=null){
            coverObserver.onNotifyPlayEvent(eventCode, bundle);
        }
    }

    protected void notifyCoverEvent(int eventCode, Bundle bundle){
        if(onCoverEventListener!=null){
            onCoverEventListener.onCoverEvent(eventCode, bundle);
        }
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {
        if(coverObserver!=null){
            coverObserver.onNotifyConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        if(coverObserver!=null){
            coverObserver.onNotifyErrorEvent(eventCode, bundle);
        }
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        if(coverObserver!=null){
            coverObserver.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        }
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        isNetError = false;
        if(coverObserver!=null){
            coverObserver.onNotifyNetWorkConnected(networkType);
        }
    }

    @Override
    public void onNotifyNetWorkError() {
        isNetError = true;
        if(coverObserver!=null){
            coverObserver.onNotifyNetWorkError();
        }
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        adListFinish = false;
        if(coverObserver!=null){
            coverObserver.onNotifyAdPrepared(adVideos);
        }
    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {
        if(coverObserver!=null){
            coverObserver.onNotifyAdStart(adVideo);
        }
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        adListFinish = isAllFinish;
        if(coverObserver!=null){
            coverObserver.onNotifyAdFinish(data, isAllFinish);
        }
    }
}
