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
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.log.PLog;


/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class BaseLevelCoverContainer extends AbsCoverContainer {

    private final String TAG = "base_cover_container";

    public BaseLevelCoverContainer(Context context) {
        super(context);
        initLevelContainers(context);
    }

    @Override
    protected void onCoverAdd(BaseCover cover) {

    }

    @Override
    protected void onAvailableCoverAdd(BaseCover cover) {
        PLog.d(TAG,"on available cover add : now count = " + getCoverCount());
    }

    @Override
    protected void onCoverRemove(BaseCover cover) {
        PLog.d(TAG,"on cover remove : now count = " + getCoverCount());

    }

    @Override
    protected void onAvailableCoverRemove(BaseCover cover) {

    }

    @Override
    protected void onCoversRemoveAll() {
        PLog.d(TAG,"on covers remove all ...");

    }

    @Override
    protected ViewGroup initContainerRootView() {
        FrameLayout root = new FrameLayout(mContext);
        root.setBackgroundColor(Color.TRANSPARENT);
        return root;
    }

    protected abstract void initLevelContainers(Context context);

    protected void addLevelContainerView(ViewGroup container, ViewGroup.LayoutParams layoutParams){
        if(getContainerView()!=null){
            if(layoutParams==null)
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getContainerView().addView(container,layoutParams);
        }
    }
}
