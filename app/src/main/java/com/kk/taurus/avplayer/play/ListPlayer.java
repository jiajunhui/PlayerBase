package com.kk.taurus.avplayer.play;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.cover.GestureCover;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.playerbase.assist.AssistPlay;
import com.kk.taurus.playerbase.assist.OnAssistPlayEventHandler;
import com.kk.taurus.playerbase.assist.RelationAssist;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;

import com.kk.taurus.avplayer.base.BSPlayer;

import java.lang.ref.WeakReference;

public class ListPlayer extends BSPlayer {

    private static ListPlayer i = null;

    private int mPlayPageIndex = 0;

    private ListPlayer(){}

    private OnHandleListener onHandleListener;

    private WeakReference<Activity> mActivityRefer;

    @Override
    protected RelationAssist onCreateRelationAssist() {
        RelationAssist assist = new RelationAssist(App.get().getApplicationContext());
        assist.setEventAssistHandler(new OnAssistPlayEventHandler(){
            @Override
            public void requestRetry(AssistPlay assistPlay, Bundle bundle) {
                if(PUtil.isTopActivity(mActivityRefer!=null?mActivityRefer.get():null)){
                    super.requestRetry(assistPlay, bundle);
                }
            }
        });
        return assist;
    }

    public void attachActivity(Activity activity){
        clearActivityRefer();
        mActivityRefer = new WeakReference<>(activity);
    }

    private void clearActivityRefer() {
        if(mActivityRefer!=null)
            mActivityRefer.clear();
    }

    @Override
    protected void onInit() {

    }

    public void setOnHandleListener(OnHandleListener onHandleListener) {
        this.onHandleListener = onHandleListener;
    }

    public static ListPlayer get(){
        if(i==null){
            synchronized (ListPlayer.class){
                if(i==null){
                    i = new ListPlayer();
                }
            }
        }
        return i;
    }

    public int getPlayPageIndex() {
        return mPlayPageIndex;
    }

    public void setPlayPageIndex(int playPageIndex) {
        this.mPlayPageIndex = playPageIndex;
    }

    public void setReceiverConfigState(Context context, int configState){
        IReceiverGroup receiverGroup = getReceiverGroup();
        if(receiverGroup==null){
            setReceiverGroup(ReceiverGroupManager.get().getLiteReceiverGroup(context));
        }
        switch (configState){
            case RECEIVER_GROUP_CONFIG_LIST_STATE:
                removeReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER);
                break;
            case RECEIVER_GROUP_CONFIG_DETAIL_PORTRAIT_STATE:
            case RECEIVER_GROUP_CONFIG_FULL_SCREEN_STATE:
                addReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER, new GestureCover(context));
                break;
        }
    }

    @Override
    protected void onCallBackPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    protected void onCallBackErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    protected void onCallBackReceiverEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                if(onHandleListener!=null)
                    onHandleListener.onBack();
                break;
            case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                if(onHandleListener!=null)
                    onHandleListener.onToggleScreen();
                break;
            case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                reset();
                break;
        }
    }

    @Override
    protected void onSetDataSource(DataSource dataSource) {

    }

    @Override
    public void destroy() {
        super.destroy();
        clearActivityRefer();
        i = null;
        onHandleListener = null;
    }

}
