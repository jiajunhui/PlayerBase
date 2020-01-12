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

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import android.view.View;

/**
 * Created by Taurus on 2017/12/9.
 *
 * style setter ,include round rect,oval rect and shadow.
 * The Shape Style settings support only more than LOLLIPOP.
 *
 */

public final class StyleSetter implements IStyleSetter {

    private View mView;

    public StyleSetter(View view){
        this.mView = view;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(float radius){
        setRoundRectShape(null, radius);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(Rect rect, float radius){
        this.mView.setClipToOutline(true);
        this.mView.setOutlineProvider(new ViewRoundRectOutlineProvider(radius, rect));
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape(){
        setOvalRectShape(null);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape(Rect rect){
        this.mView.setClipToOutline(true);
        this.mView.setOutlineProvider(new ViewOvalRectOutlineProvider(rect));
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearShapeStyle() {
        this.mView.setClipToOutline(false);
    }

    @Override
    public void setElevationShadow(float elevation) {
        setElevationShadow(Color.BLACK, elevation);
    }

    @Override
    public void setElevationShadow(int backgroundColor, float elevation) {
        mView.setBackgroundColor(backgroundColor);
        ViewCompat.setElevation(mView,elevation);
        mView.invalidate();
    }
}