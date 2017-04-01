package com.lexinsmart.xushun.myseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NewCustomSeekBar extends View {
    public static final String TAG = NewCustomSeekBar.class.getSimpleName();

    //渐变色圆
    private int[] doughnutColors = {Color.parseColor("#ff9900"), Color.parseColor("#ffc66f"), Color.parseColor("#fff4e1")};
    //画笔
    private Paint paint = new Paint();
    //最外圆的半径
    private int mGrideCircleRadius;
    //内圆的半径
    private int mInnerCircleRadius;
    //SeeKBar的Thumb颜色
    private int mStartColor = 0xffff9900;
    //SeekBar的Thumb
    private int mEndColor = 0Xfffff4e1;
    //最外圆的宽度
    private int mGradientCircleWidth;
    //小圆的轨迹
    private int mSmallCircleRadius;
    //内圆的颜色
    private int mInnerCircleColor = 0xffff9b00;
    //字体颜色
    private int mProgressTextColor = 0xffffffff;
    //字体的大小
    private int mProgressTextSize;
    //中心点的坐标
    private int mCenterX;
    //中心点的坐标
    private int mCenterY;
    //thumbX
    private float mThumbX;
    //thumbY
    private float mThumbY;
    //进度
    private int mProgress;
    //当前的角度
    private int mCurrAngle = 0;
    //最大值
    private int mMaxValue = 100;
    //Thumb的颜色
    private int mThumbColor = mEndColor;
    private float mDegrees;
    //Move事件是否响应
    private boolean mMoveRespone = true;
    //缺口的角度
    private int mCurrAngleGap = 15;


    public interface OnProgressListener {
        void onProgressChange(int progress);
    }

    OnProgressListener mOnProgressListener;

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        mOnProgressListener = onProgressListener;
    }


    public NewCustomSeekBar(Context context) {
        this(context, null);
    }

    public NewCustomSeekBar(Context context, int mInnerCircleRadius, int mGrideCircleRadius, int mGradientCircleWidth, int mProgressTextSize) {
        this(context, null);
        initViewData(mInnerCircleRadius, mGrideCircleRadius, mGradientCircleWidth, mProgressTextSize);
    }

    public NewCustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);

    }

    private void initViewData(int mInnerCircleRadius, int mGrideCircleRadius, int mGradientCircleWidth, int mProgressTextSize) {
        this.mInnerCircleRadius = mInnerCircleRadius;
        this.mGrideCircleRadius = mGrideCircleRadius;
        this.mGradientCircleWidth = mGradientCircleWidth;
        this.mProgressTextSize = mProgressTextSize;

        mSmallCircleRadius = mGrideCircleRadius;
        mCenterX = mGrideCircleRadius + mGradientCircleWidth;
        mCenterY = mGrideCircleRadius + mGradientCircleWidth;
        mThumbX = (float) (mCenterX - (mSmallCircleRadius) * Math.sin(mCurrAngleGap * Math.PI / 180));
        mThumbY = (float) (mCenterY - (mSmallCircleRadius) * Math.cos(mCurrAngleGap * Math.PI / 180));
    }

    private void initData(Context context, AttributeSet attrs) {

        // 获取自定义组件的属性
        TypedArray types = context.obtainStyledAttributes(attrs,
                R.styleable.NewCustomSeekBar_csSrcBtn);
        try {
            mInnerCircleRadius = types.getDimensionPixelOffset(
                    R.styleable.NewCustomSeekBar_csSrcBtn_InnerCircle_radius, 150);
            mGrideCircleRadius = types.getDimensionPixelOffset(
                    R.styleable.NewCustomSeekBar_csSrcBtn_ExCircle_radius, 200);
            mGradientCircleWidth = types.getDimensionPixelOffset(R.styleable.NewCustomSeekBar_csSrcBtn_SmartCircle_radius,
                    20);
            mProgressTextSize = types.getDimensionPixelOffset(R.styleable.NewCustomSeekBar_csSrcBtn_TextSize,
                    20);
        } finally {
            types.recycle(); // TypeArray用完需要recycle
        }
        mSmallCircleRadius = mGrideCircleRadius;
        mCenterX = mGrideCircleRadius + mGradientCircleWidth;
        mCenterY = mGrideCircleRadius + mGradientCircleWidth;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mCenterX * 2, mCenterY * 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //渐变圆
        drawGradientExCircle(canvas);
        //画SeeKBar的Thumb
        drawThumb(canvas);
        //画内圈大圆
        drawInnerCircle(canvas);
        //画字体
        drawProgressText(canvas);
    }


    private void drawThumb(Canvas canvas) {

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mGradientCircleWidth / 4);
        canvas.drawCircle(mThumbX, mThumbY, mGradientCircleWidth * 0.6f, paint);

        paint.setColor(mThumbColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        canvas.drawCircle(mThumbX, mThumbY, mGradientCircleWidth * 0.6f, paint);

    }

    private void drawProgressText(Canvas canvas) {
        paint.reset();
        paint.setAntiAlias(true);

        paint.setColor(mProgressTextColor);
        paint.setTextSize(mProgressTextSize);
        //字体居中操作
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (mCenterY - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(mProgress + "%", mCenterX, baseLineY, paint);
    }

    private void drawInnerCircle(Canvas canvas) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(mInnerCircleColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX, mCenterY, mInnerCircleRadius, paint);
    }

    private void drawGradientExCircle(Canvas canvas) {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(mGradientCircleWidth);
        paint.setStyle(Paint.Style.STROKE);
        SweepGradient sweepGradient = new SweepGradient(mCenterX, mCenterY, doughnutColors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, mCenterX, mCenterY);
        paint.setStrokeCap(Paint.Cap.ROUND);
        sweepGradient.setLocalMatrix(matrix);
        paint.setShader(sweepGradient);
        RectF rect = new RectF(mCenterX - mGrideCircleRadius, mCenterY - mGrideCircleRadius,
                mCenterX + mGrideCircleRadius, mCenterY + mGrideCircleRadius);
        canvas.drawArc(rect, -90 + mCurrAngleGap, 360 - mCurrAngleGap * 2, false, paint);

    }


    //移动的当前的点
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float currDegress = (float) ((float) ((Math.toDegrees(Math.atan2(x - mCenterX, mCenterY - y)) + 360.0)) % 360.0);
        if (currDegress > (360 - mCurrAngleGap) || currDegress < mCurrAngleGap) {
            mMoveRespone = false;
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((Math.pow((x - mCenterX), 2) + (Math.pow(((y - mCenterY)), 2)) > Math.pow((mGrideCircleRadius + mGradientCircleWidth), 2))) {
                    return false;
                }
                mMoveRespone = true;
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (!mMoveRespone) {
                    return false;
                }
                //计算坐标点
                mThumbX = (float) (mCenterX + (mSmallCircleRadius) * Math.cos(Math.atan2(x - mCenterX, mCenterY - y) - (Math.PI / 2)));
                mThumbY = (float) (mCenterY + (mSmallCircleRadius) * Math.sin(Math.atan2(x - mCenterX, mCenterY - y) - (Math.PI / 2)));
                double degress = Math.toDegrees(Math.atan2(x - mCenterX, mCenterY - y));
                if (degress < 0) {
                    degress += mCurrAngleGap;
                } else if (degress > 0) {
                    degress -= mCurrAngleGap;
                }
                //计算角度
                mDegrees = ((float) ((degress + (360 - mCurrAngleGap * 2))) % (360 - mCurrAngleGap * 2));
                // and to make it count 0-360
                Log.i(TAG, "Degrees==" + mDegrees);
                if (mDegrees < 0) {
                    mDegrees += 2 * Math.PI;
                }
                if (mDegrees < 2) {
                    mDegrees = 0;
                }
                mThumbColor = Util.UIColor.interpolate(mStartColor, mEndColor, (mDegrees / (360 - mCurrAngleGap * 2)));
                mProgress = (int) ((1 - ((mDegrees) / (360 - mCurrAngleGap * 2))) * mMaxValue);
                mOnProgressListener.onProgressChange(mProgress);
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    //设置最大值
    public void setMaxProgress(int maxValue) {
        mMaxValue = maxValue;
        invalidate();
    }

    //设置进度
    public void setCurrProgress(float currProgress) {
        if (currProgress <= 0 || currProgress > 1) {
            return;
        }
        mProgress = (int) (currProgress * mMaxValue);
        mThumbColor = Util.UIColor.interpolate(mStartColor, mEndColor, 1 - currProgress);
        mCurrAngle = (int) ((currProgress) * (360 - mCurrAngleGap * 2)) + mCurrAngleGap;
        if (mCurrAngle >= 0 && mCurrAngle < 90) {
            mThumbX = (float) (mCenterX - (mSmallCircleRadius) * Math.sin(mCurrAngle * Math.PI / 180));
            mThumbY = (float) (mCenterY - (mSmallCircleRadius) * Math.cos(mCurrAngle * Math.PI / 180));
        } else if (mCurrAngle >= 90 && mCurrAngle < 180) {
            mThumbX = (float) (mCenterX - (mSmallCircleRadius) * Math.cos((mCurrAngle - 90) * Math.PI / 180));
            mThumbY = (float) (mCenterY + (mSmallCircleRadius) * Math.sin((mCurrAngle - 90) * Math.PI / 180));
        } else if (mCurrAngle >= 180 && mCurrAngle < 270) {
            mThumbX = (float) (mCenterX + (mSmallCircleRadius) * Math.sin((mCurrAngle - 180) * Math.PI / 180));
            mThumbY = (float) (mCenterY + (mSmallCircleRadius) * Math.cos((mCurrAngle - 180) * Math.PI / 180));
        } else {
            mThumbX = (float) (mCenterX + (mSmallCircleRadius) * Math.cos((mCurrAngle - 270) * Math.PI / 180));
            mThumbY = (float) (mCenterY - (mSmallCircleRadius) * Math.sin((mCurrAngle - 270) * Math.PI / 180));
        }
        invalidate();
    }
}