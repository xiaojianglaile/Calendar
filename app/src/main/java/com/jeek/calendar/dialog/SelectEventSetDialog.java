package com.jeek.calendar.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeek.calendar.R;
import com.jeek.calendar.activity.AddEventSetActivity;
import com.jeek.calendar.adapter.SelectEventSetAdapter;
import com.jimmy.common.bean.EventSet;
import com.jeek.calendar.task.eventset.LoadEventSetTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

import java.util.List;

/**
 * Created by Jimmy on 2016/10/15 0015.
 */
public class SelectEventSetDialog extends Dialog implements View.OnClickListener, OnTaskFinishedListener<List<EventSet>> {

    public static int ADD_EVENT_SET_CODE = 1;

    private Context mContext;
    private OnSelectEventSetListener mOnSelectEventSetListener;
    private int mId;

    private ListView lvEventSets;

    private SelectEventSetAdapter mSelectEventSetAdapter;
    private List<EventSet> mEventSets;

    public SelectEventSetDialog(Context context, OnSelectEventSetListener onSelectEventSetListener, int id) {
        super(context, R.style.DialogFullScreen);
        mContext = context;
        mOnSelectEventSetListener = onSelectEventSetListener;
        mId = id;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_select_event_set);
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvConfirm).setOnClickListener(this);
        findViewById(R.id.tvAddEventSet).setOnClickListener(this);
        lvEventSets = (ListView) findViewById(R.id.lvEventSets);
        initData();
    }

    private void initData() {
        new LoadEventSetTask(getContext(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        lvEventSets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectEventSetAdapter.setSelectPosition(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvConfirm:
                if (mOnSelectEventSetListener != null) {
                    mOnSelectEventSetListener.onSelectEventSet(mEventSets.get(mSelectEventSetAdapter.getSelectPosition()));
                }
                dismiss();
                break;
            case R.id.tvAddEventSet:
                ((Activity) mContext).startActivityForResult(new Intent(mContext, AddEventSetActivity.class), ADD_EVENT_SET_CODE);
                break;
        }
    }

    public void addEventSet(EventSet eventSet) {
        mEventSets.add(eventSet);
        mSelectEventSetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskFinished(List<EventSet> data) {
        mEventSets = data;
        EventSet eventSet = new EventSet();
        eventSet.setName(getContext().getString(R.string.menu_no_category));
        mEventSets.add(0, eventSet);
        int position = 0;
        for (int i = 0; i < mEventSets.size(); i++) {
            if (mEventSets.get(i).getId() == mId) {
                position = i;
                break;
            }
        }
        mSelectEventSetAdapter = new SelectEventSetAdapter(mContext, mEventSets, position);
        lvEventSets.setAdapter(mSelectEventSetAdapter);
    }

    public interface OnSelectEventSetListener {
        void onSelectEventSet(EventSet eventSet);
    }

}
