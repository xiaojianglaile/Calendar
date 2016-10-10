package com.jeek.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeek.calendar.R;
import com.jeek.calendar.adapter.ScheduleAdapter;
import com.jeek.calendar.bean.Schedule;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.schedule.ScheduleLayout;
import com.jeek.calendar.widget.calendar.schedule.ScheduleRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnCalendarClickListener {

    private LinearLayout llTitleDate;
    private TextView tvTitleMonth, tvTitleDay, tvTitle;

    private ScheduleLayout slSchedule;
    private ScheduleRecyclerView rvScheduleList;
    private ScheduleAdapter mScheduleAdapter;

    private String[] mMonthText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        initData();
        bindData();
    }

    protected void bindView() {
        setContentView(R.layout.activity_main);
        llTitleDate = (LinearLayout) findViewById(R.id.llTitleDate);
        tvTitleMonth = (TextView) findViewById(R.id.tvTitleMonth);
        tvTitleDay = (TextView) findViewById(R.id.tvTitleDay);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        slSchedule = (ScheduleLayout) findViewById(R.id.slSchedule);
        slSchedule.setOnCalendarClickListener(this);
        initTitleBar();
        initScheduleList();
    }

    private void initTitleBar() {
        mMonthText = getResources().getStringArray(R.array.calendar_month);
        llTitleDate.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.GONE);
        tvTitleMonth.setText(mMonthText[slSchedule.getCurrentSelectMonth()]);
        tvTitleDay.setText(getString(R.string.calendar_today));
    }

    protected void bindData() {
        resetTitleText();
    }

    private void resetTitleText() {
        Calendar calendar = Calendar.getInstance();
        if (slSchedule.getCurrentSelectYear() == calendar.get(Calendar.YEAR) &&
                slSchedule.getCurrentSelectDay() == calendar.get(Calendar.MONTH) &&
                slSchedule.getCurrentSelectDay() == calendar.get(Calendar.DAY_OF_MONTH)) {
            tvTitleMonth.setText(mMonthText[slSchedule.getCurrentSelectMonth()]);
            tvTitleDay.setText(getString(R.string.calendar_today));
        } else {
            if (slSchedule.getCurrentSelectYear() == calendar.get(Calendar.YEAR)) {
                tvTitleMonth.setText(mMonthText[slSchedule.getCurrentSelectMonth()]);
            } else {
                tvTitleMonth.setText(String.format("%s%s", String.format(getString(R.string.calendar_year), slSchedule.getCurrentSelectYear()),
                        mMonthText[slSchedule.getCurrentSelectMonth()]));
            }
            tvTitleDay.setText(String.format(getString(R.string.calendar_day), slSchedule.getCurrentSelectDay()));
        }
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
    }

    protected void initData() {
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            schedules.add(new Schedule());
        }
        mScheduleAdapter.changeAllData(schedules);
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        resetTitleText();
    }

}
