package com.jeek.calendar.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jeek.calendar.R;
import com.jimmy.common.bean.EventSet;
import com.jeek.calendar.dialog.SelectColorDialog;
import com.jeek.calendar.task.eventset.AddEventSetTask;
import com.jeek.calendar.utils.JeekUtils;
import com.jimmy.common.base.app.BaseActivity;
import com.jimmy.common.listener.OnTaskFinishedListener;
import com.jimmy.common.util.ToastUtils;

/**
 * Created by Jimmy on 2016/10/12 0012.
 */
public class AddEventSetActivity extends BaseActivity implements View.OnClickListener, OnTaskFinishedListener<EventSet>, SelectColorDialog.OnSelectColorListener {

    public static int ADD_EVENT_SET_CANCEL = 1;
    public static int ADD_EVENT_SET_FINISH = 2;
    public static String EVENT_SET_OBJ = "event.set.obj";

    private EditText etEventSetName;
    private View vEventSetColor;
    private SelectColorDialog mSelectColorDialog;

    private int mColor = 0;

    @Override
    protected void bindView() {
        setContentView(R.layout.activity_add_event_set);
        TextView tvTitle = searchViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.menu_add_event_set));
        etEventSetName = searchViewById(R.id.etEventSetName);
        vEventSetColor = searchViewById(R.id.vEventSetColor);
        searchViewById(R.id.tvCancel).setOnClickListener(this);
        searchViewById(R.id.tvFinish).setOnClickListener(this);
        searchViewById(R.id.rlEventSetColor).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                setResult(ADD_EVENT_SET_CANCEL);
                finish();
                break;
            case R.id.tvFinish:
                addEventSet();
                break;
            case R.id.rlEventSetColor:
                showSelectColorDialog();
                break;
        }
    }

    private void showSelectColorDialog() {
        if (mSelectColorDialog == null)
            mSelectColorDialog = new SelectColorDialog(this, this);
        mSelectColorDialog.show();
    }

    private void addEventSet() {
        String name = etEventSetName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShortToast(this, R.string.event_set_name_is_not_null);
        } else {
            EventSet eventSet = new EventSet();
            eventSet.setName(name);
            eventSet.setColor(mColor);
            new AddEventSetTask(this, this, eventSet).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onTaskFinished(EventSet data) {
        setResult(ADD_EVENT_SET_FINISH, new Intent().putExtra(EVENT_SET_OBJ, data));
        finish();
    }

    @Override
    public void onSelectColor(int color) {
        mColor = color;
        vEventSetColor.setBackgroundResource(JeekUtils.getEventSetCircle(color));
    }
}
