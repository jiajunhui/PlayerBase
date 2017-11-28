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

package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.GestureObserver;
import com.kk.taurus.playerbase.callback.OnAdCoverClickListener;
import com.kk.taurus.playerbase.inter.IAdCover;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/28.
 */

public abstract class BaseAdCover extends BaseCover implements IAdCover,GestureObserver{

    public static final String KEY = "ad_cover";
    protected View mAdBox;
    protected View mAdTimerBox;
    protected ImageView mIvPic;
    protected TextView mTvTimer;

    protected boolean adFinish;

    protected BaseAdVideo adVideo;

    protected OnAdCoverClickListener onAdCoverClickListener;

    public BaseAdCover(Context context){
        super(context);
    }

    public BaseAdCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        mAdBox = findViewById(R.id.cover_player_ad_box);
        mAdTimerBox = findViewById(R.id.cover_player_ad_timer_box);
        mIvPic = findViewById(R.id.cover_player_ad_image_view_pic);
        mTvTimer = findViewById(R.id.cover_player_ad_text_view_timer);
    }

    @Override
    public void setOnAdCoverClickListener(OnAdCoverClickListener onAdCoverClickListener) {
        this.onAdCoverClickListener = onAdCoverClickListener;
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
        if(mAdTimerBox!=null){
            mAdTimerBox.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setAdTimerText(String text) {
        if(mTvTimer!=null){
            mTvTimer.setText(text);
        }
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        super.onNotifyAdPrepared(adVideos);
        adFinish = false;
    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {
        super.onNotifyAdStart(adVideo);
        this.adVideo = adVideo;
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        super.onNotifyAdFinish(data, isAllFinish);
        if(isAllFinish){
            adFinish = true;
        }
    }

    @Override
    public void onGestureSingleTab(MotionEvent event) {
    }

    @Override
    public void onGestureDoubleTab(MotionEvent event) {
    }

    @Override
    public void onGestureDown(MotionEvent event) {
    }

    @Override
    public void onGestureScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    }

    @Override
    public void onGestureHorizontalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onGestureRightVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onGestureLeftVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onGestureEnableChange(boolean enable) {

    }

    @Override
    public void onGestureEnd() {

    }
}
