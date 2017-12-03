package com.taurus.playerbaselibrary.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.kk.taurus.uiframe.f.StateFragment;
import com.kk.taurus.uiframe.i.HolderData;
import com.taurus.playerbaselibrary.bean.OnlineVideoItem;
import com.taurus.playerbaselibrary.holder.OnlineVideosHolder;
import com.taurus.playerbaselibrary.ui.activity.VideoDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class OnlineVideosFragment extends StateFragment<HolderData,OnlineVideosHolder> implements OnlineVideosHolder.OnlineHolderListener {

    private String[] mUrls = {
            "http://jiajunhui.cn/video/edwin_rolling_in_the_deep.flv",
            "http://jiajunhui.cn/video/allsharestar.mp4",
            "http://jiajunhui.cn/video/crystalliz.flv",
            "http://jiajunhui.cn/video/big_buck_bunny.mp4",
            "http://jiajunhui.cn/video/trailer.mp4"
    };

    @Override
    public OnlineVideosHolder onBindContentHolder() {
        return new OnlineVideosHolder(mContext);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        getUserContentHolder().setOnlineHolderListener(this);
    }

    @Override
    public void onLoadState() {
        super.onLoadState();
        OnlineVideoItem videoItem;
        List<OnlineVideoItem> result = new ArrayList<>();
        for(int i=0;i<5;i++){
            videoItem = new OnlineVideoItem();
            videoItem.setUrl(mUrls[i]);
            result.add(videoItem);
        }
        getUserContentHolder().refreshList(result);
    }

    @Override
    public void onItemClick(OnlineVideoItem videoEntity) {
        Intent intent = new Intent(mContext, VideoDetailActivity.class);
        intent.putExtra("data",videoEntity);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
