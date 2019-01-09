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

import android.content.Context;

public class PUtils {

    public static int getStatusBarHeight(Context context){
        int height = getStatusBarHeightMethod1(context);
        if(height<=0){
            height = getStatusBarHeightMethod2(context);
        }
        return height;
    }

    private static int getStatusBarHeightMethod1(Context context){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private static int getStatusBarHeightMethod2(Context context){
        return (int) Math.ceil(20 * context.getResources().getDisplayMetrics().density);
    }

}
