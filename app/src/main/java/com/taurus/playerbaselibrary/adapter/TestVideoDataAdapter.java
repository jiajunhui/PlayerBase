package com.taurus.playerbaselibrary.adapter;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.setting.VideoData;
import com.taurus.playerbaselibrary.bean.VideoModel;

import java.util.List;

/**
 * Created by Taurus on 2017/4/19.
 */

public class TestVideoDataAdapter extends BaseVideoDataAdapter<VideoModel> {

    public TestVideoDataAdapter(List<VideoModel> models, int startIndex) {
        super(models, startIndex);
    }

    @Override
    public VideoData getLoopNextPlayEntity() {
        int nextIndex = nextIndex();
        if(nextIndex != -1){
            VideoModel videoModel = mModels.get(nextIndex);
            return modelTransEntity(videoModel);
        }
        return null;
    }

    @Override
    public VideoData getPlayEntity() {
        VideoModel videoModel = getItemModel();
        if(videoModel!=null)
            return modelTransEntity(videoModel);
        return null;
    }

    @Override
    protected VideoData modelTransEntity(VideoModel model) {
        if(model!=null)
            return new VideoData(model.getUrl());
        return null;
    }
}
