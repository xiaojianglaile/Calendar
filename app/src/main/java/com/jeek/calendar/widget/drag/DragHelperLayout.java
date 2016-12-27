package com.jeek.calendar.widget.drag;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Jimmy on 2016/10/28 0028.
 */
public class DragHelperLayout extends LinearLayout {

    private DragContainerLayout dclDropContainer;
    private Activity mActivity;

    public DragHelperLayout(Context context) {
        this(context, null);
    }

    public DragHelperLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragHelperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGestureDetector();
    }

    private void initGestureDetector() {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActivity.getCurrentFocus() != null) {
                    mActivity.getCurrentFocus().clearFocus();
                    dclDropContainer.startDrag();
                    dclDropContainer.setFocusable(true);
                    dclDropContainer.setFocusableInTouchMode(true);
                    dclDropContainer.requestFocus();
                    dclDropContainer.requestFocusFromTouch();
                }
                return true;
            }
        });
    }

    public void setDropContainerLayout(DragContainerLayout dragContainerLayout, Activity activity) {
        dclDropContainer = dragContainerLayout;
        mActivity = activity;
    }
}
