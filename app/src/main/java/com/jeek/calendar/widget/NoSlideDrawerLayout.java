package com.jeek.calendar.widget;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Jimmy on 2016/10/12 0012.
 */
public class NoSlideDrawerLayout extends DrawerLayout {

    private View vMenu;
    private boolean mCanMove;

    public NoSlideDrawerLayout(Context context) {
        super(context);
    }

    public NoSlideDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoSlideDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        vMenu = findViewWithTag("menu");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int width = vMenu.getWidth();
            mCanMove = ev.getX() >= width || ev.getX() < 15;
        }
        try {
            return mCanMove && super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

}
