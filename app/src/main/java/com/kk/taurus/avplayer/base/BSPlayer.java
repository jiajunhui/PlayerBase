package com.kk.taurus.avplayer.base;

import android.os.Bundle;
import android.view.ViewGroup;

import com.kk.taurus.playerbase.assist.RelationAssist;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.provider.IDataProvider;
import com.kk.taurus.playerbase.receiver.GroupValue;
import com.kk.taurus.playerbase.receiver.IReceiver;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaJunHui on 2018/6/19.
 */
public abstract class BSPlayer implements ISPayer {

    private RelationAssist mRelationAssist;

    protected BSPlayer(){
        mRelationAssist = onCreateRelationAssist();
        mOnPlayerEventListeners = new ArrayList<>();
        mOnErrorEventListeners = new ArrayList<>();
        mOnReceiverEventListeners = new ArrayList<>();
        onInit();
    }

    protected abstract RelationAssist onCreateRelationAssist();

    protected abstract void onInit();

    private List<OnPlayerEventListener> mOnPlayerEventListeners;
    private List<OnErrorEventListener> mOnErrorEventListeners;
    private List<OnReceiverEventListener> mOnReceiverEventListeners;

    @Override
    public void addOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        if(mOnPlayerEventListeners.contains(onPlayerEventListener))
            return;
        mOnPlayerEventListeners.add(onPlayerEventListener);
    }

    @Override
    public boolean removePlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        return mOnPlayerEventListeners.remove(onPlayerEventListener);
    }

    @Override
    public void addOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        if(mOnErrorEventListeners.contains(onErrorEventListener))
            return;
        mOnErrorEventListeners.add(onErrorEventListener);
    }

    @Override
    public boolean removeErrorEventListener(OnErrorEventListener onErrorEventListener){
        return mOnErrorEventListeners.remove(onErrorEventListener);
    }

    @Override
    public void addOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener){
        if(mOnReceiverEventListeners.contains(onReceiverEventListener))
            return;
        mOnReceiverEventListeners.add(onReceiverEventListener);
    }

    @Override
    public boolean removeReceiverEventListener(OnReceiverEventListener onReceiverEventListener){
        return mOnReceiverEventListeners.remove(onReceiverEventListener);
    }

    private OnPlayerEventListener mInternalPlayerEventListener =
            new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    onCallBackPlayerEvent(eventCode, bundle);
                    callBackPlayerEventListeners(eventCode, bundle);
                }
            };

    protected abstract void onCallBackPlayerEvent(int eventCode, Bundle bundle);

    private void callBackPlayerEventListeners(int eventCode, Bundle bundle) {
        for(OnPlayerEventListener listener:mOnPlayerEventListeners){
            listener.onPlayerEvent(eventCode, bundle);
        }
    }

    private OnErrorEventListener mInternalErrorEventListener =
            new OnErrorEventListener() {
                @Override
                public void onErrorEvent(int eventCode, Bundle bundle) {
                    onCallBackErrorEvent(eventCode, bundle);
                    callBackErrorEventListeners(eventCode, bundle);
                }
            };

    protected abstract void onCallBackErrorEvent(int eventCode, Bundle bundle);

    private void callBackErrorEventListeners(int eventCode, Bundle bundle) {
        for(OnErrorEventListener listener:mOnErrorEventListeners){
            listener.onErrorEvent(eventCode, bundle);
        }
    }

    private OnReceiverEventListener mInternalReceiverEventListener =
            new OnReceiverEventListener() {
                @Override
                public void onReceiverEvent(int eventCode, Bundle bundle) {
                    onCallBackReceiverEvent(eventCode, bundle);
                    callBackReceiverEventListeners(eventCode, bundle);
                }
            };

    protected abstract void onCallBackReceiverEvent(int eventCode, Bundle bundle);

    private void callBackReceiverEventListeners(int eventCode, Bundle bundle) {
        for(OnReceiverEventListener listener:mOnReceiverEventListeners){
            listener.onReceiverEvent(eventCode, bundle);
        }
    }

    private void attachListener(){
        mRelationAssist.setOnPlayerEventListener(mInternalPlayerEventListener);
        mRelationAssist.setOnErrorEventListener(mInternalErrorEventListener);
        mRelationAssist.setOnReceiverEventListener(mInternalReceiverEventListener);
    }

    @Override
    public GroupValue getGroupValue(){
        IReceiverGroup receiverGroup = getReceiverGroup();
        return receiverGroup==null?null:receiverGroup.getGroupValue();
    }

    @Override
    public void updateGroupValue(String key, Object value){
        GroupValue groupValue = getGroupValue();
        if(groupValue!=null){
            groupValue.putObject(key, value);
        }
    }

    @Override
    public void registerOnGroupValueUpdateListener(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener){
        GroupValue groupValue = getGroupValue();
        if(groupValue!=null){
            groupValue.registerOnGroupValueUpdateListener(onGroupValueUpdateListener);
        }
    }

    @Override
    public void unregisterOnGroupValueUpdateListener(IReceiverGroup.OnGroupValueUpdateListener onGroupValueUpdateListener){
        GroupValue groupValue = getGroupValue();
        if(groupValue!=null){
            groupValue.unregisterOnGroupValueUpdateListener(onGroupValueUpdateListener);
        }
    }

    @Override
    public void setReceiverGroup(IReceiverGroup receiverGroup){
        mRelationAssist.setReceiverGroup(receiverGroup);
    }

    @Override
    public IReceiverGroup getReceiverGroup(){
        return mRelationAssist.getReceiverGroup();
    }

    @Override
    public final void removeReceiver(String receiverKey) {
        IReceiverGroup receiverGroup = getReceiverGroup();
        if(receiverGroup!=null)
            receiverGroup.removeReceiver(receiverKey);
    }

    @Override
    public final void addReceiver(String key, IReceiver receiver) {
        IReceiverGroup receiverGroup = getReceiverGroup();
        if(receiverGroup!=null)
            receiverGroup.addReceiver(key, receiver);
    }

    @Override
    public void attachContainer(ViewGroup userContainer){
        attachContainer(userContainer, true);
    }

    public void attachContainer(ViewGroup userContainer, boolean updateRender){
        mRelationAssist.attachContainer(userContainer, updateRender);
    }

    @Override
    public void play(DataSource dataSource){
        play(dataSource, false);
    }

    @Override
    public void play(DataSource dataSource, boolean updateRender){
        onSetDataSource(dataSource);
        attachListener();
        mRelationAssist.setDataSource(dataSource);
        mRelationAssist.play(updateRender);
    }

    protected abstract void onSetDataSource(DataSource dataSource);

    @Override
    public void setDataProvider(IDataProvider dataProvider){
        mRelationAssist.setDataProvider(dataProvider);
    }

    @Override
    public boolean isInPlaybackState(){
        int state = getState();
        PLog.d("BSPlayer","isInPlaybackState : state = " + state);
        return state!= IPlayer.STATE_END
                && state!= IPlayer.STATE_ERROR
                && state!= IPlayer.STATE_IDLE
                && state!= IPlayer.STATE_INITIALIZED
                && state!= IPlayer.STATE_PLAYBACK_COMPLETE
                && state!= IPlayer.STATE_STOPPED;
    }

    @Override
    public boolean isPlaying() {
        return mRelationAssist.isPlaying();
    }

    @Override
    public int getCurrentPosition(){
        return mRelationAssist.getCurrentPosition();
    }

    @Override
    public int getState() {
        return mRelationAssist.getState();
    }

    @Override
    public void pause() {
        mRelationAssist.pause();
    }

    @Override
    public void resume() {
        mRelationAssist.resume();
    }

    @Override
    public void stop() {
        mRelationAssist.stop();
    }

    @Override
    public void reset() {
        mRelationAssist.reset();
    }

    @Override
    public void rePlay(int position) {
        mRelationAssist.rePlay(position);
    }

    @Override
    public void destroy() {
        mOnPlayerEventListeners.clear();
        mOnErrorEventListeners.clear();
        mOnReceiverEventListeners.clear();
        IReceiverGroup receiverGroup = getReceiverGroup();
        if(receiverGroup!=null){
            receiverGroup.clearReceivers();
        }
        mRelationAssist.destroy();
    }

}
