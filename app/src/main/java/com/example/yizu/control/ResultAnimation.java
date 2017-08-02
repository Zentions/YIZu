package com.example.yizu.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by q on 2017/7/30.
 */

public class ResultAnimation extends View implements ValueAnimator.AnimatorUpdateListener {
    private Context mContext;
    /**
     * paint对象
     */
    private Paint mPaint;
    /**
     * Path和对应的空Path用来填充
     */
    private Path mPathCircle;
    private Path mPathCircleDst;
    private Path mPathRight;
    private Path mPathRightDst;
    /**
     * Path管理
     */
    private PathMeasure mPathMeasure;
    /**
     * 动画
     */
    private ValueAnimator mCircleAnimator;
    private ValueAnimator mRightAnimator;
    /**
     * 当前绘制进度占总Path长度百分比
     */
    private float mCirclePercent;
    private float mRightPercent;
    /**
     * 线宽
     */
    private int mLineWidth;

    public ResultAnimation(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ResultAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ResultAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mLineWidth = dp2px(3);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GREEN);

        initPath();
    }

    private void initPath() {
        mPathCircle = new Path();
        mPathCircleDst = new Path();
        mPathRight = new Path();
        mPathRightDst = new Path();
        mPathMeasure = new PathMeasure();

        //实例化对象
        mCircleAnimator = ValueAnimator.ofFloat(0, 1);
        //设置时长为1000ms
        mCircleAnimator.setDuration(1000);
        //开始动画
        mCircleAnimator.start();
        //动画监听
        mCircleAnimator.addUpdateListener(this);
        mRightAnimator = ValueAnimator.ofFloat(0, 1);
        mRightAnimator.setDuration(500);
        mRightAnimator.addUpdateListener(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        //画圆
        mPathCircle.addCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2 - mLineWidth, Path.Direction.CW);
        mPathMeasure.setPath(mPathCircle, false);
        mPathMeasure.getSegment(0, mCirclePercent * mPathMeasure.getLength(), mPathCircleDst, true);
        canvas.drawPath(mPathCircleDst, mPaint);

        //画对勾
        mPathRight.moveTo(getWidth() / 4, getWidth() / 2);
        mPathRight.lineTo(getWidth() / 2, getWidth() / 4 * 3);
        mPathRight.lineTo(getWidth() / 4 * 3, getWidth() / 4);
        if (mCirclePercent == 1) {
            mPathMeasure.nextContour();
            mPathMeasure.setPath(mPathRight, false);
            mPathMeasure.getSegment(0, mRightPercent * mPathMeasure.getLength(), mPathRightDst, true);
            canvas.drawPath(mPathRightDst, mPaint);
        }
    }

    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //圆形动画
        if (animation.equals(mCircleAnimator)) {
            mCirclePercent = (float) animation.getAnimatedValue();
            invalidate();
            if (mCirclePercent == 1) {
                mRightAnimator.start();
            }
        }
        //正确时 对勾动画
        else if (animation.equals(mRightAnimator)) {
            mRightPercent = (float) animation.getAnimatedValue();
            invalidate();
        }

    }
    /**
     * 固定写死了宽高，可重新手动调配
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(dp2px(50), dp2px(50));
    }
}

