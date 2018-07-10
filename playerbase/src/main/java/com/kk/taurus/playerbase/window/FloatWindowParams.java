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

package com.kk.taurus.playerbase.window;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by Taurus on 2018/5/21.
 */
public class FloatWindowParams {

    private int windowType = WindowManager.LayoutParams.TYPE_PHONE;
    private int gravity = Gravity.TOP | Gravity.LEFT;
    private int format = PixelFormat.RGBA_8888;
    private int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    private int x;
    private int y;
    private int width = WindowManager.LayoutParams.WRAP_CONTENT;
    private int height = WindowManager.LayoutParams.WRAP_CONTENT;
    private boolean defaultAnimation = true;

    public int getWindowType() {
        return windowType;
    }

    public FloatWindowParams setWindowType(int windowType) {
        this.windowType = windowType;
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    public FloatWindowParams setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public int getFormat() {
        return format;
    }

    public FloatWindowParams setFormat(int format) {
        this.format = format;
        return this;
    }

    public int getFlag() {
        return flag;
    }

    public FloatWindowParams setFlag(int flag) {
        this.flag = flag;
        return this;
    }

    public int getX() {
        return x;
    }

    public FloatWindowParams setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public FloatWindowParams setY(int y) {
        this.y = y;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public FloatWindowParams setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public FloatWindowParams setHeight(int height) {
        this.height = height;
        return this;
    }

    public boolean isDefaultAnimation() {
        return defaultAnimation;
    }

    public FloatWindowParams setDefaultAnimation(boolean defaultAnimation) {
        this.defaultAnimation = defaultAnimation;
        return this;
    }
}
