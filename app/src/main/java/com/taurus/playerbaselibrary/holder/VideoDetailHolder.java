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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.DecoderTypeEntity;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.PlayerTypeEntity;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.utils.TimeUtil;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.listener.OnHolderListener;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.callback.OnCompleteCallBack;
import com.taurus.playerbaselibrary.cover.AppControllerCover;
import com.taurus.playerbaselibrary.cover.PlayCompleteCover;

import java.util.Map;

/**
 * Created by Taurus on 2017/12/3.
 */

public class VideoDetailHolder extends ContentHolder<HolderData> implements OnPlayerEventListener {

    private DefaultPlayer mPlayer;

    private DefaultReceiverCollections mCoverCollections;
    private PlayCompleteCover completeCover;

    private TextView mTvInfo;
    private Button mBtnToggle;

    private VideoData mVideoData;

    private int mPos;

    public VideoDetailHolder(Context context) {
        super(context);
    }

    public VideoDetailHolder(Context context, OnHolderListener onHolderListener) {
        super(context, onHolderListener);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_video_detail);
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();
        mPlayer = getViewById(R.id.player);
        mTvInfo = getViewById(R.id.tv_info);
        mBtnToggle = getViewById(R.id.btn_toggle_decoder);
        mBtnToggle.setOnClickListener(this);
        mCoverCollections = new DefaultReceiverCollections(mContext);
        mCoverCollections
                .setDefaultPlayerLoadingCover()
                .setDefaultPlayerGestureCover()
                .setDefaultPlayerErrorCover()
                .addCover("appcover",new AppControllerCover(mContext))
                .addCover(PlayCompleteCover.KEY,completeCover = new PlayCompleteCover(mContext,null)).build();;

        mPlayer.bindReceiverCollections(mCoverCollections);

        if(completeCover!=null){
            completeCover.setOnCompleteListener(new OnCompleteCallBack(){
                @Override
                public void onReplay(PlayCompleteCover completeCover) {
                    super.onReplay(completeCover);
                    mPlayer.rePlay(0);
                }
            });
        }

        mPlayer.setOnPlayerEventListener(this);
    }

    public void startPlay(VideoData videoData){
        this.mVideoData = videoData;
        updateInfo();
        mPlayer.setDataSource(videoData);
        mPlayer.start(mPos);
    }

    private void updateInfo() {
        StringBuilder sb = new StringBuilder();
        if(mVideoData!=null){
            sb.append("视频: ").append(mVideoData.getData()).append("\n\n");
            sb.append("时长: ").append(TimeUtil.getTime(mPlayer.getDuration())).append("\n\n");
            String playerStr;
            if(mPlayer.getWidgetMode()== IPlayer.WIDGET_MODE_DECODER){
                int defaultPlayerType = DecoderType.getInstance().getDefaultPlayerType();
                String decoderPath = DecoderType.getInstance().getDecoderPath(defaultPlayerType);
                playerStr = decoderPath;
            }else{
                int defaultPlayerType = PlayerType.getInstance().getDefaultPlayerType();
                String playerPath = PlayerType.getInstance().getPlayerPath(defaultPlayerType);
                playerStr = playerPath;
            }
            sb.append("解码器: ").append(playerStr).append("\n");
        }
        mTvInfo.setText(sb.toString());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        mPos = mPlayer.getCurrentPosition();
        switch (v.getId()){
            case R.id.btn_toggle_decoder:
                if(mPlayer.getWidgetMode()== IPlayer.WIDGET_MODE_DECODER){
                    int defaultPlayerType = DecoderType.getInstance().getDefaultPlayerType();
                    Map<Integer, DecoderTypeEntity> decoderTypes = DecoderType.getInstance().getDecoderTypes();
                    for(Map.Entry<Integer, DecoderTypeEntity> entry:decoderTypes.entrySet()){
                        if(entry.getKey()!=defaultPlayerType){
                            DecoderType.getInstance().setDefaultDecoderType(entry.getKey());
                            mPlayer.updatePlayerType(entry.getKey());
                        }
                    }
                }else{
                    int defaultPlayerType = PlayerType.getInstance().getDefaultPlayerType();
                    Map<Integer, PlayerTypeEntity> decoderTypes = PlayerType.getInstance().getPlayerTypes();
                    for(Map.Entry<Integer, PlayerTypeEntity> entry:decoderTypes.entrySet()){
                        if(entry.getKey()!=defaultPlayerType){
                            PlayerType.getInstance().setDefaultPlayerType(entry.getKey());
                            mPlayer.updatePlayerType(entry.getKey());
                        }
                    }
                }
                startPlay(mVideoData);
                break;
        }
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.EVENT_CODE_RENDER_START:
                updateInfo();
                break;
        }
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
            mPlayer.destroy(true);
        }
    }
}
