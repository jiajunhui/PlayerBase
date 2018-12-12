package com.kk.taurus.playerbase.record;

import android.os.Bundle;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.player.IPlayerProxy;

/**
 * Created by Taurus on 2018/12/12.
 */
public class RecordProxyPlayer implements IPlayerProxy {

    private PlayValueGetter mPlayValueGetter;

    private String mKey;

    public RecordProxyPlayer(PlayValueGetter valueGetter){
        this.mPlayValueGetter = valueGetter;
    }

    @Override
    public void onDataSourceReady(DataSource dataSource) {
        //right now change DataSource, record it.
        record();
        mKey = getKey(dataSource);
    }

    @Override
    public void onIntentStop() {
        record();
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
                //on pause, record play position.
                record();
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                //on play complete, reset play record.
                PlayRecord.get().record(mKey, 0);
                break;
        }
    }

    private void record(){
        if(isInPlaybackState()){
            PlayRecord.get().record(mKey, getCurrentPosition());
        }
    }

    private int getCurrentPosition(){
        if(mPlayValueGetter!=null)
            return mPlayValueGetter.getCurrentPosition();
        return 0;
    }

    private boolean isInPlaybackState() {
        int state = mPlayValueGetter!=null?mPlayValueGetter.getState():IPlayer.STATE_IDLE;
        return state!= IPlayer.STATE_END
                && state!= IPlayer.STATE_ERROR
                && state!= IPlayer.STATE_IDLE
                && state!= IPlayer.STATE_INITIALIZED
                && state!= IPlayer.STATE_STOPPED;
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    private String getKey(DataSource dataSource){
        return PlayRecordManager.getRecordKeyProvider().generatorKey(dataSource);
    }

    public int getRecord(DataSource dataSource){
        return PlayRecord.get().getRecord(getKey(dataSource));
    }

}
