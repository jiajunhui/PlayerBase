package com.taurus.playerbaselibrary.cover;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.kk.taurus.playerbase.eventHandler.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.CornerCutCover;
import com.kk.taurus.playerbase.cover.DefaultPlayerErrorCover;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BasePlayerErrorCover;
import com.kk.taurus.playerbase.inter.ICover;

import java.util.List;

/**
 * Created by Taurus on 2017/4/18.
 */

public class PlayerErrorCover extends DefaultPlayerErrorCover {

    protected int mErrorPos;

    private final int MSG_CODE_ERROR_TIME = 901;
    protected boolean hasStart;
    private boolean needReplayOnResumeToForeground;

    public PlayerErrorCover(Context context) {
        super(context);
    }

    @Override
    protected void _handleMessage(Message msg) {
        super._handleMessage(msg);
        switch (msg.what){
            case MSG_CODE_ERROR_TIME:
                onHandleTimerCounter(getCurrentPosition(),getDuration(),getBufferPercentage());
                sendDelayErrorMsg();
                break;
        }
    }

    @Override
    protected void onClickRetry() {

    }

    private void startErrorTimer(){
        removeErrorTimerMsg();
        mHandler.sendEmptyMessage(MSG_CODE_ERROR_TIME);
    }

    private void sendDelayErrorMsg(){
        removeErrorTimerMsg();
        mHandler.sendEmptyMessageDelayed(MSG_CODE_ERROR_TIME,1000);
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        super.onCoverEvent(eventCode, bundle);
    }

    @Override
    protected void handlePlayEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                hasStart = false;
                break;
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                hasStart = true;
                setErrorState(false);
                break;
        }
    }

    protected synchronized void showErrorState() {
        if(!isVisibilityGone()){
            return;
        }
        stop();
        setErrorState(true);
    }

    protected void handleErrorEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnErrorListener.ERROR_CODE_COMMON:
                showErrorState();
                break;
        }
    }

    @Override
    public void setErrorState(boolean state) {
        super.setErrorState(state);
        if(state && getPlayer()!=null){
            List<BaseEventReceiver> covers = getPlayer().getReceiverCollections().getReceivers();
            for(BaseEventReceiver cover : covers){
                if(!(cover instanceof BaseCover))
                    continue;
                if(cover instanceof BasePlayerErrorCover
                        || cover instanceof CornerCutCover){
                    continue;
                }
                ((BaseCover)cover).setCoverVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNotifyNetWorkError() {
        super.onNotifyNetWorkError();
        startErrorTimer();
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        super.onNotifyNetWorkConnected(networkType);
        onConnectedReplay();
    }

    protected void onConnectedReplay() {
        if(!isVisibilityGone() && !isNetError){
            rePlay(adListFinish? mErrorPos:getErrorStartPos());
            setErrorState(false);
        }
    }

    private void removeErrorTimerMsg() {
        mHandler.removeMessages(MSG_CODE_ERROR_TIME);
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        super.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        if(duration > 0 && duration > curr){
            mErrorPos = curr;
        }
    }

    protected void onHandleTimerCounter(int curr, int duration, int bufferPercentage) {
        if(isNetError){
            if(!isVisibilityGone()){
                return;
            }
            if(!hasStart){
                showErrorState();
                return;
            }
            if(!adListFinish){
                showErrorState();
            }else{
                if(getPlayer()!=null && !getPlayer().isExpectedBufferAvailable()){
                    showErrorState();
                }
            }
        }else{
            if(needReplayOnResumeToForeground){
                rePlay(adListFinish? mErrorPos:getErrorStartPos());
            }
        }
    }

    @Override
    public void rePlay(final int msc) {
        if(getActivity()!=null && isForeground(mContext,getActivity().getClass().getName())){
            needReplayOnResumeToForeground = false;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    replay2(msc);
                }
            });
        }else{
            needReplayOnResumeToForeground = true;
        }
    }

    private void replay2(int msc){
        super.rePlay(msc);
        removeErrorTimerMsg();
    }

    private boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected int getErrorStartPos(){
        return 10;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeErrorTimerMsg();
    }

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_MEDIUM;
    }
}
