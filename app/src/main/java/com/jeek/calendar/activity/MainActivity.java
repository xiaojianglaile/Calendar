package com.jeek.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.jeek.calendar.R;
import com.jeek.calendar.adapter.ScheduleAdapter;
import com.jeek.calendar.bean.Schedule;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.schedule.ScheduleLayout;
import com.jeek.calendar.widget.calendar.schedule.ScheduleRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnCalendarClickListener {

    private ScheduleLayout slSchedule;
    private ScheduleRecyclerView rvScheduleList;
    private ScheduleAdapter mScheduleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slSchedule = (ScheduleLayout) findViewById(R.id.slSchedule);
        slSchedule.setOnCalendarClickListener(this);
        initScheduleList();
    }

    private void initScheduleList() {
        rvScheduleList = slSchedule.getSchedulerRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);
        mScheduleAdapter = new ScheduleAdapter(this, new ArrayList<Schedule>());
        rvScheduleList.setAdapter(mScheduleAdapter);
        initData();
    }

    protected void initData() {
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            schedules.add(new Schedule());
        }
        mScheduleAdapter.changeAllData(schedules);
    }

    @Override
    public void onClickDate(int year, int month, final int day) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("onClickDate", "xxxxxxxxxxxxxxxxx");
                List<Schedule> schedules = new ArrayList<>();
                for (int i = 0; i < day; i++) {
                    schedules.add(new Schedule());
                }
                mScheduleAdapter.changeAllData(schedules);
            }
        });

    }
}
