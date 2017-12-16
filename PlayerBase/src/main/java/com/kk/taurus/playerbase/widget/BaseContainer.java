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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.eventHandler.BaseGestureCallbackHandler;
import com.kk.taurus.playerbase.inter.IStyleSetter;
import com.kk.taurus.playerbase.eventHandler.ContainerTouchHelper;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICoverContainer;
import com.kk.taurus.playerbase.setting.StyleSetter;
import com.kk.taurus.playerbase.utils.PLog;
import com.kk.taurus.playerbase.inter.IRender;
import com.kk.taurus.playerbase.eventHandler.BaseEventReceiver;
import com.kk.taurus.playerbase.inter.ICover;


/**
 * Created by Taurus on 2017/3/24.
 *
 * 组件容器，包含播放组件{@link IRender}，接收者组件{@link BaseEventReceiver}。
 * 其中接收者组件包含覆盖层组件{@link ICover},初始化了覆盖层组件的容器{@link ICoverContainer}
 * 初始化一些基本信息。
 *
 * 手势Event的分发处理。详见{@link ContainerTouchHelper}
 */

public abstract class BaseContainer extends FrameLayout implements IStyleSetter {

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
    private boolean mGestureEnable = true;

    private StyleSetter mStyleSetter;

    public BaseContainer(Context context) {
        this(context,null);
    }

    public BaseContainer(Context context, AttributeSet attrs) {
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
        this.mStyleSetter = new StyleSetter(this);
        setBackgroundColor(Color.BLACK);
        initBaseInfo(context);
        //init render container
        initPlayerContainer(context);
        //init cover container
        initCoverContainer(context);
        //init gesture handle
        initGesture(context);
        onContainerHasInit(context);
    }

    protected void onContainerHasInit(Context context){

    }

    protected void initGesture(Context context){
        mTouchHelper = new ContainerTouchHelper(context,getGestureCallBackHandler());
        setGestureEnable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mTouchHelper.onTouch(event);
    }

    protected abstract BaseGestureCallbackHandler getGestureCallBackHandler();

    public void setGestureEnable(boolean enable){
        mGestureEnable = enable;
        mTouchHelper.setGestureEnable(enable);
        onPlayerGestureEnableChange(enable);
    }

    public boolean isGestureEnable() {
        return mGestureEnable;
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
    }

    private void initPlayerWidget(Context context) {
        View widget = getPlayerWidget(context);
        if(widget==null)
            return;
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        addViewToPlayerContainer(widget,true, lp);
    }

    protected void addViewToPlayerContainer(View view, boolean removeAll, LayoutParams lp){
        if(mPlayerContainer==null)
            return;
        if(removeAll){
            clearPlayerContainer();
        }
        if(lp==null){
            lp = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
        }
        mPlayerContainer.addView(view,lp);
    }

    protected void clearPlayerContainer(){
        if(mPlayerContainer!=null){
            mPlayerContainer.removeAllViews();
        }
    }

    protected void notifyPlayerWidget(Context context){
        initPlayerWidget(context);
    }

    protected View getPlayerRenderView(){
        if(mPlayerContainer!=null && mPlayerContainer.getChildCount()>0){
            return mPlayerContainer.getChildAt(0);
        }
        return null;
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

    protected void onPlayerGestureEnableChange(boolean enable){
        PLog.d(TAG,"onPlayerGestureEnableChange...");
    }

    @Override
    public void setRoundRectShape(float radius) {
        mStyleSetter.setRoundRectShape(radius);
    }

    @Override
    public void setRoundRectShape(Rect rect, float radius) {
        mStyleSetter.setRoundRectShape(rect, radius);
    }

    @Override
    public void setOvalRectShape() {
        mStyleSetter.setOvalRectShape();
    }

    @Override
    public void setOvalRectShape(Rect rect) {
        mStyleSetter.setOvalRectShape(rect);
    }

    @Override
    public void setElevationShadow(float elevation) {
        mStyleSetter.setElevationShadow(elevation);
    }

}