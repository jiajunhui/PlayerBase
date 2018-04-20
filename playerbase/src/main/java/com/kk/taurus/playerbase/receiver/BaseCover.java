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

package com.kk.taurus.playerbase.receiver;

import android.content.Context;
import android.view.View;

/**
 * Created by Taurus on 2018/3/17.
 */

public abstract class BaseCover extends BaseReceiver implements ICover, View.OnAttachStateChangeListener {

    private View mCoverView;

    public BaseCover(Context context) {
        super(context);
        mCoverView = onCreateCoverView(context);
        mCoverView.addOnAttachStateChangeListener(this);
    }

    public void onCoverCreate(){

    }

    protected <T extends View> T findViewById(int id){
        return mCoverView.findViewById(id);
    }

    @Override
    public final void setCoverVisibility(int visibility){
        mCoverView.setVisibility(visibility);
    }

    @Override
    public View getView(){
        return mCoverView;
    }

    protected final void post(Runnable runnable){
        if(mCoverView!=null){
            mCoverView.post(runnable);
        }
    }

    @Override
    public final void onViewAttachedToWindow(View v) {
        onCoverAttachedToWindow();
    }

    @Override
    public final void onViewDetachedFromWindow(View v) {
        onCoverDetachedToWindow();
    }

    protected void onCoverAttachedToWindow(){

    }

    protected void onCoverDetachedToWindow(){

    }

    @Override
    public abstract View onCreateCoverView(Context context);

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_MEDIUM;
    }
}
