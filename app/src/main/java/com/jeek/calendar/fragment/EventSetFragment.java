package com.jeek.calendar.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.jeek.calendar.adapter.ScheduleAdapter;
import com.jimmy.common.bean.EventSet;
import com.jimmy.common.bean.Schedule;
import com.jeek.calendar.dialog.SelectDateDialog;
import com.jeek.calendar.task.eventset.GetScheduleTask;
import com.jeek.calendar.task.schedule.AddScheduleTask;
import com.jeek.calendar.widget.calendar.schedule.ScheduleRecyclerView;
import com.jimmy.common.base.app.BaseFragment;
import com.jimmy.common.listener.OnTaskFinishedListener;
import com.jimmy.common.util.DeviceUtils;
import com.jimmy.common.util.ToastUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/12 0012.
 */
public class EventSetFragment extends BaseFragment implements View.OnClickListener, OnTaskFinishedListener<List<Schedule>>, SelectDateDialog.OnSelectDateListener {

    public static String EVENT_SET_OBJ = "event.set.obj";

    private ScheduleRecyclerView rvScheduleList;
    private EditText etInputContent;
    private RelativeLayout rlNoTask;
    private SelectDateDialog mSelectDateDialog;

    private ScheduleAdapter mScheduleAdapter;
    private EventSet mEventSet;

    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private int mPosition = -1;
    private long mTime;

    public static EventSetFragment getInstance(EventSet eventSet) {
        EventSetFragment fragment = new EventSetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_SET_OBJ, eventSet);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_event_set, container, false);
    }

    @Override
    protected void bindView() {
        rvScheduleList = searchViewById(R.id.rvScheduleList);
        rlNoTask = searchViewById(R.id.rlNoTask);
        etInputContent = searchViewById(R.id.etInputContent);
        searchViewById(R.id.ibMainClock).setOnClickListener(this);
        searchViewById(R.id.ibMainOk).setOnClickListener(this);
        initBottomInputBar();
        initScheduleList();
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
    protected void initData() {
        super.initData();
        mEventSet = (EventSet) getArguments().getSerializable(EVENT_SET_OBJ);
    }

    @Override
    protected void bindData() {
        super.bindData();
        new GetScheduleTask(mActivity, this, mEventSet.getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initScheduleList() {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScheduleList.setLayoutManager(manager);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        rvScheduleList.setItemAnimator(itemAnimator);
        mScheduleAdapter = new ScheduleAdapter(mActivity, this);
        rvScheduleList.setAdapter(mScheduleAdapter);
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
        if (mSelectDateDialog == null) {
            Calendar calendar = Calendar.getInstance();
            mSelectDateDialog = new SelectDateDialog(mActivity, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mPosition);
        }
        mSelectDateDialog.show();
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
            schedule.setColor(mEventSet.getColor());
            schedule.setEventSetId(mEventSet.getId());
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
                        rlNoTask.setVisibility(View.GONE);
                        mTime = 0;
                    }
                }
            }, schedule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
    }

    @Override
    public void onTaskFinished(List<Schedule> data) {
        mScheduleAdapter.changeAllData(data);
        rlNoTask.setVisibility(data.size() == 0 ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onSelectDate(int year, int month, int day, long time, int position) {
        setCurrentSelectDate(year, month, day);
        mTime = time;
        mPosition = position;
    }
}
