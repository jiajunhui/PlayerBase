package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IPlayerCoverHandle;
import com.kk.taurus.playerbase.inter.MSG;
import com.kk.taurus.playerbase.setting.AspectRatio;
import com.kk.taurus.playerbase.setting.Rate;

import java.util.List;

/**
 * Created by Taurus on 2017/4/20.
 */

public abstract class BasePlayerToolsReceiver extends BaseEventReceiver implements IPlayerCoverHandle{

    private final String TAG = "player_tool_receiver";

    public BasePlayerToolsReceiver(Context context) {
        super(context);
    }

    protected void _handleMessage(Message msg){
        super._handleMessage(msg);
        switch (msg.what){
            case MSG.MSG_CODE_PLAYING:
                if(player==null)
                    return;
                int curr = getCurrentPosition();
                int duration = getDuration();
                int bufferPercentage = getBufferPercentage();
                int bufferPos = bufferPercentage*duration/100;
                onNotifyPlayTimerCounter(curr,duration,bufferPos);
                sendPlayMsg();
                break;
        }
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        super.onNotifyPlayEvent(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE:
                Log.d(TAG,"EVENT_CODE_PLAYER_ON_SET_DATA_SOURCE");
                removePlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PREPARED:
                Log.d(TAG,"EVENT_CODE_PREPARED");
                startPlay();
                break;
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                Log.d(TAG,"EVENT_CODE_RENDER_START");
                startPlay();
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_START:
                Log.d(TAG,"EVENT_CODE_BUFFERING_START");
                break;
            case OnPlayerEventListener.EVENT_CODE_BUFFERING_END:
                Log.d(TAG,"EVENT_CODE_BUFFERING_END");
                sendPlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_STOP:
                Log.d(TAG,"EVENT_CODE_PLAYER_ON_STOP");
                removePlayMsg();
                break;
            case OnPlayerEventListener.EVENT_CODE_PLAYER_ON_DESTROY:
                Log.d(TAG,"EVENT_CODE_PLAYER_ON_DESTROY");
                onDestroy();
                player = null;
                break;
        }
    }

    protected void sendPlayMsg() {
        removePlayMsg();
        mHandler.sendEmptyMessageDelayed(MSG.MSG_CODE_PLAYING,1000);
    }

    protected void startPlay(){
        removePlayMsg();
        mHandler.sendEmptyMessage(MSG.MSG_CODE_PLAYING);
    }

    protected void removePlayMsg() {
        mHandler.removeMessages(MSG.MSG_CODE_PLAYING);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG.MSG_CODE_PLAYING);
    }

    //--------------------------------------------------------------------------------------------

    //#####################################################################################
    // for player handle
    //#####################################################################################

    @Override
    public void pause() {
        if(getPlayer()!=null){
            getPlayer().pause();
        }
    }

    @Override
    public void resume() {
        if(getPlayer()!=null){
            getPlayer().resume();
        }
    }

    @Override
    public void seekTo(int msc) {
        if(getPlayer()!=null){
            getPlayer().seekTo(msc);
        }
    }

    @Override
    public void stop() {
        if(getPlayer()!=null){
            getPlayer().stop();
        }
    }

    @Override
    public void rePlay(int msc) {
        if(getPlayer()!=null){
            getPlayer().rePlay(msc);
        }
    }

    @Override
    public boolean isPlaying() {
        if(getPlayer()!=null){
            return getPlayer().isPlaying();
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        if(getPlayer()!=null){
            return getPlayer().getCurrentPosition();
        }
        return 0;
    }

    @Override
    public int getDuration() {
        if(getPlayer()!=null){
            return getPlayer().getDuration();
        }
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        if(getPlayer()!=null){
            return getPlayer().getBufferPercentage();
        }
        return 0;
    }

    @Override
    public int getStatus() {
        if(getPlayer()!=null){
            return getPlayer().getStatus();
        }
        return 0;
    }

    @Override
    public Rate getCurrentDefinition() {
        if(getPlayer()!=null){
            return getPlayer().getCurrentDefinition();
        }
        return null;
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(getPlayer()!=null){
            return getPlayer().getVideoDefinitions();
        }
        return null;
    }

    @Override
    public void changeVideoDefinition(Rate rate) {
        if(getPlayer()!=null){
            getPlayer().changeVideoDefinition(rate);
        }
    }

    @Override
    public void setAspectRatio(AspectRatio aspectRatio) {
        if(getPlayer()!=null){
            getPlayer().setAspectRatio(aspectRatio);
        }
    }
}
