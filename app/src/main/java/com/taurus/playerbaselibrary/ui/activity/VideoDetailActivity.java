package com.taurus.playerbaselibrary.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.uiframe.a.TitleBarActivity;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.v.BaseTitleBarHolder;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.kk.taurus.uiframe.v.d.DefaultTitleBarHolder;
import com.taurus.playerbaselibrary.bean.OnlineVideoItem;
import com.taurus.playerbaselibrary.holder.VideoDetailHolder;

public class VideoDetailActivity extends TitleBarActivity<HolderData,VideoDetailHolder> {

    private OnlineVideoItem item;
    private VideoData videoData;

    @Override
    public void onParseIntent() {
        item = (OnlineVideoItem) getIntent().getSerializableExtra("data");
        videoData = new VideoData(item.getUrl());
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        }

        getTitleBarHolder().setTitle(item.getUrl());

        keepScreenOn();

        getUserContentHolder().startPlay(videoData);

    }

    private DefaultTitleBarHolder getTitleBarHolder(){
        BaseTitleBarHolder titleBarHolder = getUserHolder().titleBarHolder;
        return  (DefaultTitleBarHolder)titleBarHolder;
    }

    @Override
    public ContentHolder onBindContentHolder() {
        return new VideoDetailHolder(this,this);
    }
}
