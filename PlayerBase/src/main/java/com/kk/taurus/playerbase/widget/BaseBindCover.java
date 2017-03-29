package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.GestureObserver;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseCoverCollections;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.inter.IPlayer;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 */

public abstract class BaseBindCover extends BaseContainer implements PlayerObserver,GestureObserver,OnCoverEventListener{

    private BaseCoverCollections coverCollections;

    public BaseBindCover(@NonNull Context context){
        super(context);
    }

    public BaseBindCover(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseBindCover(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindCoverCollections(BaseCoverCollections coverCollections){
        if(this.coverCollections!=null)
            return;
        this.coverCollections = coverCollections;
        initCovers(mAppContext);
    }

    private void initCovers(Context context) {
        if(coverCollections==null)
            return;
        List<BaseCover> covers = coverCollections.getCovers();
        for(BaseCover cover : covers){
            addCover(cover,null);
        }
        onCoversHasInit(context);
    }

    public BaseCoverCollections getCoverCollections(){
        return coverCollections;
    }

    protected void onCoversHasInit(Context context) {

    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onCoverEvent(eventCode, bundle);
            }
        }
    }

    public void onBindPlayer(IPlayer player, OnCoverEventListener onCoverEventListener) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onBindPlayer(player,onCoverEventListener);
            }
        }
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyConfigurationChanged(newConfig);
            }
        }
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyPlayEvent(eventCode, bundle);
            }
        }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyErrorEvent(eventCode, bundle);
            }
        }
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
            }
        }
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyNetWorkConnected(networkType);
            }
        }
    }

    @Override
    public void onNotifyNetWorkError() {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyNetWorkError();
            }
        }
    }

    @Override
    public void onNotifyAdPreparedStart(List<BaseAdVideo> adVideos) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyAdPreparedStart(adVideos);
            }
        }
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        for(BaseCover cover:mCovers){
            if(cover!=null){
                cover.onNotifyAdFinish(data, isAllFinish);
            }
        }
    }

    @Override
    public void onGestureSingleTab(MotionEvent event) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureSingleTab(event);
            }
        }
    }

    @Override
    public void onGestureDoubleTab(MotionEvent event) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureDoubleTab(event);
            }
        }
    }

    @Override
    public void onGestureScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureScroll(e1, e2, distanceX, distanceY);
            }
        }
    }

    @Override
    public void onGestureHorizontalSlide(float percent) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureHorizontalSlide(percent);
            }
        }
    }

    @Override
    public void onGestureRightVerticalSlide(float percent) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureRightVerticalSlide(percent);
            }
        }
    }

    @Override
    public void onGestureLeftVerticalSlide(float percent) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureLeftVerticalSlide(percent);
            }
        }
    }

    @Override
    public void onGestureEnableChange(boolean enable) {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureEnableChange(enable);
            }
        }
    }

    @Override
    public void onGestureEnd() {
        for(BaseCover cover:mCovers){
            if(cover instanceof GestureObserver){
                ((GestureObserver)cover).onGestureEnd();
            }
        }
    }


    //gesture handle----------------

    @Override
    public void onSingleTapUp(MotionEvent event) {
        super.onSingleTapUp(event);
        onGestureSingleTab(event);
    }

    @Override
    public void onDoubleTap(MotionEvent event) {
        super.onDoubleTap(event);
        onGestureDoubleTab(event);
    }

    @Override
    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        super.onScroll(e1, e2, distanceX, distanceY);
        onGestureScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public void onHorizontalSlide(float percent) {
        super.onHorizontalSlide(percent);
        onGestureHorizontalSlide(percent);
    }

    @Override
    public void onRightVerticalSlide(float percent) {
        super.onRightVerticalSlide(percent);
        onGestureRightVerticalSlide(percent);
    }

    @Override
    public void onLeftVerticalSlide(float percent) {
        super.onLeftVerticalSlide(percent);
        onGestureLeftVerticalSlide(percent);
    }

    @Override
    protected void onPlayerGestureEnableChange(boolean enable) {
        super.onPlayerGestureEnableChange(enable);
        onGestureEnableChange(enable);
    }

    @Override
    protected void onEndGesture() {
        super.onEndGesture();
        onGestureEnd();
    }
}
