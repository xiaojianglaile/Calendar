package com.jeek.calendar.task.schedule;

import android.content.Context;

import com.jimmy.common.data.ScheduleDao;
import com.jimmy.common.base.task.BaseAsyncTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class RemoveScheduleTask extends BaseAsyncTask<Boolean> {

    private long mId;

    public RemoveScheduleTask(Context context, OnTaskFinishedListener<Boolean> onTaskFinishedListener, long id) {
        super(context, onTaskFinishedListener);
        mId = id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ScheduleDao dao = ScheduleDao.getInstance(mContext);
        return dao.removeSchedule(mId);
    }
}
