package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.kk.taurus.baseframe.base.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.adapter.OnlineVideoAdapter;
import com.taurus.playerbaselibrary.bean.VideoEntity;
import com.taurus.playerbaselibrary.bean.VideoResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class OnlineVideosHolder extends ContentHolder<VideoResult> implements XRecyclerView.LoadingListener, OnlineVideoAdapter.OnItemClickListener {

    private XRecyclerView mRecycler;
    private OnlineVideoAdapter mAdapter;
    private List<VideoEntity> mVideoList = new ArrayList<>();
    private OnlineHolderListener onlineHolderListener;
    private boolean refresh = true;

    public OnlineVideosHolder(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.fragment_online_videos);
        mRecycler = getViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecycler.setLoadingListener(this);
    }

    @Override
    public void onHolderCreated(Bundle savedInstanceState) {
        super.onHolderCreated(savedInstanceState);
        mAdapter = new OnlineVideoAdapter(mContext,mVideoList);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void refreshView() {
        super.refreshView();
        if(mData.V9LG4B3A0==null)
            return;
        if(refresh){
            mVideoList.clear();
        }
        mRecycler.refreshComplete();
        mRecycler.loadMoreComplete();
        mVideoList.addAll(mData.V9LG4B3A0);
        mAdapter.notifyDataSetChanged();
    }

    public void setOnlineHolderListener(OnlineHolderListener onlineHolderListener) {
        this.onlineHolderListener = onlineHolderListener;
    }

    @Override
    public void onRefresh() {
        refresh = true;
        if(onlineHolderListener!=null){
            onlineHolderListener.onRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        refresh = false;
        if(onlineHolderListener!=null){
            onlineHolderListener.onLoadMore();
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        if(onlineHolderListener!=null){
            onlineHolderListener.onItemClick(mVideoList.get(position));
        }
    }

    public interface OnlineHolderListener{
        void onRefresh();
        void onLoadMore();
        void onItemClick(VideoEntity videoEntity);
    }
}
