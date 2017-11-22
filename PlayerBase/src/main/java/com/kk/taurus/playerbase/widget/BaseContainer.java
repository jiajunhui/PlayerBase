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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.BaseGestureCallbackHandler;
import com.kk.taurus.playerbase.setting.ContainerTouchHelper;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICoverContainer;


/**
 * Created by Taurus on 2017/3/24.
 *
 * 播放组件容器。初始化一些设备基本信息。
 *
 */

public abstract class BaseContainer extends FrameLayout {

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
     * for handle user gesture.
     */
    private ContainerTouchHelper mTouchHelper;

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
        mTouchHelper.onSizeChanged(w, h, oldw, oldh);
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
        mTouchHelper = new ContainerTouchHelper(context,getGestureCallBackHandler());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchHelper.onTouch(event);
    }

    protected abstract BaseGestureCallbackHandler getGestureCallBackHandler();

    public void setGestureEnable(boolean enable){
        mTouchHelper.setGestureEnable(enable);
        onPlayerGestureEnableChange(enable);
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
            clearPlayerContainer();
        }
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        mPlayerContainer.addView(view,lp);
    }

    protected void clearPlayerContainer(){
        if(mPlayerContainer!=null){
            mPlayerContainer.removeAllViews();
        }
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
        if(mCoverContainer!=null){
            removeView(mCoverContainer.getContainerRoot());
        }
    }

    protected void onPlayerGestureEnableChange(boolean enable){
        Log.d(TAG,"onPlayerGestureEnableChange...");
    }
}
