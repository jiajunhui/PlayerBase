package com.kk.taurus.playerbase.window;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

public class FloatWindowParams {

    private int windowType = WindowManager.LayoutParams.TYPE_TOAST;
    private int gravity = Gravity.TOP | Gravity.LEFT;
    private int format = PixelFormat.RGBA_8888;
    private int flag = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    private int x;
    private int y;
    private int width = WindowManager.LayoutParams.WRAP_CONTENT;
    private int height = WindowManager.LayoutParams.WRAP_CONTENT;

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
}
