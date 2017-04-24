package com.kk.taurus.playerbase.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.setting.PlayerGestureDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 *
 * 播放组件容器。初始化一些设备基本信息。
 *
 */

public abstract class BaseContainer extends FrameLayout implements OnPlayerGestureListener {

    private final String TAG = "_BaseContainer";
    /**
     * the app context , must set activity context.
     */
    protected Context mAppContext;
    /**
     * player widget container. such as VideoView.
     */
    private FrameLayout mPlayerContainer;
    /**
     * the business covers container .
     */
    private FrameLayout mLevelLowCoverContainer;
    /**
     * the state covers container .
     */
    private FrameLayout mLevelMediumCoverContainer;
    /**
     * the extend covers container .
     */
    private FrameLayout mLevelHighCoverContainer;
    /**
     * cover collections.
     */
    protected List<BaseCover> mCovers = new ArrayList<>();

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
    private FrameLayout mGestureLayout;
    private PlayerGestureDetector mPlayerGestureDetector;

    public BaseContainer(@NonNull Context context) {
        this(context,null);
    }

    public BaseContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContainer(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if(mPlayerGestureDetector!=null){
            mPlayerGestureDetector.updateWH(w,h);
        }
    }

    private void initContainer(Context context) {
        if(!(context instanceof Activity))
            throw new IllegalArgumentException("please set activity context !");
        this.mAppContext = context;
        setBackgroundColor(Color.BLACK);
        initBaseInfo(context);
        //init render container
        initPlayerContainer(context);
        //init gesture handle layout
        initGesture(context);
        //init low cover container
        initLevelLowCoverContainer(context);
        //init medium cover container
        initLevelMediumCoverContainer(context);
        //init high cover container
        initLevelHighCoverContainer(context);
        onContainerHasInit(context);
    }

    protected void onContainerHasInit(Context context){

    }

    protected void initGesture(Context context){
        mGestureLayout = new FrameLayout(context);
        mGestureLayout.setBackgroundColor(Color.TRANSPARENT);
        addView(mGestureLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPlayerGestureDetector = new PlayerGestureDetector(mWidth,mHeight);
        mPlayerGestureDetector.setOnPlayerGestureListener(this);
        final GestureDetector gestureDetector = new GestureDetector(getContext(), mPlayerGestureDetector);
        mGestureLayout.setClickable(true);
        mGestureLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;
                // 处理手势结束
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        onEndGesture();
                        break;
                }

                return false;
            }
        });
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
        addView(mPlayerContainer,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        onPlayerContainerHasInit(context);
        notifyPlayerWidget(context);
    }

    protected void onPlayerContainerHasInit(Context context) {

    }

    private void initPlayerWidget(Context context) {
        if(mPlayerContainer!=null){
            mPlayerContainer.removeAllViews();
            mPlayerContainer.addView(getPlayerWidget(context),new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    protected void notifyPlayerWidget(Context context){
        initPlayerWidget(context);
    }

    protected abstract View getPlayerWidget(Context context);

    private void initLevelLowCoverContainer(Context context) {
        mLevelLowCoverContainer = new FrameLayout(context);
        mLevelLowCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addView(mLevelLowCoverContainer,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initLevelMediumCoverContainer(Context context) {
        mLevelMediumCoverContainer = new FrameLayout(context);
        mLevelMediumCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addView(mLevelMediumCoverContainer,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initLevelHighCoverContainer(Context context) {
        mLevelHighCoverContainer = new FrameLayout(context);
        mLevelHighCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addView(mLevelHighCoverContainer,new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected void addCover(BaseCover cover, ViewGroup.LayoutParams layoutParams){
        if(cover==null)
            return;
        if(isContainCoverView(cover))
            return;
        if(layoutParams==null){
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        mCovers.add(cover);
        switch (cover.getCoverLevel()){
            case ICover.COVER_LEVEL_LOW:
                mLevelLowCoverContainer.addView(cover.getView(),layoutParams);
                break;
            case ICover.COVER_LEVEL_MEDIUM:
                mLevelMediumCoverContainer.addView(cover.getView(),layoutParams);
                break;
            case ICover.COVER_LEVEL_HIGH:
                mLevelHighCoverContainer.addView(cover.getView(),layoutParams);
                break;
        }
    }

    private boolean isContainCoverView(BaseCover cover){
        if(cover==null)
            return false;
        return mLevelLowCoverContainer.indexOfChild(cover.getView())!=-1
                || mLevelMediumCoverContainer.indexOfChild(cover.getView())!=-1
                || mLevelHighCoverContainer.indexOfChild(cover.getView())!=-1;
    }

    protected void removeCover(BaseCover cover){
        if(cover==null)
            return;
        mLevelLowCoverContainer.removeView(cover.getView());
        mLevelMediumCoverContainer.removeView(cover.getView());
        mLevelHighCoverContainer.removeView(cover.getView());
    }

    protected void removeAllCovers(){
        if(mCovers!=null){
            for(BaseCover cover : mCovers){
                removeCover(cover);
            }
            mCovers.clear();
        }
    }

    protected void removeAllContainers(){
        removeView(mPlayerContainer);
        removeView(mLevelLowCoverContainer);
        removeView(mLevelMediumCoverContainer);
        removeView(mLevelHighCoverContainer);
        removeView(mGestureLayout);
    }

    @Override
    public void onSingleTapUp(MotionEvent event) {
        Log.d(TAG,"onSingleTapUp...");
    }

    @Override
    public void onDoubleTap(MotionEvent event) {
        Log.d(TAG,"onDoubleTap...");

    }

    @Override
    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG,"onScroll...");

    }

    @Override
    public void onHorizontalSlide(float percent) {
        Log.d(TAG,"onHorizontalSlide...");

    }

    @Override
    public void onRightVerticalSlide(float percent) {
        Log.d(TAG,"onRightVerticalSlide...");

    }

    @Override
    public void onLeftVerticalSlide(float percent) {
        Log.d(TAG,"onLeftVerticalSlide...");

    }

    protected void onPlayerGestureEnableChange(boolean enable){
        Log.d(TAG,"onPlayerGestureEnableChange...");

    }

    protected void onEndGesture(){
        Log.d(TAG,"onEndGesture...");

    }
}
