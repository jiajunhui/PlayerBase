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
import com.kk.taurus.playerbase.inter.ICoverContainer;
import com.kk.taurus.playerbase.setting.PlayerGestureDetector;


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
        //init cover container
        initCoverContainer(context);
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

    private void initCoverContainer(Context context) {
        mCoverContainer = getCoverContainer(context);
        if(mCoverContainer==null){
            throw new NullPointerException("please init cover container !");
        }
        addView(mCoverContainer.getContainerRoot(),new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected abstract ICoverContainer getCoverContainer(Context context);

    protected void addCover(BaseCover cover){
        if(mCoverContainer!=null){
            mCoverContainer.addCover(cover);
        }
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
