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
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/29.
 */

public abstract class BaseLevelCoverContainer extends BaseCoverContainer {

    protected List<View> mLevelContainer = new ArrayList<>();

    public BaseLevelCoverContainer(Context context) {
        super(context);
        initLevelContainers(context);
    }

    protected abstract void initLevelContainers(Context context);

    protected void addLevelContainerView(ViewGroup container, ViewGroup.LayoutParams layoutParams){
        if(getContainerRoot()!=null){
            if(layoutParams==null)
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getContainerRoot().addView(container,layoutParams);
            mLevelContainer.add(container);
        }
    }

}
