package com.jeek.calendar.widget.calendar.schedule;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Jimmy on 2016/10/7 0007.
 */
public class OnScheduleScrollListener extends GestureDetector.SimpleOnGestureListener {

    private ScheduleLayout mScheduleLayout;

    public OnScheduleScrollListener(ScheduleLayout scheduleLayout) {
        mScheduleLayout = scheduleLayout;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mScheduleLayout.onCalendarScroll(distanceY);
        return true;
    }
}
