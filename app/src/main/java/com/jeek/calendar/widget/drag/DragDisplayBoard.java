package com.jeek.calendar.widget.drag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.jeek.calendar.R;

/**
 * Created by Jimmy on 2016/10/28 0028.
 */
public class DragDisplayBoard extends View {

    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;

    private int mColumnSize, mRowSize, mSelectCircleSize;
    private int mDisplayX, mDisplayY;
    private int mSelectCircleColor, mSelectBorderColor;
    private DisplayMetrics mDisplayMetrics;
    private Paint mPaint;
    private boolean mIsDrag;

    public DragDisplayBoard(Context context) {
        this(context, null);
    }

    public DragDisplayBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragDisplayBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context.obtainStyledAttributes(attrs, R.styleable.DragDisplayBoard));
        initPaint();
    }

    private void initAttrs(TypedArray array) {
        if (array != null) {
            mSelectCircleColor = array.getColor(R.styleable.DragDisplayBoard_board_select_circle_color, Color.parseColor("#32FF8594"));
            mSelectBorderColor = array.getColor(R.styleable.DragDisplayBoard_board_select_border_color, Color.parseColor("#FF8594"));
        } else {
            mSelectCircleColor = Color.parseColor("#32FF8594");
            mSelectBorderColor = Color.parseColor("#FF8594");
        }
        mDisplayX = -1;
        mDisplayY = -1;
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        drawDisplayBoard(canvas);
    }

    private void drawDisplayBoard(Canvas canvas) {
        if (mIsDrag) {
            float startX = mDisplayX * mColumnSize;
            float startY = mDisplayY * mRowSize;
            float endX = startX + mColumnSize;
            float endY = startY + mRowSize;
            float x = (startX + endX) / 2;
            float y = (startY + endY) / 2;
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mSelectCircleColor);
            canvas.drawCircle(x, y, mSelectCircleSize, mPaint);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mSelectBorderColor);
            canvas.drawCircle(x, y, mSelectCircleSize, mPaint);
        }
    }

    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
        mSelectCircleSize = (int) (mColumnSize / 3.2);
    }

    public void changePosition(float x, float y) {
        calculatePosition(x, y);
        invalidate();
    }

    public void stopDrag() {
        changePosition(-1, -1);
        mIsDrag = false;
    }

    public void startDrag() {
        mIsDrag = true;
        changePosition(-1, -1);
    }

    private void calculatePosition(float x, float y) {
        if (y <= getHeight() && x >= 0 && y >= 0) {
            mDisplayX = (int) (x / mColumnSize);
            mDisplayY = (int) (y / mRowSize);
        } else {
            mDisplayX = -1;
            mDisplayY = -1;
        }
    }

    public int getDisplayX() {
        return mDisplayX;
    }

    public int getDisplayY() {
        return mDisplayY;
    }
}
