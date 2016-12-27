package com.jeek.calendar.widget.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.jeek.calendar.R;

/**
 * Created by Jimmy on 2016/10/28 0028.
 */
public class DragContainerLayout extends FrameLayout {

    private DragLayout dlDragLayout;
    private DragDisplayBoard ddbDisplayBoard;
    private int mDragLayoutW, mDragLayoutH;
    private int mTopH, mActionBarH;
    private boolean mCanDrag;
    private OnDragFinishedListener mOnDragFinishedListener;

    public DragContainerLayout(Context context) {
        this(context, null);
    }

    public DragContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs();
    }

    private void initAttrs() {
        mActionBarH = getResources().getDimensionPixelOffset(R.dimen.action_bar_height);
        mTopH = mActionBarH + getResources().getDimensionPixelOffset(R.dimen.week_bar_height);
        mCanDrag = false;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dlDragLayout = (DragLayout) findViewById(R.id.dlDragLayout);
        ddbDisplayBoard = (DragDisplayBoard) findViewById(R.id.ddbDisplayBoard);
        dlDragLayout.setVisibility(GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ddbDisplayBoard.layout(left, mTopH, right, ddbDisplayBoard.getHeight() + mTopH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mDragLayoutW = dlDragLayout.getMeasuredWidth();
        mDragLayoutH = dlDragLayout.getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCanDrag) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startDrag();
                case MotionEvent.ACTION_MOVE:
                    move(event);
                    return true;
                case MotionEvent.ACTION_UP:
                    calculatePosition(event.getX(), event.getY());
                    endDrag();
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void move(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (y + mDragLayoutH * 2 / 3 >= mTopH) {
            ddbDisplayBoard.changePosition(x, y - mTopH - mDragLayoutH / 2);
        } else {
            ddbDisplayBoard.changePosition(-1, -1);
        }
        x = x - mDragLayoutW / 2;
        y = y - mDragLayoutH * 2 / 3;
        dlDragLayout.setX(x);
        dlDragLayout.setY(y);
        dlDragLayout.setVisibility(VISIBLE);
    }

    private void calculatePosition(float x, float y) {
        if (mOnDragFinishedListener != null) {
            if (y - mDragLayoutH * 2 / 3 < mActionBarH && x > getWidth() - mActionBarH) {
                mOnDragFinishedListener.onSelectTopBarRightButton();
            } else if (y > mTopH) {
                mOnDragFinishedListener.onSelectDisplayBoard(ddbDisplayBoard.getDisplayX(), ddbDisplayBoard.getDisplayY());
            } else {
                mOnDragFinishedListener.onSelectOther();
            }
        }
    }

    public void setOnDragFinishedListener(OnDragFinishedListener onDragFinishedListener) {
        mOnDragFinishedListener = onDragFinishedListener;
    }

    public void startDrag() {
        mCanDrag = true;
        ddbDisplayBoard.startDrag();
        dlDragLayout.setVisibility(VISIBLE);
    }

    public void endDrag() {
        dlDragLayout.setVisibility(GONE);
        mCanDrag = false;
        ddbDisplayBoard.stopDrag();
    }

}
