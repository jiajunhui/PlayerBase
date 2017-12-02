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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.adapter.ListVideoAdapter;
import com.taurus.playerbaselibrary.bean.VideosInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class LocalVideoListHolder extends ContentHolder<VideosInfo> implements ListVideoAdapter.OnItemListener {

    private RecyclerView mRecycler;
    private ListVideoAdapter mAdapter;
    private List<VideoItem> videoItems = new ArrayList<>();

    private OnLocalVideoListHolderListener onLocalVideoListHolderListener;

    public LocalVideoListHolder(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.fragment_local_videos);
        mRecycler = getViewById(R.id.recycler);
    }

    public void setOnLocalVideoListHolderListener(OnLocalVideoListHolderListener onLocalVideoListHolderListener) {
        this.onLocalVideoListHolderListener = onLocalVideoListHolderListener;
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapter = new ListVideoAdapter(mContext,videoItems);
        mAdapter.setRecycler(mRecycler);
        mAdapter.setOnItemListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void refreshView() {
        super.refreshView();
        videoItems.clear();
        videoItems.addAll(mData.getVideoItems());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onIntentToDetail(VideoItem item, int position) {
        if(onLocalVideoListHolderListener!=null){
            onLocalVideoListHolderListener.onIntentToDetail(item, position);
        }
    }

    @Override
    public void onFullScreen() {
        if(onLocalVideoListHolderListener!=null){
            onLocalVideoListHolderListener.onFullScreen();
        }
    }

    public void onHidden(){
        if(mAdapter!=null){
            mAdapter.destroy(true);
            mAdapter.resetNotify();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            mAdapter.destroy(true);
        }
    }

    public interface OnLocalVideoListHolderListener{
        void onIntentToDetail(VideoItem item, int position);
        void onFullScreen();
    }

}
