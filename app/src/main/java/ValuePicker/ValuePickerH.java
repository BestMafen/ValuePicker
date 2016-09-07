package ValuePicker;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ValuePickerH extends View {
    private static final String TAG = "TBG";

    public enum Gravity {
        TOP, BOTTOM
    }

    public enum Orientation {
        UP, DOWN
    }

    private int h10 = 60, h5 = 50, h1 = 30;
    private int w10 = 4, w5 = 4, w1 = 2;
    private int c10, c5;

    private Gravity mGravity;
    private Orientation mOrientation;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private Paint mPaintLine;
    /**
     * 控件的X轴的中心点
     */
    private int mCentreX;
    /**
     * 控件的高
     */
    private int mHeight;
    /**
     * 滑动的偏移量
     */
    private int mOffset;
    /**
     * 刻度的宽度
     */
    private int mItemWidth = 20;
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

    public ValuePickerH(Context context) {
        this(context, null);
    }

    public ValuePickerH(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValuePickerH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
        mPaintLine = new Paint();
        mPaintLine.setDither(true);
        mPaintLine.setAntiAlias(true);
        c10 = Color.parseColor("#FF4081");
        c5 = Color.parseColor("#999999");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCentreX = getMeasuredWidth() / 2;
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x, top, bottom;
        for (int i = 0; i <= mTotalCounts; i++) {
            x = mCentreX + i * mItemWidth - mOffset;
            if (x < 0 || x > mCentreX * 2) {
                continue;
            }
            if (i % 10 == 0) {
                mPaintLine.setColor(c10);
                mPaintLine.setStrokeWidth(w10);
                if (mGravity == Gravity.TOP) {
                    top = 0;
                    bottom = h10;
                } else {
                    top = mHeight - h10;
                    bottom = mHeight;
                }
            } else if (i % 5 == 0) {
                mPaintLine.setColor(c5);
                mPaintLine.setStrokeWidth(w5);
                if (mGravity == Gravity.TOP) {
                    if (mOrientation == Orientation.UP) {
                        top = h10 - h5;
                        bottom = h10;
                    } else {
                        top = 0;
                        bottom = h5;
                    }
                } else {
                    if (mOrientation == Orientation.UP) {
                        top = mHeight - h5;
                        bottom = mHeight;
                    } else {
                        top = mHeight - h10;
                        bottom = top + h5;
                    }
                }
            } else {
                mPaintLine.setColor(c5);
                mPaintLine.setStrokeWidth(w1);
                if (mGravity == Gravity.TOP) {
                    if (mOrientation == Orientation.UP) {
                        top = h10 - h1;
                        bottom = h10;
                    } else {
                        top = 0;
                        bottom = h1;
                    }
                } else {
                    if (mOrientation == Orientation.UP) {
                        top = mHeight - h1;
                        bottom = mHeight;
                    } else {
                        top = mHeight - h10;
                        bottom = top + h1;
                    }
                }
            }
            canvas.drawLine(x, top, x, bottom, mPaintLine);
        }
    }

    public void setParams(Gravity gravity, Orientation orientation, float start, float end, float per, float selectedValue) {
        this.mGravity = gravity;
        this.mOrientation = orientation;
        this.mValueStart = start;
        this.mValuePer = per;
        this.mTotalCounts = (int) (Math.abs(start - end) / per);
        this.mOffset = (int) (mItemWidth * (selectedValue - start) / per);
        invalidate();
    }

    public void setOnValuePickedListener(OnValuePickedListener l) {
        this.mOnValuePickedListener = l;
    }

    private int mLastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();

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
                break;
            case MotionEvent.ACTION_MOVE:
                mOffset += (mLastX - x);
                if (mOffset < 0) {
                    mOffset = 0;
                }
                if (mOffset > mTotalCounts * mItemWidth) {
                    mOffset = mTotalCounts * mItemWidth;
                }
                returnValue();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(500);
                float xVelocity = mVelocityTracker.getXVelocity();//大约为几千，往右为负，往左为正
                if (Math.abs(xVelocity) > 50) {
                    mScroller.fling(0, 0, (int) (xVelocity), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
                    postInvalidate();
                } else {
                    end();
                }
                return false;
        }

        mLastX = x;
        return true;
    }

    private ValueAnimator mVa;

    private void end() {
        int end;
        if (mOffset % mItemWidth > mItemWidth / 2) {
            end = (mOffset / mItemWidth + 1) * mItemWidth;
        } else {
            end = mOffset / mItemWidth * mItemWidth;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > mItemWidth * mTotalCounts) {
            end = mItemWidth * mTotalCounts;
        }
        if (mVa != null) {
            mVa.cancel();
        }
        mVa = ValueAnimator.ofInt(mOffset, end);
        mVa.setDuration(200);
        mVa.start();
        mVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
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
        if (mScroller.computeScrollOffset()) {
            int x = mScroller.getCurrX();
            int dx = x - mScrollerOffset;
            mOffset -= dx;
            if (mOffset < 0) {
                mOffset = 0;
                mScroller.forceFinished(true);
            }
            if (mOffset > mItemWidth * mTotalCounts) {
                mOffset = mItemWidth * mTotalCounts;
                mScroller.forceFinished(true);
            }
            returnValue();
            postInvalidate();
            mScrollerOffset = x;
            if (mScroller.getCurrX() == mScroller.getFinalX()) {
                if (mOffset > 0 && mOffset < mItemWidth * mTotalCounts) {
                    end();
                }
            }
        }
    }

    private void returnValue() {
        if (mOnValuePickedListener != null) {
            mOnValuePickedListener.onValueSelected(getValue());
        }
    }

    private float getValue() {
        return mValueStart + mValuePer * (mOffset / mItemWidth);
    }
}
