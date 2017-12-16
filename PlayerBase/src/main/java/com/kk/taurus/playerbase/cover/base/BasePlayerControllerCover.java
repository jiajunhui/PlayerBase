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
import android.widget.SeekBar;
import android.widget.TextView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.callback.GestureObserver;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.inter.IPlayerControllerCover;
import com.kk.taurus.playerbase.utils.TimeUtil;
import com.kk.taurus.playerbase.view.BatteryView;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BasePlayerControllerCover extends BaseCover implements IPlayerControllerCover,GestureObserver{

    public static final String KEY = "player_controller_cover";
    protected View mTopContainer;
    protected View mBottomContainer;
    protected BatteryView mBatteryView;
    protected ImageView mIvBackIcon;
    protected ImageView mIvPlayState;
    protected TextView mTvVideoTitle;
    protected TextView mTvSystemTime;
    protected TextView mTvCurrTime;
    protected TextView mTvTotalTime;
    protected TextView mTvMergeTime;
    protected SeekBar mSeekBar;
    protected View.OnClickListener mOnBackClickListener;
    private boolean timerCounterUpdateEnable = true;

    public BasePlayerControllerCover(Context context){
        super(context);
    }

    @Override
    protected void findView() {
        mTopContainer = findViewById(R.id.cover_player_controller_top_container);
        mBottomContainer = findViewById(R.id.cover_player_controller_bottom_container);
        mIvBackIcon = findViewById(R.id.cover_player_controller_image_view_back_icon);
        mTvVideoTitle = findViewById(R.id.cover_player_controller_text_view_video_title);
        mBatteryView = findViewById(R.id.cover_player_controller_view_battery_state);
        mIvPlayState = findViewById(R.id.cover_player_controller_image_view_play_state);
        mTvSystemTime = findViewById(R.id.cover_player_controller_text_view_system_time);
        mTvCurrTime = findViewById(R.id.cover_player_controller_text_view_curr_time);
        mTvTotalTime = findViewById(R.id.cover_player_controller_text_view_total_time);
        mTvMergeTime = findViewById(R.id.cover_player_controller_text_view_merge_time);
        mSeekBar = findViewById(R.id.cover_player_controller_seek_bar);
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        super.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
        if(timerCounterUpdateEnable){
            updateSeekBar(curr, duration, bufferPercentage);
            setPlayTime(curr,duration);
        }
    }

    @Override
    public void setOnBackClickListener(View.OnClickListener onBackClickListener) {
        this.mOnBackClickListener = onBackClickListener;
    }

    @Override
    public void setVideoTitle(String text) {
        if(mTvVideoTitle!=null){
            mTvVideoTitle.setText(text);
        }
    }

    @Override
    public void setTopContainerState(boolean state) {
        if(mTopContainer!=null){
            mTopContainer.setVisibility(state?View.VISIBLE:View.GONE);
            updateSystemTime();
        }
    }

    @Override
    public void setBottomContainerState(boolean state) {
        if(mBottomContainer!=null){
            mBottomContainer.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setControllerState(boolean state) {
        setTopContainerState(state);
        setBottomContainerState(state);
        if(state){
            onControllerShow();
        }
        notifyCoverEvent(state? OnCoverEventListener.EVENT_CODE_ON_PLAYER_CONTROLLER_SHOW
                :OnCoverEventListener.EVENT_CODE_ON_PLAYER_CONTROLLER_HIDDEN,null);
    }

    protected void onControllerShow() {

    }

    protected void switchControllerState(){
        if(isVisibilityGone()){
            setControllerState(true);
        }else{
            setControllerState(false);
        }
    }

    @Override
    public void updateSystemTime() {
        if(mTvSystemTime==null)
            return;
        mTvSystemTime.setText(TimeUtil.getNowTime());
    }

    @Override
    public void updateBatteryState(int batteryValue) {
        if(mBatteryView==null)
            return;
        mBatteryView.setBatteryValue(batteryValue);
    }

    @Override
    public void setPlayState(boolean isPlaying) {
        if(mIvPlayState==null)
            return;
        mIvPlayState.setSelected(isPlaying);
    }

    @Override
    public void setPlayTime(long curr, long total) {
        if(mTvMergeTime!=null){
            mTvMergeTime.setText(TimeUtil.getTime(curr) + "/" + TimeUtil.getTime(total));
        }else{
            if(mTvCurrTime!=null){
                mTvCurrTime.setText(TimeUtil.getTime(curr));
            }
            if(mTvTotalTime!=null){
                mTvTotalTime.setText(TimeUtil.getTime(total));
            }
        }
    }

    protected void updateSeekBar(int curr, int duration, int bufferPercentage){
        setSeekMax(duration);
        setSeekProgress(curr);
        setSeekSecondProgress(bufferPercentage);
    }

    @Override
    public void setTimerCounterUpdateProgressEnable(boolean enable) {
        timerCounterUpdateEnable = enable;
    }

    @Override
    public void setSeekMax(int max) {
        if(mSeekBar==null)
            return;
        mSeekBar.setMax(max);
    }

    @Override
    public void setSeekProgress(int progress) {
        if(mSeekBar==null)
            return;
        mSeekBar.setProgress(progress);
    }

    @Override
    public void setSeekSecondProgress(int progress) {
        if(mSeekBar==null)
            return;
        mSeekBar.setSecondaryProgress(progress);
    }

    @Override
    public int getSeekProgress() {
        if(mSeekBar==null)
            return 0;
        return mSeekBar.getProgress();
    }

    @Override
    public void resetSeekBar() {
        setSeekMax(0);
        setSeekProgress(0);
        setSeekSecondProgress(0);
    }

    @Override
    public void resetPlayTime() {
        setPlayTime(0,0);
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
