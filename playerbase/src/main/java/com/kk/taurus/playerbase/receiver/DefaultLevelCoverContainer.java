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

public class DefaultLevelCoverContainer extends BaseLevelCoverContainer {

    private final String TAG = "DefaultLevelCoverContainer";

    /**
     * the low level covers container .
     */
    private FrameLayout mLevelLowCoverContainer;
    /**
     * the medium level covers container .
     */
    private FrameLayout mLevelMediumCoverContainer;
    /**
     * the high level covers container .
     */
    private FrameLayout mLevelHighCoverContainer;

    public DefaultLevelCoverContainer(Context context) {
        super(context);
    }

    @Override
    protected void initLevelContainers(Context context) {
        //add low cover container
        mLevelLowCoverContainer = new FrameLayout(context);
        mLevelLowCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelLowCoverContainer,null);

        //add medium cover container
        mLevelMediumCoverContainer = new FrameLayout(context);
        mLevelMediumCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelMediumCoverContainer,null);

        //add high cover container
        mLevelHighCoverContainer = new FrameLayout(context);
        mLevelHighCoverContainer.setBackgroundColor(Color.TRANSPARENT);
        addLevelContainerView(mLevelHighCoverContainer,null);
    }

    @Override
    protected void onAvailableCoverAdd(BaseCover cover, boolean insert) {
        super.onAvailableCoverAdd(cover, insert);
        int mode = cover.getCoverLayer();
        ViewGroup viewGroup;
        switch (mode) {
            default:
            case ICover.COVER_LEVEL_LOW:
                viewGroup = mLevelLowCoverContainer;
                break;
            case ICover.COVER_LEVEL_MEDIUM:
                viewGroup = mLevelLowCoverContainer;
                break;
            case ICover.COVER_LEVEL_HIGH:
                viewGroup = mLevelHighCoverContainer;
                break;
        }
        addView(viewGroup, cover, insert);
    }

    private void addView(ViewGroup parent, BaseCover cover, boolean insert) {
        int priority = cover.getCoverPriority();
        int childCount = parent.getChildCount();
        int insetPosition = -1;
        if (insert) {
            for (int i = 0; i < childCount; i++) {
                int viewPriority = (int) parent.getChildAt(i).getTag();
                if (priority < viewPriority) {
                    insetPosition = i;
                    break;
                }
            }
            PLog.d(TAG, "insert position " + insetPosition);
        }
        parent.addView(cover.getView(), insetPosition);
    }

    @Override
    protected void onAvailableCoverRemove(BaseCover cover) {
        super.onAvailableCoverRemove(cover);
        mLevelLowCoverContainer.removeView(cover.getView());
        mLevelMediumCoverContainer.removeView(cover.getView());
        mLevelHighCoverContainer.removeView(cover.getView());
    }

    @Override
    protected void onCoversRemoveAll() {
        super.onCoversRemoveAll();
        mLevelLowCoverContainer.removeAllViews();
        mLevelMediumCoverContainer.removeAllViews();
        mLevelHighCoverContainer.removeAllViews();
    }

    private ViewGroup.LayoutParams getNewMatchLayoutParams(){
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
