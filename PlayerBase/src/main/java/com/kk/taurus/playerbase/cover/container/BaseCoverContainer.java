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

package com.kk.taurus.playerbase.cover.container;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.cover.base.BaseCover;


/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class BaseCoverContainer extends AbsCoverContainer {

    private final String TAG = "base_cover_container";

    public BaseCoverContainer(Context context) {
        super(context);
    }

    @Override
    protected void onCoverAdd(BaseCover cover) {

    }

    @Override
    protected void onAvailableCoverAdd(BaseCover cover) {
        Log.d(TAG,"on available cover add : now count = " + getCoverCount());
    }

    @Override
    protected void onCoverRemove(BaseCover cover) {
        Log.d(TAG,"on cover remove : now count = " + getCoverCount());

    }

    @Override
    protected void onAvailableCoverRemove(BaseCover cover) {

    }

    @Override
    protected void onCoversRemoveAll() {
        Log.d(TAG,"on covers remove all ...");

    }

    @Override
    protected ViewGroup initContainerRootView() {
        FrameLayout root = new FrameLayout(mContext);
        root.setBackgroundColor(Color.TRANSPARENT);
        return root;
    }
}
