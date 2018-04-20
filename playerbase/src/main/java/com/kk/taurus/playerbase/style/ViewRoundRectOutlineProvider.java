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

package com.kk.taurus.playerbase.style;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * Created by Taurus on 2017/12/9.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ViewRoundRectOutlineProvider extends ViewOutlineProvider {

    private float mRadius;
    private Rect mRect;

    public ViewRoundRectOutlineProvider(float radius){
        this.mRadius = radius;
    }

    public ViewRoundRectOutlineProvider(float radius, Rect rect) {
        this.mRadius = radius;
        this.mRect = rect;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        if(mRect!=null){
            selfRect = mRect;
        }
        outline.setRoundRect(selfRect, mRadius);
    }
}
