package com.jeek.calendar.task.schedule;

import android.content.Context;

import com.jimmy.common.bean.Schedule;
import com.jimmy.common.data.ScheduleDao;
import com.jimmy.common.base.task.BaseAsyncTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class AddScheduleTask extends BaseAsyncTask<Schedule> {

    private Schedule mSchedule;

    public AddScheduleTask(Context context, OnTaskFinishedListener<Schedule> onTaskFinishedListener, Schedule schedule) {
        super(context, onTaskFinishedListener);
        mSchedule = schedule;
    }

    @Override
    protected Schedule doInBackground(Void... params) {
        if (mSchedule != null) {
            ScheduleDao dao = ScheduleDao.getInstance(mContext);
            int id = dao.addSchedule(mSchedule);
            if (id != 0) {
                mSchedule.setId(id);
                return mSchedule;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
