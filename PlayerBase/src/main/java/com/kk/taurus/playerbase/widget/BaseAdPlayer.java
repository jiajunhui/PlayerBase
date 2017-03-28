package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.PlayData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseAdPlayer extends BaseSettingPlayer {

    private PlayData mPlayData;
    private boolean adListFinish = true;
    private int mAdIndex;
    private List<BaseAdVideo> adVideos;

    public BaseAdPlayer(@NonNull Context context) {
        super(context);
    }

    public BaseAdPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseAdPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onPlayerEvent(int eventCode, Bundle bundle) {
        super.onPlayerEvent(eventCode, bundle);
        onHandleAdPlay(eventCode, bundle);
    }

    @Override
    protected void onErrorEvent(int eventCode, Bundle bundle) {
        super.onErrorEvent(eventCode, bundle);
        onHandleErrorEvent(eventCode, bundle);
    }

    @Override
    public void playData(@NonNull PlayData data) {
        this.mPlayData = data;
        if(data.isNeedAdPlay()){
            adListFinish = false;
            this.adVideos = data.getAdVideos();
            mAdIndex = 0;
            onNotifyAdPreparedStart(adVideos);
            startPlayAdVideos();
        }else{
            adListFinish = true;
            onNotifyAdFinish(null,true);
            setAndStartVideo(data);
        }
    }

    private void setAndStartVideo(@NonNull PlayData data) {
        updatePlayerType(data.getData().getPlayerType());
        setDataSource(data.getData());
        start(data.getData().getStartPos());
    }

    private void startPlayAdVideos() {
        if(mAdIndex>adVideos.size()-1){
            adListFinish = true;
            onNotifyAdFinish(adVideos.get(adVideos.size()-1),true);
            setAndStartVideo(mPlayData);
            return;
        }
        BaseAdVideo adVideo = adVideos.get(mAdIndex);
        updatePlayerType(adVideo.getPlayerType());
        setDataSource(adVideo);
        start(adVideo.getStartPos());
        mAdIndex++;
    }


    private void onHandleAdPlay(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_PLAY_COMPLETE:
                if(!adListFinish){
                    onNotifyAdFinish(adVideos.get(mAdIndex-1),false);
                    startPlayAdVideos();
                }
                break;
        }
    }

    private void onHandleErrorEvent(int eventCode, Bundle bundle) {
        if(adListFinish)
            return;
        if(eventCode!=OnErrorListener.ERROR_CODE_NET_ERROR){
            startPlayAdVideos();
        }
    }
}
