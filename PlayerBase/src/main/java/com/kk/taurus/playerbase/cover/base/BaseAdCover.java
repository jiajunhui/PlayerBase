package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.CoverObserver;
import com.kk.taurus.playerbase.inter.IAdCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public abstract class BaseAdCover extends BaseCover implements IAdCover{

    public static final String KEY = "ad_cover";
    protected View mAdBox;
    protected ImageView mIvPic;
    protected TextView mTvTimer;

    protected boolean adFinish;

    public BaseAdCover(Context context, CoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        setCoverVisibility(View.GONE);
        mAdBox = findViewById(R.id.cover_player_ad_box);
        mIvPic = findViewById(R.id.cover_player_ad_image_view_pic);
        mTvTimer = findViewById(R.id.cover_player_ad_text_view_timer);
    }

    @Override
    public void setAdCoverState(boolean state) {
        setCoverVisibility(state?View.VISIBLE:View.GONE);
    }

    @Override
    public void setImagePicState(boolean state) {
        if(mIvPic!=null){
            mIvPic.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setAdTimerState(boolean state) {
        if(mTvTimer!=null){
            mTvTimer.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setAdTimerText(String text) {
        if(mTvTimer!=null){
            mTvTimer.setText(text);
        }
    }

    @Override
    public void onNotifyAdPreparedStart(List<BaseAdVideo> adVideos) {
        super.onNotifyAdPreparedStart(adVideos);
        adFinish = false;
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        super.onNotifyAdFinish(data, isAllFinish);
        if(isAllFinish){
            adFinish = true;
        }
    }
}
