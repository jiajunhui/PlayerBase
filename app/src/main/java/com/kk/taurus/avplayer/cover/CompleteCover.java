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

package com.kk.taurus.avplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.BaseCover;

/**
 * Created by Taurus on 2018/4/20.
 */

public class CompleteCover extends BaseCover {

    private TextView mReplay;

    public CompleteCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        mReplay = findViewById(R.id.tv_replay);

        mReplay.setOnClickListener(mOnClickListener);

    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        if(getGroupValue().getBoolean(DataInter.Key.KEY_COMPLETE_SHOW)){
            setPlayCompleteState(true);
        }
    }

    @Override
    protected void onCoverDetachedToWindow() {
        super.onCoverDetachedToWindow();
        setCoverVisibility(View.GONE);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_replay:
                    requestReplay(null);
                    break;
            }
            setPlayCompleteState(false);
        }
    };

    private void setPlayCompleteState(boolean state){
        setCoverVisibility(state?View.VISIBLE:View.GONE);
        getGroupValue().putBoolean(DataInter.Key.KEY_COMPLETE_SHOW, state);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                setPlayCompleteState(false);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                setPlayCompleteState(true);
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_complete_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return levelMedium(20);
    }
}
