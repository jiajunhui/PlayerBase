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

package com.kk.taurus.playerbase.utils;

import android.graphics.Rect;

/**
 * Created by Taurus on 2017/12/10.
 */

public class RectUtils {

    public static Rect getOvalRect(Rect rect){
        int width = rect.right - rect.left;
        int height = rect.bottom - rect.top;

        int left;
        int top;
        int right;
        int bottom;
        if(width > height){
            left   = width/2 - height/2;
            top    = 0;
            right  = width/2 + height/2;
            bottom = height;
        }else{
            left = height/2 - width/2;
            top = 0;
            right = height/2 + width/2;
            bottom = width;
        }

        Rect selfRect = new Rect(
                left,
                top,
                right,
                bottom);
        return selfRect;
    }

}
