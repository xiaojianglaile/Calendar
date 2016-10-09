package com.jeek.calendar.widget.calendar.schedule;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Jimmy on 2016/10/8 0008.
 */
public class ScheduleRecyclerView extends RecyclerView {

    private boolean mIsScrollTop = true;

    public ScheduleRecyclerView(Context context) {
        this(context, null);
    }

    public ScheduleRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mIsScrollTop = recyclerView.canScrollVertically(-1);
            }
        });
    }

    public boolean isScrollTop() {
        return mIsScrollTop;
    }
}