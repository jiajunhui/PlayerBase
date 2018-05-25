package com.kk.taurus.playerbase.assist;

import android.os.Bundle;

import com.kk.taurus.playerbase.event.EventKey;

public class OnAssistPlayEventHandler extends BaseEventAssistHandler<AssistPlay> {

    @Override
    public void requestPause(AssistPlay assistPlay, Bundle bundle) {
        if(assistPlay.isInPlaybackState()){
            assistPlay.pause();
        }else{
            assistPlay.stop();
            assistPlay.reset();
        }
    }

    @Override
    public void requestResume(AssistPlay assistPlay, Bundle bundle) {
        if(assistPlay.isInPlaybackState()){
            assistPlay.resume();
        }else{
            assistPlay.rePlay(0);
        }
    }

    @Override
    public void requestSeek(AssistPlay assistPlay, Bundle bundle) {
        int pos = 0;
        if(bundle!=null){
            pos = bundle.getInt(EventKey.INT_DATA);
        }
        assistPlay.seekTo(pos);
    }

    @Override
    public void requestStop(AssistPlay assistPlay, Bundle bundle) {
        assistPlay.stop();
    }

    @Override
    public void requestReset(AssistPlay assist, Bundle bundle) {
        assist.reset();
    }

    @Override
    public void requestRetry(AssistPlay assistPlay, Bundle bundle) {
        int pos = 0;
        if(bundle!=null){
            pos = bundle.getInt(EventKey.INT_DATA);
        }
        assistPlay.rePlay(pos);
    }

    @Override
    public void requestReplay(AssistPlay assistPlay, Bundle bundle) {
        assistPlay.rePlay(0);
    }

}
