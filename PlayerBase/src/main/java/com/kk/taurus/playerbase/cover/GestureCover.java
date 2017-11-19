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

package com.kk.taurus.playerbase.cover;

import android.content.Context;
import android.view.View;

import com.kk.taurus.playerbase.callback.OnPlayerGestureListener;
import com.kk.taurus.playerbase.cover.base.BaseCover;
import com.kk.taurus.playerbase.inter.ICover;
import com.kk.taurus.playerbase.view.GestureLayout;

/**
 * Created by mtime on 2017/10/18.
 */

public class GestureCover extends BaseCover {

    private GestureLayout mGestureLayout;

    public GestureCover(Context context, OnPlayerGestureListener onPlayerGestureListener) {
        super(context);
        mGestureLayout.setPlayerGestureListener(onPlayerGestureListener);
    }

    public GestureLayout getGestureLayout(){
        return mGestureLayout;
    }

    @Override
    protected void setDefaultGone() {

    }

    @Override
    protected void findView() {

    }

    @Override
    protected void afterFindView() {
        super.afterFindView();
    }

    @Override
    public View initCoverLayout(Context context) {
        mGestureLayout = new GestureLayout(context);
        return mGestureLayout;
    }

    @Override
    public int getCoverLevel() {
        return ICover.COVER_LEVEL_MEDIUM;
    }
}
