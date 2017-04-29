package com.kk.taurus.playerbase.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kk.taurus.playerbase.callback.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.GestureObserver;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;
import com.kk.taurus.playerbase.callback.PlayerObserver;
import com.kk.taurus.playerbase.inter.IDpadFocusCover;
import com.kk.taurus.playerbase.inter.IEventReceiver;
import com.kk.taurus.playerbase.setting.BaseAdVideo;
import com.kk.taurus.playerbase.setting.VideoData;

import java.util.List;

/**
 * Created by Taurus on 2017/3/24.
 *
 * 绑定receiver集合，负责分发和中转player或者cover消息。
 *
 */

public abstract class BaseBindEventReceiver extends BaseContainer implements IEventReceiver, PlayerObserver,GestureObserver,OnCoverEventListener{

    private BaseReceiverCollections receiverCollections;
    private OnCoverEventListener mOnCoverEventListener;

    public BaseBindEventReceiver(@NonNull Context context){
        super(context);
    }

    public BaseBindEventReceiver(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseBindEventReceiver(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindCoverCollections(BaseReceiverCollections coverCollections){
        if(this.receiverCollections !=null)
            return;
        this.receiverCollections = coverCollections;
        initCovers(mAppContext);
    }

    public void unbindCoverCollections(){
        if(receiverCollections !=null){
            receiverCollections.clear();
            receiverCollections = null;
        }
        removeAllCovers();
        removeAllContainers();
    }

    private void initCovers(Context context) {
        if(receiverCollections ==null)
            return;
        List<BaseEventReceiver> covers = receiverCollections.getReceivers();
        for(BaseEventReceiver cover : covers){
            if(cover instanceof BaseCover){
                addCover((BaseCover) cover);
            }
        }
        onCoversHasInit(context);
    }

    public BaseReceiverCollections getReceiverCollections(){
        return receiverCollections;
    }

    protected void onCoversHasInit(Context context) {

    }

    public void setOnCoverEventListener(OnCoverEventListener onCoverEventListener){
        this.mOnCoverEventListener = onCoverEventListener;
    }

    @Override
    public void onCoverEvent(int eventCode, Bundle bundle) {
        if(mOnCoverEventListener!=null){
            mOnCoverEventListener.onCoverEvent(eventCode, bundle);
        }
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onCoverEvent(eventCode, bundle);
                }
            }
    }

    /**
     * 当cover集合中存在Dpad控制层时，将焦点控制权交给它。
     */
    public void dPadRequestFocus(){
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null && receiver instanceof IDpadFocusCover){
                    receiver.onNotifyPlayEvent(OnPlayerEventListener.EVENT_CODE_PLAYER_DPAD_REQUEST_FOCUS, null);
                }
            }
    }

    @Override
    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onBindPlayer(player,onCoverEventListener);
                }
            }
    }

    @Override
    public void onNotifyConfigurationChanged(Configuration newConfig) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyConfigurationChanged(newConfig);
                }
            }
    }

    @Override
    public void onNotifyPlayEvent(int eventCode, Bundle bundle) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyPlayEvent(eventCode, bundle);
                }
            }
    }

    @Override
    public void onNotifyErrorEvent(int eventCode, Bundle bundle) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyErrorEvent(eventCode, bundle);
                }
            }
    }

    @Override
    public void onNotifyPlayTimerCounter(int curr, int duration, int bufferPercentage) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyPlayTimerCounter(curr, duration, bufferPercentage);
                }
            }
    }

    @Override
    public void onNotifyNetWorkConnected(int networkType) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyNetWorkConnected(networkType);
                }
            }
    }

    @Override
    public void onNotifyNetWorkError() {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyNetWorkError();
                }
            }
    }

    @Override
    public void onNotifyAdPrepared(List<BaseAdVideo> adVideos) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyAdPrepared(adVideos);
                }
            }
    }

    @Override
    public void onNotifyAdStart(BaseAdVideo adVideo) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyAdStart(adVideo);
                }
            }
    }

    @Override
    public void onNotifyAdFinish(VideoData data, boolean isAllFinish) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver!=null){
                    receiver.onNotifyAdFinish(data, isAllFinish);
                }
            }
    }

    @Override
    public void onGestureSingleTab(MotionEvent event) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureSingleTab(event);
                }
            }
    }

    @Override
    public void onGestureDoubleTab(MotionEvent event) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureDoubleTab(event);
                }
            }
    }

    @Override
    public void onGestureScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureScroll(e1, e2, distanceX, distanceY);
                }
            }
    }

    @Override
    public void onGestureHorizontalSlide(float percent) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureHorizontalSlide(percent);
                }
            }
    }

    @Override
    public void onGestureRightVerticalSlide(float percent) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureRightVerticalSlide(percent);
                }
            }
    }

    @Override
    public void onGestureLeftVerticalSlide(float percent) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureLeftVerticalSlide(percent);
                }
            }
    }

    @Override
    public void onGestureEnableChange(boolean enable) {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureEnableChange(enable);
                }
            }
    }

    @Override
    public void onGestureEnd() {
        if(receiverCollections!=null && receiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:receiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureEnd();
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
