package com.taurus.playerbaselibrary.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.kk.taurus.baseframe.bean.PageState;
import com.kk.taurus.baseframe.ui.fragment.StateFragment;
import com.kk.taurus.http_helper.bean.XResponse;
import com.kk.taurus.http_helper.callback.BeanCallBack;
import com.kk.taurus.http_helper.callback.HttpCallBack;
import com.taurus.playerbaselibrary.bean.VideoEntity;
import com.taurus.playerbaselibrary.bean.VideoResult;
import com.taurus.playerbaselibrary.engine.API;
import com.taurus.playerbaselibrary.engine.DataEngine;
import com.taurus.playerbaselibrary.holder.OnlineVideosHolder;
import com.taurus.playerbaselibrary.ui.activity.VideoDetailActivity;

import okhttp3.Call;

/**
 * Created by Taurus on 2017/4/30.
 */

public class OnlineVideosFragment extends StateFragment<VideoResult,OnlineVideosHolder> implements OnlineVideosHolder.OnlineHolderListener {

    private int pageIndex;
    private Call mCall;

    @Override
    public OnlineVideosHolder getContentViewHolder(Bundle savedInstanceState) {
        return new OnlineVideosHolder(mContext);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mContentHolder.setOnlineHolderListener(this);
    }

    @Override
    public void loadState() {
        setPageState(PageState.loading());
        loadData();
    }

    private void loadData(){
        mCall = DataEngine.loadVideos(API.VIDEO_TYPE_HOT_ID, pageIndex, new BeanCallBack<VideoResult>() {
            @Override
            public void onResponseBean(VideoResult result) {
                if(result!=null){
                    setData(result);
                    setPageState(PageState.success());
                }
            }

            @Override
            public void onError(int errorType, XResponse response) {
                super.onError(errorType, response);
                if(errorType== HttpCallBack.ERROR_TYPE_NETWORK){
                    setPageState(PageState.errorNetWork());
                }else{
                    setPageState(PageState.error());
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        loadData();
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        loadData();
    }

    @Override
    public void onItemClick(VideoEntity videoEntity) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        intent.putExtra("data",videoEntity);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCall!=null){
            mCall.cancel();
        }
    }
}
