package com.jeek.calendar.fragment;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jeek.calendar.R;
import com.jeek.calendar.activity.MainActivity;
import com.jeek.calendar.adapter.ScheduleAdapter;
import com.jimmy.common.bean.Schedule;
import com.jeek.calendar.dialog.SelectDateDialog;
import com.jeek.calendar.task.schedule.AddScheduleTask;
import com.jeek.calendar.task.schedule.LoadScheduleTask;
import com.jeek.calendar.widget.calendar.OnCalendarClickListener;
import com.jeek.calendar.widget.calendar.schedule.ScheduleLayout;
import com.jeek.calendar.widget.calendar.schedule.ScheduleRecyclerView;
import com.jimmy.common.base.app.BaseFragment;
import com.jimmy.common.listener.OnTaskFinishedListener;
import com.jimmy.common.util.DeviceUtils;
import com.jimmy.common.util.ToastUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class ScheduleFragment extends BaseFragment implements OnCalendarClickListener, View.OnClickListener,
        OnTaskFinishedListener<List<Schedule>>, SelectDateDialog.OnSelectDateListener {

    private ScheduleLayout slSchedule;
    private ScheduleRecyclerView rvScheduleList;
    private EditText etInputContent;
    private RelativeLayout rLNoTask;

    private ScheduleAdapter mScheduleAdapter;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private long mTime;

    public static ScheduleFragment getInstance() {
        return new ScheduleFragment();
    }

    @Nullable
    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    protected void bindView() {
        slSchedule = searchViewById(R.id.slSchedule);
        etInputContent = searchViewById(R.id.etInputContent);
        rLNoTask = searchViewById(R.id.rlNoTask);
        slSchedule.setOnCalendarClickListener(this);
        searchViewById(R.id.ibMainClock).setOnClickListener(this);
        searchViewById(R.id.ibMainOk).setOnClickListener(this);
        initScheduleList();
        initBottomInputBar();
    }

    @Override
    protected void initData() {
        super.initData();
        initDate();
    }

    @Override
    protected void bindData() {
        super.bindData();
        resetScheduleList();
    }

    public void resetScheduleList() {
        new LoadScheduleTask(mActivity, this, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        setCurrentSelectDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        setCurrentSelectDate(year, month, day);
        resetScheduleList();
    }

    @Override
    public void onPageChange(int year, int month, int day) {

    }

    private void initScheduleList() {
        rvScheduleList = slSchedule.getSchedulerRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);
        mScheduleAdapter = new ScheduleAdapter(mActivity, this);
        rvScheduleList.setAdapter(mScheduleAdapter);
    }

    private void initBottomInputBar() {
        etInputContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etInputContent.setGravity(s.length() == 0 ? Gravity.CENTER : Gravity.CENTER_VERTICAL);
            }
        });
        etInputContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibMainClock:
                showSelectDateDialog();
                break;
            case R.id.ibMainOk:
                addSchedule();
                break;
        }
    }

    private void showSelectDateDialog() {
        new SelectDateDialog(mActivity, this, mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay, slSchedule.getMonthCalendar().getCurrentItem()).show();
    }

    private void closeSoftInput() {
        etInputContent.clearFocus();
        DeviceUtils.closeSoftInput(mActivity, etInputContent);
    }

    private void addSchedule() {
        String content = etInputContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShortToast(mActivity, R.string.schedule_input_content_is_no_null);
        } else {
            closeSoftInput();
            Schedule schedule = new Schedule();
            schedule.setTitle(content);
            schedule.setState(0);
            schedule.setTime(mTime);
            schedule.setYear(mCurrentSelectYear);
            schedule.setMonth(mCurrentSelectMonth);
            schedule.setDay(mCurrentSelectDay);
            new AddScheduleTask(mActivity, new OnTaskFinishedListener<Schedule>() {
                @Override
                public void onTaskFinished(Schedule data) {
                    if (data != null) {
                        mScheduleAdapter.insertItem(data);
                        etInputContent.getText().clear();
                        rLNoTask.setVisibility(View.GONE);
                        mTime = 0;
                        updateTaskHintUi(mScheduleAdapter.getItemCount() - 2);
                    }
                }
            }, schedule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
        if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).resetMainTitleDate(year, month, day);
        }
    }

    @Override
    public void onTaskFinished(List<Schedule> data) {
        mScheduleAdapter.changeAllData(data);
        rLNoTask.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
        updateTaskHintUi(data.size());
    }

    private void updateTaskHintUi(int size) {
        if (size == 0) {
            slSchedule.removeTaskHint(mCurrentSelectDay);
        } else {
            slSchedule.addTaskHint(mCurrentSelectDay);
        }
    }

    @Override
    public void onSelectDate(final int year, final int month, final int day, long time, int position) {
        slSchedule.getMonthCalendar().setCurrentItem(position);
        slSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
                slSchedule.getMonthCalendar().getCurrentMonthView().clickThisMonth(year, month, day);
            }
        }, 100);
        mTime = time;
    }

    public int getCurrentCalendarPosition() {
        return slSchedule.getMonthCalendar().getCurrentItem();
    }

}
