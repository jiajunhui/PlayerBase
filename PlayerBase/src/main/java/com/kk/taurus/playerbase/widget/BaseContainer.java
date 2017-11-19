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

package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.cover.GestureCover;
import com.kk.taurus.playerbase.view.GestureLayout;
import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICoverContainer;


/**
 * Created by Taurus on 2017/3/24.
 *
 * 播放组件容器。初始化一些设备基本信息。
 *
 */

public abstract class BaseContainer extends FrameLayout implements OnPlayerGestureListener {

    private final String TAG = "_BaseContainer";
    /**
     * the app context.
     */
    protected Context mAppContext;
    /**
     * player widget container. such as VideoView.
     */
    private FrameLayout mPlayerContainer;
    /**
     * cover container
     */
    private ICoverContainer mCoverContainer;
    /**
     * the container width and height.
     */
    protected int mWidth,mHeight;
    /**
     * the device info , screen width and screen height.
     */
    protected int mScreenW, mScreenH;
    /**
     * gesture layout for handle user gesture.
     */
    private GestureLayout mGestureLayout;

    public BaseContainer(@NonNull Context context) {
        this(context,null);
    }

    public BaseContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initContainer(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if(mGestureLayout!=null){
            mGestureLayout.updateWH(w,h);
        }
    }

    private void initContainer(Context context) {
        this.mAppContext = context;
        setBackgroundColor(Color.BLACK);
        initBaseInfo(context);
        //init render container
        initPlayerContainer(context);
        //init cover container
        initCoverContainer(context);
        //init gesture handle layout
        initGesture(context);
        onContainerHasInit(context);
    }

    protected void onContainerHasInit(Context context){

    }

    protected void initGesture(Context context){
//        mGestureLayout = new GestureLayout(context, mWidth, mHeight);
//        mGestureLayout.setPlayerGestureListener(this);
//        addView(mGestureLayout,new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        GestureCover gestureCover = new GestureCover(context,this);
        mGestureLayout = gestureCover.getGestureLayout();
        addCover(gestureCover);
    }

    public void setGestureEnable(boolean enable){
        if(mGestureLayout!=null){
            mGestureLayout.setEnabled(enable);
            onPlayerGestureEnableChange(enable);
        }
    }

    protected void initBaseInfo(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenW = displayMetrics.widthPixels;
        mScreenH = displayMetrics.heightPixels;
    }

    private void initPlayerContainer(Context context) {
        mPlayerContainer = new FrameLayout(context);
        mPlayerContainer.setBackgroundColor(Color.TRANSPARENT);
        addView(mPlayerContainer,new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        onPlayerContainerHasInit(context);
        notifyPlayerWidget(context);
    }

    protected void onPlayerContainerHasInit(Context context) {

    }

    private void initPlayerWidget(Context context) {
        View widget = getPlayerWidget(context);
        if(widget==null)
            return;
        addViewToPlayerContainer(widget,true);
    }

    protected void addViewToPlayerContainer(View view, boolean removeAll){
        if(mPlayerContainer==null)
            return;
        if(removeAll){
            mPlayerContainer.removeAllViews();
        }
        mPlayerContainer.addView(view,
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected boolean isPlayerContainerHasChild(){
        if(mPlayerContainer!=null){
            return mPlayerContainer.getChildCount() >= 0;
        }
        return false;
    }

    protected void notifyPlayerWidget(Context context){
        initPlayerWidget(context);
    }

    protected abstract View getPlayerWidget(Context context);

    private void initCoverContainer(Context context) {
        mCoverContainer = getCoverContainer(context);
        if(mCoverContainer==null){
            throw new NullPointerException("please init cover container !");
        }
        addView(mCoverContainer.getContainerRoot(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected abstract ICoverContainer getCoverContainer(Context context);

    protected void addCover(BaseCover cover){
        if(mCoverContainer!=null){
            mCoverContainer.addCover(cover);
        }
    }

    public ICoverContainer getCoverContainer(){
        return mCoverContainer;
    }

    private boolean isContainCoverView(BaseCover cover){
        if(mCoverContainer!=null){
            return mCoverContainer.isContainsCover(cover);
        }
        return false;
    }

    protected void removeCover(BaseCover cover){
        if(mCoverContainer!=null){
            mCoverContainer.removeCover(cover);
        }
    }

    protected void removeAllCovers(){
        if(mCoverContainer!=null){
            mCoverContainer.removeAllCovers();
        }
    }

    protected void removeAllContainers(){
        removeView(mPlayerContainer);
        removeView(mGestureLayout);
        if(mCoverContainer!=null){
            removeView(mCoverContainer.getContainerRoot());
        }
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(TAG,"onSingleTapUp...");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        Log.d(TAG,"onDoubleTap...");
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(TAG,"onDown...");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"onScroll...");
        return false;
    }

    @Override
    public void onHorizontalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"onHorizontalSlide...");
    }

    @Override
    public void onRightVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"onRightVerticalSlide...");
    }

    @Override
    public void onLeftVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"onLeftVerticalSlide...");
    }

    public void onEndGesture(){
        Log.d(TAG,"onEndGesture...");
    }

    protected void onPlayerGestureEnableChange(boolean enable){
        Log.d(TAG,"onPlayerGestureEnableChange...");
    }
}
