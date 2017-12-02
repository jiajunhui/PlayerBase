package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.adapter.OnlineVideoAdapter;
import com.taurus.playerbaselibrary.bean.OnlineVideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class OnlineVideosHolder extends ContentHolder<HolderData> implements OnlineVideoAdapter.OnItemClickListener {

    private RecyclerView mRecycler;
    private OnlineVideoAdapter mAdapter;
    private List<OnlineVideoItem> mVideoList = new ArrayList<>();
    private OnlineHolderListener onlineHolderListener;

    public OnlineVideosHolder(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.fragment_online_videos);
        mRecycler = getViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();

    }

    public void refreshList(List<OnlineVideoItem> list){
        mVideoList.clear();
        mVideoList.addAll(list);
        mAdapter = new OnlineVideoAdapter(mContext,mVideoList);
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void refreshView() {
        super.refreshView();

    }

    public void setOnlineHolderListener(OnlineHolderListener onlineHolderListener) {
        this.onlineHolderListener = onlineHolderListener;
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
        if(onlineHolderListener!=null){
            onlineHolderListener.onItemClick(mVideoList.get(position));
        }
    }

    public interface OnlineHolderListener{
        void onItemClick(OnlineVideoItem videoEntity);
    }
}
