package com.kk.taurus.avplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Taurus on 2017/5/21.
 */

public class VisualizerView extends View {

    public static final byte WAVE_TYPE_BROKEN_LINE = 0;
    public static final byte WAVE_TYPE_RECTANGLE = 1;
    public static final byte WAVE_TYPE_CURVE = 2;

    private int mWidth, mHeight;
    // 数组保存了波形抽样点的值
    private byte[] bytes;
    private float[] points;
    // 定义画笔
    private Paint paint = new Paint();
    // 矩形区域
    private Rect rect = new Rect();
    private byte type = 0;

    private int[] colors;

    public VisualizerView(Context context) {
        super(context);
        init(context);
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setColors(colors);
    }

    private void init(Context context) {
        bytes = null;
        // 设置画笔的属性
        paint.setStrokeWidth(1f);// 设置空心线宽
        paint.setAntiAlias(true);// 抗锯齿
        paint.setColor(Color.BLUE);// 画笔颜色
        paint.setStyle(Paint.Style.STROKE);// 非填充模式
    }

    public void updateVisualizer(byte[] ftt) {
        bytes = ftt;
        // 通知组件重绘
        invalidate();
    }

    public void setColors(int[] colors) {
        if(colors==null||colors.length<=0)
            return;
        this.colors = colors;
        int len = colors.length;
        if(len>1){
            paint.setShader(new LinearGradient(0, 0,
                    mWidth, 0,
                    colors[0], colors[1], Shader.TileMode.MIRROR));
        }else{
            paint.setColor(colors[0]);
        }
        invalidate();
    }

    public void setWaveType(byte waveType){
        this.type = waveType;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (bytes == null || width <= 0 || height <= 0) {
            return;
        }
        // 使用rect对象记录该组件的宽度和高度
        rect.set(0, 0, width, height);
        switch (type) {
            // 绘制柱状的波形图（每隔18个抽样点绘制一个矩形）
            case WAVE_TYPE_RECTANGLE:
                for (int i = 0; i < bytes.length - 1; i += 18) {
                    float left = rect.width() * i / (bytes.length - 1);
                    // 根据波形值计算该矩形的高度
                    float top = rect.height() - (byte) (bytes[i + 1] + 128)
                            * rect.height() / 128;
                    float right = left + 6;
                    float bottom = rect.height();
                    canvas.drawRect(left, top, right, bottom, paint);
                }
                break;
            // -绘制曲线波形图
            case WAVE_TYPE_CURVE:
                // 如果point数组还未初始化
                if (points == null || points.length < bytes.length * 4) {
                    points = new float[bytes.length * 4];
                }
                for (int i = 0; i < bytes.length - 1; i++) {
                    // 计算第i个点的x坐标
                    points[i * 4] = rect.width() * i / (bytes.length - 1);
                    // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
                    points[i * 4 + 1] = (rect.height() / 2)
                            + ((byte) (bytes[i] + 128)) * 128 / (rect.height() / 2);
                    // 计算第i+1个点的x坐标
                    points[i * 4 + 2] = rect.width() * (i + 1) / (bytes.length - 1);
                    // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
                    points[i * 4 + 3] = (rect.height() / 2)
                            + ((byte) (bytes[i + 1] + 128)) * 128
                            / (rect.height() / 2);
                }
                // 绘制波形曲线
                canvas.drawLines(points, paint);
                break;
            // 绘制块状的波形图
            case WAVE_TYPE_BROKEN_LINE:
                default:
                for (int i = 0; i < bytes.length - 1; i++) {
                    float left = width * i / (bytes.length - 1);
                    // 根据波形值计算该矩形的高度
                    float top = rect.height() - (byte) (bytes[i + 1] + 128)
                            * rect.height() / 128;
                    float right = left + 1;
                    float bottom = rect.height();
                    canvas.drawRect(left, top, right, bottom, paint);
                }
                break;
        }
    }
}
