package com.jeek.calendar.task.eventset;

import android.content.Context;

import com.jimmy.common.bean.Schedule;
import com.jimmy.common.data.ScheduleDao;
import com.jimmy.common.base.task.BaseAsyncTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

import java.util.List;

/**
 * Created by Jimmy on 2016/10/13 0013.
 */
public class GetScheduleTask extends BaseAsyncTask<List<Schedule>> {

    private int mId;

    public GetScheduleTask(Context context, OnTaskFinishedListener<List<Schedule>> onTaskFinishedListener, int id) {
        super(context, onTaskFinishedListener);
        mId = id;
    }

    @Override
    protected List<Schedule> doInBackground(Void... params) {
        ScheduleDao dao = ScheduleDao.getInstance(mContext);
        return dao.getScheduleByEventSetId(mId);
    }

}
