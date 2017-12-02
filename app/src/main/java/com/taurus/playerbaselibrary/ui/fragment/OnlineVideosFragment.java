package com.taurus.playerbaselibrary.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.kk.taurus.uiframe.f.StateFragment;
import com.kk.taurus.uiframe.i.HolderData;
import com.taurus.playerbaselibrary.R;
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
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
            "http://www.w3school.com.cn/example/html5/mov_bbb.mp4",
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    };

    private int[] mIds = {
            R.mipmap.icon_trailer,
            R.mipmap.icon_mov_bbb,
            R.mipmap.icon_big_buck_bunny
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
        for(int i=0;i<3;i++){
            videoItem = new OnlineVideoItem();
            videoItem.setUrl(mUrls[i]);
            videoItem.setResId(mIds[i]);
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
