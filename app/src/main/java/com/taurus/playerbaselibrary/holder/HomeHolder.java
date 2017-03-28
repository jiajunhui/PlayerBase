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
 * Created by Taurus on 2017/3/28.
 */

public class HomeHolder extends ContentHolder<VideosInfo> implements VideoListAdapter.OnItemClickListener {

    private RecyclerView mRecycler;
    private VideoListAdapter mAdapter;
    private List<VideoItem> videoItems = new ArrayList<>();
    private OnHomeHolderListener onHomeHolderListener;

    public HomeHolder(Context context) {
        super(context);
    }

    public void setOnHomeHolderListener(OnHomeHolderListener onHomeHolderListener) {
        this.onHomeHolderListener = onHomeHolderListener;
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_home);
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
        if(onHomeHolderListener!=null){
            onHomeHolderListener.onItemClick(item,position);
        }
    }

    public interface OnHomeHolderListener{
        void onItemClick(VideoItem item, int position);
    }

}
