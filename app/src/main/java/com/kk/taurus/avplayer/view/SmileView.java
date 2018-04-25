package com.kk.taurus.avplayer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Taurus on 2018/1/21.
 */

public class SmileView extends View {

    final int DEFAULT_BORDER_WIDTH = 5;
    final long ANIMATION_DURATION = 400;

    private int mWidth, mHeight;
    private int mCenterX, mCenterY;

    private Paint mBorderPaint;
    private Paint mFacePaint;

    private int borderWidth;
    private int borderColor;
    private int faceColor;

    private Path mLeftPath;
    private Path mRightPath;
    private Path mBottomPath;

    /**
     * 左眼坐标
     */
    private float[] leftPoint;

    /**
     * 右眼坐标
     */
    private float[] rightPoint;

    /**
     * 嘴巴坐标
     */
    private float[] bottomPoint;

    /**
     * 左眼控制点范围
     */
    private float[] leftControlLimit;

    /**
     * 右眼控制点范围
     */
    private float[] rightControlLimit;

    /**
     * 嘴巴控制点范围
     */
    private float[] bottomControlLimit;

    private float leftControlY;
    private float rightControlY;
    private float bottomControlY;

    private ValueAnimator mValueAnimator01;
    private ValueAnimator mValueAnimator02;
    private boolean hasAttach;

    private void startAnimation01(){
        final float dValueTop = leftControlLimit[1] - leftControlLimit[0];
        final float dValueBottom = bottomControlLimit[1] - bottomControlLimit[0];
        mValueAnimator01 = ValueAnimator.ofFloat(0f, 1.0f);
        mValueAnimator01.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                leftControlY = rightControlY = leftControlLimit[0]  + (dValueTop * value);
                bottomControlY = bottomControlLimit[1] - (dValueBottom * value);
                invalidate();
            }
        });
        mValueAnimator01.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startAnimation02();
            }
        });
        mValueAnimator01.setDuration(ANIMATION_DURATION);
        mValueAnimator01.start();
    }

    private void startAnimation02(){
        final float dValueTop = leftControlLimit[1] - leftControlLimit[0];
        final float dValueBottom = bottomControlLimit[1] - bottomControlLimit[0];
        mValueAnimator02 = ValueAnimator.ofFloat(1.0f, 0f);
        mValueAnimator02.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                leftControlY = rightControlY = leftControlLimit[0]  + (dValueTop * value);
                bottomControlY = bottomControlLimit[1] - (dValueBottom * value);
                invalidate();
            }
        });
        mValueAnimator02.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startAnimation01();
            }
        });
        mValueAnimator02.setDuration(ANIMATION_DURATION);
        mValueAnimator02.start();
    }

    private void cancelAnimator01(){
        if(mValueAnimator01!=null){
            mValueAnimator01.removeAllListeners();
            mValueAnimator01.removeAllUpdateListeners();
            mValueAnimator01.cancel();
            mValueAnimator01 = null;
        }
    }

    private void cancelAnimator02(){
        if(mValueAnimator02!=null){
            mValueAnimator02.removeAllListeners();
            mValueAnimator02.removeAllUpdateListeners();
            mValueAnimator02.cancel();
            mValueAnimator02 = null;
        }
    }

    public SmileView(Context context) {
        this(context, null);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        borderWidth = DEFAULT_BORDER_WIDTH;
        borderColor = Color.YELLOW;
        faceColor = borderColor;

        leftPoint = new float[4];
        rightPoint = new float[4];
        bottomPoint = new float[4];

        leftControlLimit = new float[2];
        rightControlLimit = new float[2];
        bottomControlLimit = new float[2];

        mLeftPath = new Path();
        mRightPath = new Path();
        mBottomPath = new Path();

        mBorderPaint = new Paint();
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mFacePaint = new Paint();
        mFacePaint.setColor(faceColor);
        mFacePaint.setStrokeWidth(borderWidth);
        mFacePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = mWidth/2;
        mCenterY = mHeight/2;

        mBorderPaint.setShader(new LinearGradient(0, 0, mWidth, mHeight, Color.YELLOW, Color.BLUE, Shader.TileMode.MIRROR));
        mFacePaint.setShader(new LinearGradient(0, 0, mWidth, mHeight, Color.YELLOW, Color.BLUE, Shader.TileMode.MIRROR));

        float dVH = 0.2f;
        float dVV = 0.3f;
        float topV1 = 0.3f;
        float topV2 = 1.1f;

        leftPoint[0] = mCenterX * dVH;
        leftPoint[1] = mCenterY - (mCenterY * dVV);

        leftPoint[2] = mCenterX - (mCenterX * dVH);
        leftPoint[3] = leftPoint[1];

        leftControlLimit[0] = rightControlLimit[0] = mCenterY * topV1;
        leftControlLimit[1] = rightControlLimit[1] = mCenterY * topV2;

        rightPoint[0] = mCenterX + (mCenterX * dVH);
        rightPoint[1] = leftPoint[1];

        rightPoint[2] = mWidth - (mCenterX * dVH);
        rightPoint[3] = rightPoint[1];

        float dVH1 = 0.5f;
        float dVV1 = 0.4f;
        float bottomV1 = 0f;
        float bottomV2 = 0.9f;

        bottomPoint[0] = mCenterX - (mCenterX * dVH1);
        bottomPoint[1] = mCenterY + (mCenterY * dVV1);

        bottomPoint[2] = mCenterX + (mCenterY * dVH1);
        bottomPoint[3] = bottomPoint[1];

        bottomControlLimit[0] = mCenterY + (mCenterY * bottomV1);
        bottomControlLimit[1] = mCenterY + (mCenterY * bottomV2);
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setFaceColor(int faceColor) {
        this.faceColor = faceColor;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hasAttach = true;
        startAnimation01();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimator01();
        cancelAnimator02();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!hasAttach)
            return;
        drawBorder(canvas);
        drawFace(canvas);
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawRoundRect(new RectF(borderWidth/2, borderWidth/2, mWidth - (borderWidth/2), mHeight - (borderWidth/2)), 10, 10, mBorderPaint);
    }

    private void drawFace(Canvas canvas) {
        canvas.drawPoint(mCenterX, mCenterY ,mFacePaint);

        mLeftPath.reset();
        mRightPath.reset();
        mBottomPath.reset();

        mLeftPath.moveTo(leftPoint[0], leftPoint[1]);
        mLeftPath.quadTo(mCenterX/2, leftControlY, leftPoint[2], leftPoint[3]);
        canvas.drawPath(mLeftPath, mFacePaint);

        mRightPath.moveTo(rightPoint[0], rightPoint[1]);
        mRightPath.quadTo(mCenterX + (mCenterX/2), rightControlY, rightPoint[2], rightPoint[3]);
        canvas.drawPath(mRightPath, mFacePaint);

        mBottomPath.moveTo(bottomPoint[0], bottomPoint[1]);
        mBottomPath.quadTo(mCenterX, bottomControlY, bottomPoint[2], bottomPoint[3]);
        canvas.drawPath(mBottomPath, mFacePaint);
    }
}
