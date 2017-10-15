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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.base.ContentHolder;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.adapter.VideoListAdapter;
import com.taurus.playerbaselibrary.adapter.VideoListAdapter2;
import com.taurus.playerbaselibrary.bean.VideosInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class LocalVideoListHolder extends ContentHolder<VideosInfo> implements VideoListAdapter2.OnItemClickListener {

    private RecyclerView mRecycler;
    private VideoListAdapter2 mAdapter;
    private List<VideoItem> videoItems = new ArrayList<>();
    private OnLocalVideoListener onLocalVideoListener;

    private BasePlayer mPlayer;

    public LocalVideoListHolder(Context context) {
        super(context);
    }

    public void setOnLocalVideoListener(OnLocalVideoListener onLocalVideoListener) {
        this.onLocalVideoListener = onLocalVideoListener;
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.fragment_local_videos);
        mRecycler = getViewById(R.id.recycler);
    }

    @Override
    public void onHolderCreated(Bundle savedInstanceState) {
        super.onHolderCreated(savedInstanceState);

        mPlayer = new DefaultPlayer(mContext);
        DefaultReceiverCollections receiverCollections = new DefaultReceiverCollections(mContext);
        receiverCollections.buildDefault();
        mPlayer.bindCoverCollections(receiverCollections);
        mPlayer.setUseDefaultRender(false);


        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapter = new VideoListAdapter2(mContext,videoItems, mRecycler);
        mAdapter.bindPlayer(mPlayer);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayer!=null){
            mPlayer.resume();
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
    public void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy();
        }
    }

    @Override
    public void refreshView() {
        super.refreshView();
        videoItems.clear();
        videoItems.addAll(mData.getVideoItems());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        VideoItem item = videoItems.get(position);
        if(onLocalVideoListener!=null){
            onLocalVideoListener.onItemClick(item,position);
        }
    }

    public interface OnLocalVideoListener{
        void onItemClick(VideoItem item, int position);
    }
}
