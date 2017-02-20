package com.jeek.calendar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Created by Jimmy on 2016/10/12 0012.
 */
public class SlideDeleteView extends FrameLayout {

    private int mDeleteViewWidth;
    private int mWidth;
    private View vDeleteView;
    private boolean mIsOpen = false;
    private OnContentClickListener mOnContentClickListener;
    private boolean mIsMove = false;

    public SlideDeleteView(Context context) {
        this(context, null);
    }

    public SlideDeleteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float downX, moveX;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private long startTime, endTime;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                moveX = downX;
                startTime = System.currentTimeMillis();
                mIsMove = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getX() - moveX) >= 5) {
                    moveChild(event.getX() - moveX);
                    moveX = event.getX();
                    mIsMove = true;
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                endTime = System.currentTimeMillis();
                moveX = event.getX();
                determineSpeed();
                isClickContentView(event);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void isClickContentView(MotionEvent event) {
        if (!mIsMove && Math.abs(event.getX() - downX) < 5 && mOnContentClickListener != null) {
            if (mIsOpen) {
                close(true);
            } else {
                mOnContentClickListener.onContentClick();
            }
        }
        downX = 0;
        moveX = 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if ("delete".equals(child.getTag())) {
                vDeleteView = child;
                mDeleteViewWidth = child.getWidth();
                child.layout(right, 0, right + mDeleteViewWidth, child.getHeight());
            }
        }
    }

    private void moveChild(float distanceX) {
        if (vDeleteView != null) {
            if (vDeleteView.getX() >= mWidth - mDeleteViewWidth && vDeleteView.getX() <= mWidth) {
                distanceX *= 1.5;
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    float positionX = child.getX() + distanceX;
                    if (child != vDeleteView) {
                        positionX = Math.max(positionX, -mDeleteViewWidth);
                        positionX = Math.min(positionX, 0);
                    } else {
                        positionX = Math.max(positionX, mWidth - mDeleteViewWidth);
                        positionX = Math.min(positionX, mWidth);
                    }
                    child.setX(positionX);
                }
            }
        }
    }

    private void determineSpeed() {
        if ((moveX - downX) / (endTime - startTime) < -0.5) {
            mIsOpen = true;
            vDeleteView.startAnimation(new AutoMoveAnimation((long) (mWidth - mDeleteViewWidth / 2 - vDeleteView.getX())));
        } else if ((moveX - downX) / (endTime - startTime) > 0.5) {
            mIsOpen = false;
            vDeleteView.startAnimation(new AutoMoveAnimation((long) (mWidth - vDeleteView.getX())));
        } else {
            determineTheState();
        }
    }

    private void determineTheState() {
        if (vDeleteView.getX() < mWidth - mDeleteViewWidth / 2) { // open
            mIsOpen = true;
            if (vDeleteView.getX() != mWidth - mDeleteViewWidth) {
                vDeleteView.startAnimation(new AutoMoveAnimation((long) (mWidth - mDeleteViewWidth / 2 - vDeleteView.getX())));
            }
        } else { // close
            mIsOpen = false;
            if (vDeleteView.getX() != mWidth) {
                vDeleteView.startAnimation(new AutoMoveAnimation((long) (mWidth - vDeleteView.getX())));
            }
        }
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    private class AutoMoveAnimation extends Animation {

        private AutoMoveAnimation(long duration) {
            setDuration(Math.max(duration * 10, mDeleteViewWidth / 5));
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (isOpen()) {
                moveChild(-mDeleteViewWidth / 10);
            } else {
                moveChild(mDeleteViewWidth / 10);
            }
        }
    }

    public void close(boolean anim) {
        if (isOpen()) {
            mIsOpen = false;
            if (anim) {
                vDeleteView.startAnimation(new AutoMoveAnimation((long) (mWidth - vDeleteView.getX())));
            } else {
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.setX(child.getX() + mDeleteViewWidth);
                }
            }
        }
    }

    public void setOnContentClickListener(OnContentClickListener onContentClickListener) {
        mOnContentClickListener = onContentClickListener;
    }

    public interface OnContentClickListener {
        void onContentClick();
    }

}
