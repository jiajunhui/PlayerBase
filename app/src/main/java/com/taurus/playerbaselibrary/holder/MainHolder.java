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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.adapter.ListVideoAdapter;
import com.taurus.playerbaselibrary.ui.activity.FullScreenActivity;
import com.taurus.playerbaselibrary.ui.activity.SecondActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/11/19.
 */

public class MainHolder extends ContentHolder<HolderData> implements ListVideoAdapter.OnItemListener {

    private List<VideoItem> mVideoItems = new ArrayList<>();
    private ListVideoAdapter mAdapter;
    private RecyclerView mRecycler;
    private RelativeLayout mRenderContainer;
    private int[] rootLocation = new int[2];
    private boolean isFullScreen;

    public MainHolder(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_test_list_video_play);
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();
        mRecycler = getViewById(R.id.recycler);
        mRenderContainer = getViewById(R.id.renderViewContainer);
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mRootView.getLocationOnScreen(rootLocation);
            }
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false));
        mAdapter = new ListVideoAdapter(mContext,mVideoItems);
        mAdapter.setOnItemListener(this);
        mAdapter.setRecycler(mRecycler);
        mRecycler.setAdapter(mAdapter);
    }

    public void updateItems(List<VideoItem> items){
        this.mVideoItems.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onIntentToDetail(VideoItem item, int position) {
        Intent intent = new Intent(mContext, SecondActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            isFullScreen = false;
            mRenderContainer.setVisibility(View.GONE);
            mRenderContainer.removeAllViews();
        }else{
            isFullScreen = true;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRenderContainer.getLayoutParams();
            layoutParams.topMargin = 0;
            layoutParams.width = -1;
            layoutParams.height = -1;
            mRenderContainer.setLayoutParams(layoutParams);

        }
    }

    public boolean onBackPressed() {
        if(isFullScreen){
            setOrientation(false);
            return true;
        }
        return false;
    }

    private void setOrientation(boolean landscape){
        ((Activity)mContext).setRequestedOrientation(landscape? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onFullScreen() {
        Intent intent = new Intent(mContext, FullScreenActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter!=null){
            mAdapter.destroy(true);
        }
    }
}
