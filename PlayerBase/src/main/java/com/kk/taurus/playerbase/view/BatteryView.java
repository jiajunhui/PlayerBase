package com.kk.taurus.playerbase.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kk.taurus.playerbase.R;

/**
 * Created by Taurus on 16/11/27.
 */

public class BatteryView extends View {

    private final String TAG = "_Battery";
    private final int DEFAULT_BORDER_WIDTH = 3;
    private final int DEFAULT_BORDER_COLOR = Color.WHITE;

    public static final int HEAD_ORIENTATION_LEFT = 0;
    public static final int HEAD_ORIENTATION_RIGHT = 2;

    public static final int SHOW_TYPE_FILL = 0;
    public static final int SHOW_TYPE_NUMBER = 1;

    private final int HEAD_WIDTH = 5;

    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mHeadOrientation = HEAD_ORIENTATION_LEFT;
    private int mProgressColor = DEFAULT_BORDER_COLOR;
    private int mShowType = SHOW_TYPE_FILL;

    private final int MIN_WIDTH = 60;
    private final int MIN_HEIGHT = 30;
    private int mWidth = MIN_WIDTH;
    private int mHeight = MIN_HEIGHT;
    private Paint mProgressPaint,mBorderPaint;

    private final int MAX_PROGRESS = 50;
    private int mProgress = MAX_PROGRESS;

    private final int DEFAULT_NUMBER_TEXT_SIZE = 20;
    private int mNumberTextSize = DEFAULT_NUMBER_TEXT_SIZE;

    private int mBodyWidth;

    private final int DEFAULT_LOW_POWER_VALUE = 20;
    private int mLowPowerValue = DEFAULT_LOW_POWER_VALUE;

    private boolean isLowPowerWarn = true;

    private final int DEFAULT_LOW_POWER_WARN_PROGRESS_COLOR = Color.RED;
    private int mLowPowerProgressColor = DEFAULT_LOW_POWER_WARN_PROGRESS_COLOR;

    public BatteryView(Context context) {
        this(context,null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);
        if (typedArray != null) {
            mBorderColor = typedArray.getColor(R.styleable.BatteryView_border_color, DEFAULT_BORDER_COLOR);
            mProgressColor = typedArray.getColor(R.styleable.BatteryView_progress_color, DEFAULT_BORDER_COLOR);
            mBorderWidth = (int) typedArray.getDimension(R.styleable.BatteryView_border_width, DEFAULT_BORDER_WIDTH);
            mNumberTextSize = (int) typedArray.getDimension(R.styleable.BatteryView_number_text_size, DEFAULT_NUMBER_TEXT_SIZE);
            mHeadOrientation = typedArray.getInt(R.styleable.BatteryView_head_orientation, HEAD_ORIENTATION_LEFT);
            mShowType = typedArray.getInt(R.styleable.BatteryView_show_type, SHOW_TYPE_FILL);
            typedArray.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.TRANSPARENT);
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setTextSize(mNumberTextSize);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBodyWidth = w;
        Log.d(TAG,"width = " + mWidth + " height = " + mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specModeW = MeasureSpec.getMode(widthMeasureSpec);
        int specModeH = MeasureSpec.getMode(heightMeasureSpec);
        int width;
        int height;
        if(specModeW == MeasureSpec.AT_MOST || specModeH == MeasureSpec.AT_MOST
                || specModeW == MeasureSpec.UNSPECIFIED || specModeH == MeasureSpec.UNSPECIFIED) {
            width = MIN_WIDTH;
            height = MIN_HEIGHT;
            setMeasuredDimension(width,height);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
        drawProgress(canvas);
    }

    public int getLowPowerValue() {
        return mLowPowerValue;
    }

    /**
     * 设置低电量预警值
     * @param mLowPowerValue
     */
    public void setLowPowerValue(int mLowPowerValue) {
        boolean needInvalidate = this.mLowPowerValue!=mLowPowerValue && mProgress<=mLowPowerValue;
        this.mLowPowerValue = mLowPowerValue;
        if(needInvalidate){
            invalidate();
        }
    }

    public int getLowPowerProgressColor() {
        return mLowPowerProgressColor;
    }

    /**
     * 设置低电量预警进度颜色
     * @param mLowPowerProgressColor
     */
    public void setLowPowerProgressColor(int mLowPowerProgressColor) {
        boolean needInvalidate = this.mLowPowerProgressColor!=mLowPowerProgressColor && mProgress<=mLowPowerValue;
        this.mLowPowerProgressColor = mLowPowerProgressColor;
        if(needInvalidate){
            invalidate();
        }
    }

    public boolean isLowPowerWarn() {
        return isLowPowerWarn;
    }

    /**
     * 是否开启低电量预警
     * @param lowPowerWarn
     */
    public void setLowPowerWarn(boolean lowPowerWarn) {
        boolean needInvalidate = this.isLowPowerWarn!=lowPowerWarn && mProgress<=mLowPowerValue;
        isLowPowerWarn = lowPowerWarn;
        if(needInvalidate){
            invalidate();
        }
    }

    /**
     * 设置电池电量值
     * @param progress
     */
    public void setBatteryValue(int progress){
        boolean needInvalidate = mProgress != progress;
        this.mProgress = progress;
        if(needInvalidate){
            invalidate();
        }
    }

    /**
     * 设置电量显示类型。
     * @param showType SHOW_TYPE_FILL or SHOW_TYPE_NUMBER
     */
    public void setShowType(int showType){
        boolean needInvalidate = this.mShowType!=showType;
        this.mShowType = showType;
        if(needInvalidate){
            invalidate();
        }
    }

    /**
     * 设置电池正极朝向
     * @param headOrientation HEAD_ORIENTATION_LEFT or HEAD_ORIENTATION_RIGHT
     */
    public void setHeadOrientation(int headOrientation){
        boolean needInvalidate = this.mHeadOrientation!=headOrientation;
        this.mHeadOrientation = headOrientation;
        if(needInvalidate){
            invalidate();
        }
    }

    public void setNumberTextSize(int textSize){
        boolean needInvalidate = mNumberTextSize!=textSize;
        this.mNumberTextSize = textSize;
        mProgressPaint.setTextSize(mNumberTextSize);
        if(needInvalidate){
            invalidate();
        }
    }

    private void drawBorder(Canvas canvas) {
        int[] headRect = new int[4];
        int[] bodyRect = new int[4];
        if(mHeadOrientation == HEAD_ORIENTATION_LEFT){
            headRect[0] = 0;
            headRect[1] = mHeight/4;
            headRect[2] = HEAD_WIDTH;
            headRect[3] = mHeight*3/4;

            bodyRect[0] = HEAD_WIDTH;
            bodyRect[1] = 0;
            bodyRect[2] = mWidth;
            bodyRect[3] = mHeight;
        }else if(mHeadOrientation == HEAD_ORIENTATION_RIGHT){
            headRect[0] = mWidth - HEAD_WIDTH;
            headRect[1] = mHeight/4;
            headRect[2] = mWidth;
            headRect[3] = mHeight*3/4;

            bodyRect[0] = 0;
            bodyRect[1] = 0;
            bodyRect[2] = mWidth - HEAD_WIDTH;
            bodyRect[3] = mHeight;
        }
        mBorderPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new RectF(headRect[0],headRect[1],headRect[2],headRect[3]),mBorderPaint);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        Rect rect = new Rect(bodyRect[0], bodyRect[1], bodyRect[2], bodyRect[3]);
        mBodyWidth = rect.width();
        canvas.drawRect(rect,mBorderPaint);
    }

    private void drawProgress(Canvas canvas) {
        if(mProgress<=mLowPowerValue && isLowPowerWarn){
            mProgressPaint.setColor(mLowPowerProgressColor);
        }else{
            mProgressPaint.setColor(mProgressColor);
        }
        if(mShowType == SHOW_TYPE_FILL){
            int[] rectF = new int[4];
            int progressW = getProgressWidth(mProgress);
            if(mHeadOrientation == HEAD_ORIENTATION_LEFT){
                rectF[0] = mWidth - progressW - mBorderWidth;
                rectF[1] = mBorderWidth;
                rectF[2] = mWidth - mBorderWidth;
                rectF[3] = mHeight - mBorderWidth;
            }else if(mHeadOrientation == HEAD_ORIENTATION_RIGHT){
                rectF[0] = mBorderWidth;
                rectF[1] = mBorderWidth;
                rectF[2] = mBorderWidth + progressW;
                rectF[3] = mHeight - mBorderWidth;
            }
            canvas.drawRect(new RectF(rectF[0],rectF[1],rectF[2],rectF[3]),mProgressPaint);
        }else if(mShowType == SHOW_TYPE_NUMBER){
            String progressText = String.valueOf(mProgress);
            mProgressPaint.setTextAlign(Paint.Align.LEFT);
            Rect bounds = new Rect();
            mProgressPaint.getTextBounds(progressText, 0, progressText.length(), bounds);
            Paint.FontMetricsInt fontMetrics = mProgressPaint.getFontMetricsInt();
            int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(progressText,mBodyWidth / 2 - bounds.width() / 2, baseline, mProgressPaint);
        }
    }

    private int getProgressWidth(int progress){
        return (int) (((mWidth - HEAD_WIDTH - (2*mBorderWidth))*1.0/100)*progress);
    }
}
