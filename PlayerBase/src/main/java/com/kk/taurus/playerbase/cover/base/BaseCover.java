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
import android.view.View;

import com.kk.taurus.playerbase.adapter.BaseVideoDataAdapter;
import com.kk.taurus.playerbase.eventHandler.BaseEventReceiver;
import com.kk.taurus.playerbase.callback.OnCoverEventListener;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.setting.CoverData;
import com.kk.taurus.playerbase.widget.BasePlayer;

/**
 * Created by Taurus on 2017/3/24.
 *
 * 覆盖层基类，默认显示状态为GONE。
 *
 */

public abstract class BaseCover extends BaseEventReceiver implements ICover , View.OnClickListener{

    private View mCoverView;
    protected boolean coverEnable = true;

    public BaseCover(Context context){
        super(context);
        handCoverView(context);
        setDefaultGone();
        findView();
        afterFindView();
    }

    protected void afterFindView() {

    }

    protected void setDefaultGone() {
        setCoverVisibility(View.GONE);
    }

    private void handCoverView(Context context){
        mCoverView = initCoverLayout(context);
        mCoverView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                onCoverViewAttachedToWindow(v);
            }
            @Override
            public void onViewDetachedFromWindow(View v) {
                onCoverViewDetachedFromWindow(v);
            }
        });
    }

    protected abstract void findView();

    protected <V> V findViewById(int id){
        return (V) mCoverView.findViewById(id);
    }

    @Override
    public abstract View initCoverLayout(Context context);

    protected void onCoverViewAttachedToWindow(View v){

    }

    protected void onCoverViewDetachedFromWindow(View v){
        onDestroy();
    }

    public void setCoverEnable(boolean enable) {
        if(!enable){
            setCoverVisibility(View.GONE);
        }
        this.coverEnable = enable;
    }

    @Override
    public void setCoverVisibility(int visibility) {
        if(!coverEnable)
            return;
        if(mCoverView!=null){
            mCoverView.setVisibility(visibility);
            onCoverVisibilityChange(visibility);
        }
    }

    protected void onCoverVisibilityChange(int visibility){

    }

    public void onRefreshDataAdapter(BaseVideoDataAdapter dataAdapter){
        super.onRefreshDataAdapter(dataAdapter);

    }

    public void onRefreshCoverData(CoverData data) {
        super.onRefreshCoverData(data);

    }

    protected boolean isVisibilityGone(){
        return getView().getVisibility()!=View.VISIBLE;
    }

    @Override
    public View getView() {
        return mCoverView;
    }

    @Override
    public int getCoverLevel() {
        return COVER_LEVEL_LOW;
    }

    @Override
    public void onClick(View v) {

    }

    public void onBindPlayer(BasePlayer player, OnCoverEventListener onCoverEventListener) {
        super.onBindPlayer(player, onCoverEventListener);

    }

    protected void releaseFocusToDpadCover() {
        if (getPlayer() != null) {
            getPlayer().dPadRequestFocus();
        }
    }

}
