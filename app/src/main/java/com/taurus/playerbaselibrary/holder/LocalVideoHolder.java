package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.baseframe.base.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.adapter.VideoListAdapter;
import com.taurus.playerbaselibrary.bean.VideosInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class LocalVideoHolder extends ContentHolder<VideosInfo> implements VideoListAdapter.OnItemClickListener {

    private RecyclerView mRecycler;
    private VideoListAdapter mAdapter;
    private List<VideoItem> videoItems = new ArrayList<>();
    private OnLocalVideoListener onLocalVideoListener;

    public LocalVideoHolder(Context context) {
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
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapter = new VideoListAdapter(mContext,videoItems);
        mAdapter.setOnItemClickListener(this);
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
