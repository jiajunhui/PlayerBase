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

package com.kk.taurus.playerbase.callback;

import android.view.MotionEvent;

import com.kk.taurus.playerbase.cover.base.BaseReceiverCollections;

/**
 * Created by Taurus on 2017/10/15.
 */

public class GestureObserverHandler implements GestureObserver{

    private BaseReceiverCollections mReceiverCollections;

    public GestureObserverHandler(BaseReceiverCollections receiverCollections){
        this.mReceiverCollections = receiverCollections;
    }

    @Override
    public boolean onGestureSingleTab(MotionEvent event) {
        boolean result = false;
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    result = ((GestureObserver)receiver).onGestureSingleTab(event);
                    if(result){
                        break;
                    }
                }
            }
        return result;
    }

    @Override
    public boolean onGestureDoubleTab(MotionEvent event) {
        boolean result = false;
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    result = ((GestureObserver)receiver).onGestureDoubleTab(event);
                    if(result){
                        break;
                    }
                }
            }
        return result;
    }

    @Override
    public boolean onGestureDown(MotionEvent event) {
        boolean result = false;
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    result = ((GestureObserver)receiver).onGestureDown(event);
                    if(result){
                        break;
                    }
                }
            }
        return result;
    }

    @Override
    public boolean onGestureScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        boolean result = false;
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    result = ((GestureObserver)receiver).onGestureScroll(e1, e2, distanceX, distanceY);
                    if(result){
                        break;
                    }
                }
            }
        return result;
    }

    @Override
    public void onGestureHorizontalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureHorizontalSlide(percent,e1, e2, distanceX, distanceY);
                }
            }
    }

    @Override
    public void onGestureRightVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureRightVerticalSlide(percent,e1, e2, distanceX, distanceY);
                }
            }
    }

    @Override
    public void onGestureLeftVerticalSlide(float percent, MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureLeftVerticalSlide(percent,e1, e2, distanceX, distanceY);
                }
            }
    }

    @Override
    public void onGestureEnableChange(boolean enable) {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureEnableChange(enable);
                }
            }
    }

    @Override
    public void onGestureEnd() {
        if(mReceiverCollections!=null && mReceiverCollections.getReceivers()!=null)
            for(BaseEventReceiver receiver:mReceiverCollections.getReceivers()){
                if(receiver instanceof GestureObserver){
                    ((GestureObserver)receiver).onGestureEnd();
                }
            }
    }
}
