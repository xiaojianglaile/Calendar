package com.jeek.calendar.task.eventset;

import android.content.Context;

import com.jimmy.common.bean.EventSet;
import com.jimmy.common.data.EventSetDao;
import com.jimmy.common.base.task.BaseAsyncTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class AddEventSetTask extends BaseAsyncTask<EventSet> {

    private EventSet mEventSet;

    public AddEventSetTask(Context context, OnTaskFinishedListener<EventSet> onTaskFinishedListener, EventSet eventSet) {
        super(context, onTaskFinishedListener);
        mEventSet = eventSet;
    }

    @Override
    protected EventSet doInBackground(Void... params) {
        if (mEventSet != null) {
            EventSetDao dao = EventSetDao.getInstance(mContext);
            int id = dao.addEventSet(mEventSet);
            if (id != 0) {
                mEventSet.setId(id);
                return mEventSet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
