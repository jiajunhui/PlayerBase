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

package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.listener.OnHolderListener;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;

/**
 * Created by Taurus on 2017/12/9.
 */

public class OutlineHolder extends ContentHolder<HolderData> {

    private DefaultPlayer mPlayer;
    private TextView mPlay, mRoundRect, mOvalRect;

    private final String url = "http://jiajunhui.cn/video/crystalliz.flv";

    public OutlineHolder(Context context) {
        super(context);
    }

    public OutlineHolder(Context context, OnHolderListener onHolderListener) {
        super(context, onHolderListener);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.fragment_outline);
        mPlayer = getViewById(R.id.player);
        mPlay = getViewById(R.id.tv_play);
        mRoundRect = getViewById(R.id.tv_round_rect);
        mOvalRect = getViewById(R.id.tv_oval_rect);

        mPlay.setOnClickListener(this);
        mOvalRect.setOnClickListener(this);
        mRoundRect.setOnClickListener(this);

        mPlayer.setElevationShadow(25f);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_play:
                playVideo();
                break;
            case R.id.tv_round_rect:
                mPlayer.setRoundRectShape(45f);
                break;
            case R.id.tv_oval_rect:
                mPlayer.setOvalRectShape();
                break;
        }
    }

    public void onHidden(){
        mPlayer.destroy();
    }

    private void playVideo(){
        VideoData data = new VideoData(url);
        bindReceivers();
        mPlayer.stop();
        mPlayer.setDataSource(data);
        mPlayer.start();
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();

    }

    private void bindReceivers() {
        DefaultReceiverCollections receiverCollections = new DefaultReceiverCollections(mContext);
        receiverCollections.setDefaultPlayerLoadingCover().setDefaultPlayerErrorCover().setDefaultPlayerGestureCover();
        mPlayer.bindReceiverCollections(receiverCollections);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayer!=null){
            mPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayer!=null){
            mPlayer.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy();
        }
    }
}
