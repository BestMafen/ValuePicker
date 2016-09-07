package ValuePicker;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import java.text.DecimalFormat;

public class ValuePickerC extends View {
    public enum Gravity {
        START, END, TOP, BOTTOM, CENTRE
    }

    private int h10 = 60, h5 = 50, h1 = 30;
    private int w10 = 4, w5 = 4, w1 = 2;
    private int c10, c5;

    private Gravity mGravity = Gravity.START;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private Paint mPaintLine;
    /**
     * 控件的X轴的中心点
     */
    private int mCentreY;
    /**
     * 控件的X轴的中心点
     */
    private int mCentreX;
    /**
     * 控件的高
     */
    private int mRadius;
    private int mWidth, mHeight;
    /**
     * 滑动的偏移量
     */
    private float mOffset;
    /**
     * 刻度的宽度
     */
    private int mItemDegree = 3;
    /**
     * 起始值
     */
    private float mValueStart;
    /**
     * 刻度的差值
     */
    private float mValuePer;
    /**
     * 总共刻度
     */
    private int mTotalCounts;

    private OnValuePickedListener mOnValuePickedListener;

    public ValuePickerC(Context context) {
        this(context, null);
    }

    public ValuePickerC(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValuePickerC(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mPaintLine = new Paint();
        mPaintLine.setDither(true);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setTextSize(56);
        c10 = Color.parseColor("#FF4081");
        c5 = Color.parseColor("#999999");
    }

    RectF mRange;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        switch (mGravity) {
            case START:
                mCentreX = 0;
                mCentreY = mHeight / 2;
                mRadius = mCentreY;
                mRange = new RectF(-mRadius, 0, mRadius, mHeight);
                break;
            case END:
                mCentreX = mWidth;
                mCentreY = mHeight / 2;
                mRadius = mCentreY;
                mRange = new RectF(mWidth - mRadius, 0, mWidth + mRadius, mHeight);
                break;
            case TOP:
                mCentreX = mWidth / 2;
                mCentreY = 0;
                mRadius = mCentreX;
                mRange = new RectF(0, -mRadius, mWidth, mRadius);
                break;
            case BOTTOM:
                mCentreX = mWidth / 2;
                mRadius = mWidth / 2;
                mCentreY = mHeight;
                mRange = new RectF(0, mHeight - mRadius, mWidth, mHeight + mRadius);
                break;
            case CENTRE:
                mCentreX = mWidth / 2;
                mCentreY = mHeight / 2;
                mRadius = mHeight < mWidth ? mHeight / 2 : mWidth / 2;
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (mGravity == Gravity.BOTTOM) {
//            mPaintLine.setColor(c5);
//            mPaintLine.setStrokeWidth(w5);
//            mPaintLine.setStyle(Paint.Style.STROKE);
//            canvas.drawCircle(mWidth / 2, mHeight, mRadius, mPaintLine);
//        }
        int startX = 0, endX = 0, startY = 0, endY = 0;
        float offset;
        for (int i = 0; i <= mTotalCounts; i++) {
            switch (mGravity) {
                case START:
                    offset = mOffset - mItemDegree * i;
                    if (offset < -90 || offset > 90) {
                        continue;
                    }
                    startX = (int) (mRadius * Math.cos(offset * Math.PI / 180));
                    startY = (int) (mRadius - mRadius * Math.sin(offset * Math.PI / 180));
                    if (i % 10 == 0) {
                        endX = (int) ((mRadius - h10) * Math.cos(offset * Math.PI / 180));
                        endY = (int) (mRadius - (mRadius - h10) * Math.sin(offset * Math.PI / 180));

                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        mPaintLine.setStyle(Paint.Style.FILL);
                        String text = String.valueOf(mValueStart + i * mValuePer);
                        Path path = new Path();
                        path.addArc(mRange, -30 - offset, 60);
                        float textWidth = mPaintLine.measureText(text);
                        float hOffset = (float) (Math.PI * mRadius / 6 - textWidth / 2);
                        float vOffset = h10 + 40;
                        canvas.drawTextOnPath(text, path, hOffset, vOffset, mPaintLine);

                        mPaintLine.setColor(c10);
                        mPaintLine.setStrokeWidth(w10);
                    } else if (i % 5 == 0) {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w5);
                        endX = (int) ((mRadius - h5) * Math.cos(offset * Math.PI / 180));
                        endY = (int) (mRadius - (mRadius - h5) * Math.sin(offset * Math.PI / 180));
                    } else {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        endX = (int) ((mRadius - h1) * Math.cos(offset * Math.PI / 180));
                        endY = (int) (mRadius - (mRadius - h1) * Math.sin(offset * Math.PI / 180));
                    }
                    break;
                case END:
                    offset = mOffset - mItemDegree * i;
                    if (offset < -90 || offset > 90) {
                        continue;
                    }
                    startX = (int) (mWidth - mRadius * Math.cos(offset * Math.PI / 180));
                    startY = (int) (mRadius - mRadius * Math.sin(offset * Math.PI / 180));
                    if (i % 10 == 0) {
                        endX = (int) (mWidth - (mRadius - h10) * Math.cos(offset * Math.PI / 180));
                        endY = (int) (mRadius - (mRadius - h10) * Math.sin(offset * Math.PI / 180));

                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        mPaintLine.setStyle(Paint.Style.FILL);
                        String text = String.valueOf(mValueStart + i * mValuePer);
                        Path path = new Path();
                        path.addArc(mRange, offset + 150, 60);
                        float textWidth = mPaintLine.measureText(text);
                        float hOffset = (float) (Math.PI * mRadius / 6 - textWidth / 2);
                        float vOffset = h10 + 40;
                        canvas.drawTextOnPath(text, path, hOffset, vOffset, mPaintLine);

                        mPaintLine.setColor(c10);
                        mPaintLine.setStrokeWidth(w10);
                    } else if (i % 5 == 0) {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w5);
                        endX = (int) (mWidth - (mRadius - h5) * Math.cos(offset * Math.PI / 180));
                        endY = (int) (mRadius - (mRadius - h5) * Math.sin(offset * Math.PI / 180));
                    } else {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        endX = (int) (mWidth - (mRadius - h1) * Math.cos(offset * Math.PI / 180));
                        endY = (int) (mRadius - (mRadius - h1) * Math.sin(offset * Math.PI / 180));
                    }
                    break;
                case TOP:
                    offset = mItemDegree * i - mOffset;
                    if (offset < -90 || offset > 90) {
                        continue;
                    }
                    startX = (int) (mRadius * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                    startY = (int) (mRadius * Math.cos(offset * Math.PI / 180));
                    if (i % 10 == 0) {
                        endX = (int) ((mRadius - h10) * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                        endY = (int) ((mRadius - h10) * Math.cos(offset * Math.PI / 180));

                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        mPaintLine.setStyle(Paint.Style.FILL);
                        String text = String.valueOf(mValueStart + i * mValuePer);
                        Path path = new Path();
                        path.addArc(mRange, 60 - offset, 60);
                        float textWidth = mPaintLine.measureText(text);
                        float hOffset = (float) (Math.PI * mRadius / 6 - textWidth / 2);
                        float vOffset = h10 + 40;
                        canvas.drawTextOnPath(text, path, hOffset, vOffset, mPaintLine);

                        mPaintLine.setColor(c10);
                        mPaintLine.setStrokeWidth(w10);
                    } else if (i % 5 == 0) {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w5);
                        endX = (int) ((mRadius - h5) * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                        endY = (int) ((mRadius - h5) * Math.cos(offset * Math.PI / 180));
                    } else {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        endX = (int) ((mRadius - h1) * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                        endY = (int) ((mRadius - h1) * Math.cos(offset * Math.PI / 180));
                    }
                    break;
                case BOTTOM:
                    offset = mItemDegree * i - mOffset;
                    if (offset < -90 || offset > 90) {
                        continue;
                    }
                    startX = (int) (mRadius * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                    startY = (int) (mHeight - mRadius * Math.cos(offset * Math.PI / 180));
                    if (i % 10 == 0) {
                        endX = (int) ((mRadius - h10) * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                        endY = (int) (mHeight - (mRadius - h10) * Math.cos(offset * Math.PI / 180));

                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        mPaintLine.setStyle(Paint.Style.FILL);
                        String text = String.valueOf(mValueStart + i * mValuePer);
                        Path path = new Path();
                        path.addArc(mRange, offset - 120, 60);
                        float textWidth = mPaintLine.measureText(text);
                        float hOffset = (float) (Math.PI * mRadius / 6 - textWidth / 2);
                        float vOffset = h10 + 40;
                        canvas.drawTextOnPath(text, path, hOffset, vOffset, mPaintLine);

                        mPaintLine.setColor(c10);
                        mPaintLine.setStrokeWidth(w10);
                    } else if (i % 5 == 0) {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w5);
                        endX = (int) ((mRadius - h5) * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                        endY = (int) (mHeight - (mRadius - h5) * Math.cos(offset * Math.PI / 180));
                    } else {
                        mPaintLine.setColor(c5);
                        mPaintLine.setStrokeWidth(w1);
                        endX = (int) ((mRadius - h1) * Math.sin(offset * Math.PI / 180) + mWidth / 2);
                        endY = (int) (mHeight - (mRadius - h1) * Math.cos(offset * Math.PI / 180));
                    }
                    break;
                case CENTRE:

                    break;
            }
            canvas.drawLine(startX, startY, endX, endY, mPaintLine);
        }
    }

    public void setParams(Gravity gravity, float start, float end, float per, float selectedValue) {
        this.mGravity = gravity;
        this.mValueStart = start;
        this.mValuePer = per;
        this.mTotalCounts = (int) (Math.abs(start - end) / per);
        this.mOffset = mItemDegree * (selectedValue - start) / per;
        invalidate();
    }

    public void setOnValuePickedListener(OnValuePickedListener l) {
        this.mOnValuePickedListener = l;
    }

    private float mLastX;
    private float mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollerOffset = 0;
                if (mVa != null) {
                    mVa.cancel();
                }
                mScroller.forceFinished(true);
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mGravity == Gravity.START || mGravity == Gravity.END) {
                    float dy = mLastY - y;
                    float offset = (float) (180 * dy / Math.PI / mRadius);
                    mOffset += offset;
                    if (mOffset < 0) {
                        mOffset = 0;
                    }
                    if (mOffset > mTotalCounts * mItemDegree) {
                        mOffset = mTotalCounts * mItemDegree;
                    }
                } else if (mGravity == Gravity.TOP || mGravity == Gravity.BOTTOM) {
                    float dx = mLastX - x;
                    float offset = (float) (180 * dx / Math.PI / mRadius);
                    mOffset += offset;
                    if (mOffset < 0) {
                        mOffset = 0;
                    }
                    if (mOffset > mTotalCounts * mItemDegree) {
                        mOffset = mTotalCounts * mItemDegree;
                    }
                }
                returnValue();
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                if (mGravity == Gravity.START || mGravity == Gravity.END) {
                    mVelocityTracker.computeCurrentVelocity(500);
                    float yVelocity = mVelocityTracker.getYVelocity();//大约为几千，往右为负，往左为正
                    if (Math.abs(yVelocity) > 50) {
                        mScroller.fling(0, 0, 0, (int) (yVelocity / 2), 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        postInvalidate();
                    } else {
                        end();
                    }
                } else if (mGravity == Gravity.TOP || mGravity == Gravity.BOTTOM) {
                    mVelocityTracker.computeCurrentVelocity(500);
                    float xVelocity = mVelocityTracker.getXVelocity();//大约为几千，往右为负，往左为正
                    if (Math.abs(xVelocity) > 50) {
                        mScroller.fling(0, 0, (int) (xVelocity / 2), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
                        postInvalidate();
                    } else {
                        end();
                    }
                }
                break;
        }
        return true;
    }

    private ValueAnimator mVa;

    private void end() {
        int end;
        if (mOffset % mItemDegree > (mItemDegree / 2)) {
            end = ((int) (mOffset / mItemDegree) + 1) * mItemDegree;
        } else {
            end = (int) (mOffset / mItemDegree) * mItemDegree;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > mItemDegree * mTotalCounts) {
            end = mItemDegree * mTotalCounts;
        }
        if (mVa != null) {
            mVa.cancel();
        }
        mVa = ValueAnimator.ofFloat(mOffset, end);
        mVa.setDuration(400);
        mVa.start();
        mVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                returnValue();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private int mScrollerOffset;

    @Override
    public void computeScroll() {
        switch (mGravity) {
            case START:
            case END:
                if (mScroller.computeScrollOffset()) {
                    int y = mScroller.getCurrY();
                    int dy = y - mScrollerOffset;
                    mOffset -= dy;
                    if (mOffset < 0) {
                        mOffset = 0;
                        mScroller.forceFinished(true);
                    }
                    if (mOffset > mItemDegree * mTotalCounts) {
                        mOffset = mItemDegree * mTotalCounts;
                        mScroller.forceFinished(true);
                    }
                    returnValue();
                    postInvalidate();
                    mScrollerOffset = y;
                    if (mScroller.getCurrY() == mScroller.getFinalY()) {
                        if (mOffset > 0 && mOffset < mItemDegree * mTotalCounts) {
                            end();
                        }
                    }
                }
                break;
            case TOP:
            case BOTTOM:
                if (mScroller.computeScrollOffset()) {
                    int x = mScroller.getCurrX();
                    int dx = x - mScrollerOffset;
                    mOffset -= dx;
                    if (mOffset < 0) {
                        mOffset = 0;
                        mScroller.forceFinished(true);
                    }
                    if (mOffset > mItemDegree * mTotalCounts) {
                        mOffset = mItemDegree * mTotalCounts;
                        mScroller.forceFinished(true);
                    }
                    returnValue();
                    postInvalidate();
                    mScrollerOffset = x;
                    if (mScroller.getCurrX() == mScroller.getFinalX()) {
                        if (mOffset > 0 && mOffset < mItemDegree * mTotalCounts) {
                            end();
                        }
                    }
                }
                break;
        }
    }

    private void returnValue() {
        if (mOnValuePickedListener != null) {
            mOnValuePickedListener.onValueSelected(getValue());
        }
    }

    private DecimalFormat mDecimalFormat = new DecimalFormat("####0.0");

    private float getValue() {
        return Float.parseFloat(mDecimalFormat.format(mValueStart + mValuePer * (mOffset / mItemDegree)));
    }
}