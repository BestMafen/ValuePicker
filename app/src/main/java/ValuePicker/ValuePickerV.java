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
public class ValuePickerV extends View {
    public enum Gravity {
        START, END
    }

    public enum Orientation {
        LEFT, RIGHT
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
    private int mCentreY;
    /**
     * 控件的高
     */
    private int mWidth;
    /**
     * 滑动的偏移量
     */
    private int mOffset;
    /**
     * 刻度的宽度
     */
    private int mItemHeight = 20;
    /**
     * 起始值
     */
    private float mValueStart;
    /**
     * 结束值
     */
    private float mValueEnd;
    /**
     * 刻度的差值
     */
    private float mValuePer;
    /**
     * 总共刻度
     */
    private int mTotalCounts;

    private OnValuePickedListener mOnValuePickedListener;

    public ValuePickerV(Context context) {
        this(context, null);
    }

    public ValuePickerV(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValuePickerV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mPaintLine = new Paint();
        mPaintLine.setDither(true);
        mPaintLine.setAntiAlias(true);
        c10 = Color.parseColor("#FF4081");
        c5 = Color.parseColor("#999999");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCentreY = getMeasuredHeight() / 2;
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int y, left, right;
        for (int i = 0; i <= mTotalCounts; i++) {
            y = mCentreY + i * mItemHeight - mOffset;
            if (y < 0 || y > mCentreY * 2) {
                continue;
            }
            if (i % 10 == 0) {
                mPaintLine.setColor(c10);
                mPaintLine.setStrokeWidth(w10);
                if (mGravity == Gravity.START) {
                    left = 0;
                    right = h10;
                } else {
                    left = mWidth - h10;
                    right = mWidth;
                }
            } else if (i % 5 == 0) {
                mPaintLine.setColor(c5);
                mPaintLine.setStrokeWidth(w5);
                if (mGravity == Gravity.START) {
                    if (mOrientation == Orientation.LEFT) {
                        left = h10 - h5;
                        right = h10;
                    } else {
                        left = 0;
                        right = h5;
                    }
                } else {
                    if (mOrientation == Orientation.LEFT) {
                        left = mWidth - h5;
                        right = mWidth;
                    } else {
                        left = mWidth - h10;
                        right = left + h5;
                    }
                }
            } else {
                mPaintLine.setColor(c5);
                mPaintLine.setStrokeWidth(w1);
                if (mGravity == Gravity.START) {
                    if (mOrientation == Orientation.LEFT) {
                        left = h10 - h1;
                        right = h10;
                    } else {
                        left = 0;
                        right = h1;
                    }
                } else {
                    if (mOrientation == Orientation.LEFT) {
                        left = mWidth - h1;
                        right = mWidth;
                    } else {
                        left = mWidth - h10;
                        right = left + h1;
                    }
                }
            }
            canvas.drawLine(left, y, right, y, mPaintLine);
        }
    }

    public void setParams(Gravity gravity, Orientation orientation, float start, float end, float per, float selectedValue) {
        this.mGravity = gravity;
        this.mOrientation = orientation;
        this.mValueStart = start;
        this.mValueEnd = end;
        this.mValuePer = per;
        this.mTotalCounts = (int) (Math.abs(start - end) / per);
        this.mOffset = (int) (mItemHeight * (selectedValue - start) / per);
        invalidate();
    }

    public void setOnValuePickedListener(OnValuePickedListener l) {
        this.mOnValuePickedListener = l;
    }

    private int mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int y = (int) event.getY();

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
                mOffset += (mLastY - y);
                if (mOffset < 0) {
                    mOffset = 0;
                }
                if (mOffset > mTotalCounts * mItemHeight) {
                    mOffset = mTotalCounts * mItemHeight;
                }
                returnValue();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(500);
                float yVelocity = mVelocityTracker.getYVelocity();//大约为几千，往右为负，往左为正
                if (Math.abs(yVelocity) > 50) {
                    mScroller.fling(0, 0, 0, (int) (yVelocity), 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    postInvalidate();
                } else {
                    end();
                }
                return false;
        }

        mLastY = y;
        return true;
    }

    private ValueAnimator mVa;

    private void end() {
        int end;
        if (mOffset % mItemHeight > mItemHeight / 2) {
            end = (mOffset / mItemHeight + 1) * mItemHeight;
        } else {
            end = mOffset / mItemHeight * mItemHeight;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > mItemHeight * mTotalCounts) {
            end = mItemHeight * mTotalCounts;
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
            int y = mScroller.getCurrY();
            int dy = y - mScrollerOffset;
            mOffset -= dy;
            if (mOffset < 0) {
                mOffset = 0;
                mScroller.forceFinished(true);
            }
            if (mOffset > mItemHeight * mTotalCounts) {
                mOffset = mItemHeight * mTotalCounts;
                mScroller.forceFinished(true);
            }
            returnValue();
            postInvalidate();
            mScrollerOffset = y;
            if (mScroller.getCurrY() == mScroller.getFinalY()) {
                if (mOffset > 0 && mOffset < mItemHeight * mTotalCounts) {
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
        return mValueStart + mValuePer * (mOffset / mItemHeight);
    }
}
