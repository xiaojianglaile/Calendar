package com.jeek.calendar.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeek.calendar.R;
import com.jimmy.common.bean.EventSet;
import com.jimmy.common.bean.Schedule;
import com.jeek.calendar.dialog.InputLocationDialog;
import com.jeek.calendar.dialog.SelectDateDialog;
import com.jeek.calendar.dialog.SelectEventSetDialog;
import com.jeek.calendar.task.eventset.LoadEventSetMapTask;
import com.jeek.calendar.task.schedule.UpdateScheduleTask;
import com.jeek.calendar.utils.DateUtils;
import com.jeek.calendar.utils.JeekUtils;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.listener.OnTaskFinishedListener;
import com.jimmy.common.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jimmy on 2016/10/15 0015.
 */
public class ScheduleDetailActivity extends BaseActivity implements View.OnClickListener, OnTaskFinishedListener<Map<Integer, EventSet>>, SelectDateDialog.OnSelectDateListener, InputLocationDialog.OnLocationBackListener, SelectEventSetDialog.OnSelectEventSetListener {

    public static int UPDATE_SCHEDULE_CANCEL = 1;
    public static int UPDATE_SCHEDULE_FINISH = 2;
    public static String SCHEDULE_OBJ = "schedule.obj";
    public static String CALENDAR_POSITION = "calendar.position";

    private View vScheduleColor;
    private EditText etScheduleTitle, etScheduleDesc;
    private ImageView ivScheduleEventSetIcon;
    private TextView tvScheduleEventSet, tvScheduleTime, tvScheduleLocation;
    private SelectEventSetDialog mSelectEventSetDialog;
    private SelectDateDialog mSelectDateDialog;
    private InputLocationDialog mInputLocationDialog;

    private Map<Integer, EventSet> mEventSetsMap;
    private Schedule mSchedule;
    private int mPosition = -1;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_schedule_detail);
        TextView tvTitle = searchViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.schedule_event_detail_setting));
        searchViewById(R.id.tvCancel).setOnClickListener(this);
        searchViewById(R.id.tvFinish).setOnClickListener(this);
        searchViewById(R.id.llScheduleEventSet).setOnClickListener(this);
        searchViewById(R.id.llScheduleTime).setOnClickListener(this);
        searchViewById(R.id.llScheduleLocation).setOnClickListener(this);
        vScheduleColor = searchViewById(R.id.vScheduleColor);
        ivScheduleEventSetIcon = searchViewById(R.id.ivScheduleEventSetIcon);
        etScheduleTitle = searchViewById(R.id.etScheduleTitle);
        etScheduleDesc = searchViewById(R.id.etScheduleDesc);
        tvScheduleEventSet = searchViewById(R.id.tvScheduleEventSet);
        tvScheduleTime = searchViewById(R.id.tvScheduleTime);
        tvScheduleLocation = searchViewById(R.id.tvScheduleLocation);
    }

    @Override
    protected void initData() {
        super.initData();
        mEventSetsMap = new HashMap<>();
        mSchedule = (Schedule) getIntent().getSerializableExtra(SCHEDULE_OBJ);
        mPosition = getIntent().getIntExtra(CALENDAR_POSITION, -1);
        new LoadEventSetMapTask(this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void bindData() {
        super.bindData();
        setScheduleData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                setResult(UPDATE_SCHEDULE_CANCEL);
                finish();
                break;
            case R.id.tvFinish:
                confirm();
                break;
            case R.id.llScheduleEventSet:
                showSelectEventSetDialog();
                break;
            case R.id.llScheduleTime:
                showSelectDateDialog();
                break;
            case R.id.llScheduleLocation:
                showInputLocationDialog();
                break;
        }
    }

    private void confirm() {
        if (etScheduleTitle.getText().length() != 0) {
            mSchedule.setTitle(etScheduleTitle.getText().toString());
            mSchedule.setDesc(etScheduleDesc.getText().toString());
            new UpdateScheduleTask(this, new OnTaskFinishedListener<Boolean>() {
                @Override
                public void onTaskFinished(Boolean data) {
                    setResult(UPDATE_SCHEDULE_FINISH);
                    finish();
                }
            }, mSchedule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            ToastUtils.showShortToast(this, R.string.schedule_input_content_is_no_null);
        }
    }

    private void showSelectEventSetDialog() {
        if (mSelectEventSetDialog == null) {
            mSelectEventSetDialog = new SelectEventSetDialog(this, this, mSchedule.getEventSetId());
        }
        mSelectEventSetDialog.show();
    }

    private void showSelectDateDialog() {
        if (mSelectDateDialog == null) {
            mSelectDateDialog = new SelectDateDialog(this, this, mSchedule.getYear(), mSchedule.getMonth(), mSchedule.getDay(), mPosition);
        }
        mSelectDateDialog.show();
    }

    private void showInputLocationDialog() {
        if (mInputLocationDialog == null) {
            mInputLocationDialog = new InputLocationDialog(this, this);
        }
        mInputLocationDialog.show();
    }

    private void setScheduleData() {
        vScheduleColor.setBackgroundResource(JeekUtils.getEventSetColor(mSchedule.getColor()));
        ivScheduleEventSetIcon.setImageResource(mSchedule.getEventSetId() == 0 ? R.mipmap.ic_detail_category : R.mipmap.ic_detail_icon);
        etScheduleTitle.setText(mSchedule.getTitle());
        etScheduleDesc.setText(mSchedule.getDesc());
        EventSet current = mEventSetsMap.get(mSchedule.getEventSetId());
        if (current != null) {
            tvScheduleEventSet.setText(current.getName());
        }
        resetDateTimeUi();
        if (TextUtils.isEmpty(mSchedule.getLocation())) {
            tvScheduleLocation.setText(R.string.click_here_select_location);
        } else {
            tvScheduleLocation.setText(mSchedule.getLocation());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectEventSetDialog.ADD_EVENT_SET_CODE) {
            if (resultCode == AddEventSetActivity.ADD_EVENT_SET_FINISH) {
                EventSet eventSet = (EventSet) data.getSerializableExtra(AddEventSetActivity.EVENT_SET_OBJ);
                if (eventSet != null) {
                    mSelectEventSetDialog.addEventSet(eventSet);
                    sendBroadcast(new Intent(MainActivity.ADD_EVENT_SET_ACTION).putExtra(AddEventSetActivity.EVENT_SET_OBJ, eventSet));
                }
            }
        }
    }

    @Override
    public void onTaskFinished(Map<Integer, EventSet> data) {
        mEventSetsMap = data;
        EventSet eventSet = new EventSet();
        eventSet.setName(getString(R.string.menu_no_category));
        mEventSetsMap.put(eventSet.getId(), eventSet);
        EventSet current = mEventSetsMap.get(mSchedule.getEventSetId());
        if (current != null) {
            tvScheduleEventSet.setText(current.getName());
        }
    }

    @Override
    public void onSelectDate(int year, int month, int day, long time, int position) {
        mSchedule.setYear(year);
        mSchedule.setMonth(month);
        mSchedule.setDay(day);
        mSchedule.setTime(time);
        mPosition = position;
        resetDateTimeUi();
    }

    private void resetDateTimeUi() {
        if (mSchedule.getTime() == 0) {
            if (mSchedule.getYear() != 0) {
                tvScheduleTime.setText(String.format(getString(R.string.date_format_no_time), mSchedule.getYear(), mSchedule.getMonth() + 1, mSchedule.getDay()));
            } else {
                tvScheduleTime.setText(R.string.click_here_select_date);
            }
        } else {
            tvScheduleTime.setText(DateUtils.timeStamp2Date(mSchedule.getTime(), getString(R.string.date_format)));
        }
    }

    @Override
    public void onLocationBack(String text) {
        mSchedule.setLocation(text);
        if (TextUtils.isEmpty(mSchedule.getLocation())) {
            tvScheduleLocation.setText(R.string.click_here_select_location);
        } else {
            tvScheduleLocation.setText(mSchedule.getLocation());
        }
    }

    @Override
    public void onSelectEventSet(EventSet eventSet) {
        mSchedule.setColor(eventSet.getColor());
        mSchedule.setEventSetId(eventSet.getId());
        vScheduleColor.setBackgroundResource(JeekUtils.getEventSetColor(mSchedule.getColor()));
        tvScheduleEventSet.setText(eventSet.getName());
        ivScheduleEventSetIcon.setImageResource(mSchedule.getEventSetId() == 0 ? R.mipmap.ic_detail_category : R.mipmap.ic_detail_icon);
    }
}
