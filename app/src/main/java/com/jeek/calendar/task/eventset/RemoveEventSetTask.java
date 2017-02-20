package com.jeek.calendar.task.eventset;

import android.content.Context;

import com.jimmy.common.data.EventSetDao;
import com.jimmy.common.data.ScheduleDao;
import com.jimmy.common.base.task.BaseAsyncTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class RemoveEventSetTask extends BaseAsyncTask<Boolean> {

    private int mId;

    public RemoveEventSetTask(Context context, OnTaskFinishedListener<Boolean> onTaskFinishedListener, int id) {
        super(context, onTaskFinishedListener);
        mId = id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ScheduleDao scheduleDao = ScheduleDao.getInstance(mContext);
        scheduleDao.removeScheduleByEventSetId(mId);
        EventSetDao dao = EventSetDao.getInstance(mContext);
        return dao.removeEventSet(mId);
    }
}
