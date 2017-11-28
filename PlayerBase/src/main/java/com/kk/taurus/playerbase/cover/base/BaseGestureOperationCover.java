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
import com.kk.taurus.playerbase.inter.IGestureOperation;

/**
 * Created by Taurus on 2017/3/26.
 */

public abstract class BaseGestureOperationCover extends BaseCover implements IGestureOperation, GestureObserver{

    public static final String KEY = "gesture_cover";
    protected View mVolumeBox;
    protected View mBrightnessBox;
    protected View mFastForwardBox;

    protected ImageView mVolumeIcon;
    protected TextView mVolumeText;
    protected TextView mBrightnessText;
    protected TextView mFastForwardStepTime;
    protected TextView mFastForwardProgressTime;

    public BaseGestureOperationCover(Context context){
        super(context);
    }

    public BaseGestureOperationCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void setDefaultGone() {
        setCoverVisibility(View.VISIBLE);
    }

    @Override
    protected void findView() {
        mVolumeBox = findViewById(R.id.cover_player_gesture_operation_volume_box);
        mVolumeIcon = findViewById(R.id.cover_player_gesture_operation_volume_icon);
        mVolumeText = findViewById(R.id.cover_player_gesture_operation_volume_text);
        mBrightnessBox = findViewById(R.id.cover_player_gesture_operation_brightness_box);
        mBrightnessText = findViewById(R.id.cover_player_gesture_operation_brightness_text);
        mFastForwardBox = findViewById(R.id.cover_player_gesture_operation_fast_forward_box);
        mFastForwardStepTime = findViewById(R.id.cover_player_gesture_operation_fast_forward_text_view_step_time);
        mFastForwardProgressTime = findViewById(R.id.cover_player_gesture_operation_fast_forward_text_view_progress_time);
    }

    @Override
    public void setVolumeBoxState(boolean state) {
        if(mVolumeBox!=null){
            mVolumeBox.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setVolumeIcon(int resId) {
        if(mVolumeIcon!=null){
            mVolumeIcon.setImageResource(resId);
        }
    }

    @Override
    public void setVolumeText(String text) {
        if(mVolumeText!=null){
            mVolumeText.setText(text);
        }
    }

    @Override
    public void setBrightnessBoxState(boolean state) {
        if(mBrightnessBox!=null){
            mBrightnessBox.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setBrightnessText(String text) {
        if(mBrightnessText!=null){
            mBrightnessText.setText(text);
        }
    }

    @Override
    public void setFastForwardState(boolean state) {
        if(mFastForwardBox!=null){
            mFastForwardBox.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    @Override
    public void setFastForwardStepTime(String text) {
        if(mFastForwardStepTime!=null){
            mFastForwardStepTime.setText(text);
        }
    }

    @Override
    public void setFastForwardProgressTime(String text) {
        if(mFastForwardProgressTime!=null){
            mFastForwardProgressTime.setText(text);
        }
    }

    @Override
    public void onGestureHorizontalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onGestureLeftVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onGestureRightVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

    }

    @Override
    public void onGestureDown(MotionEvent event) {

    }

    @Override
    public void onGestureEnd() {

    }

    @Override
    public void onGestureSingleTab(MotionEvent event) {

    }

    @Override
    public void onGestureDoubleTab(MotionEvent event) {

    }

    @Override
    public void onGestureScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    }

    @Override
    public void onGestureEnableChange(boolean enable) {

    }

}
