/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.assist;

import android.os.Bundle;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.log.PLog;

/**
 * Created by Taurus on 2018/5/21.
 */
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
            requestRetry(assistPlay, bundle);
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

    @Override
    public void requestPlayDataSource(AssistPlay assist, Bundle bundle) {
        if(bundle!=null){
            DataSource data = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
            if(data==null){
                PLog.e("OnAssistPlayEventHandler","requestPlayDataSource need legal data source");
                return;
            }
            assist.stop();
            assist.setDataSource(data);
            assist.play();
        }
    }
}
