package com.jeek.calendar.task.eventset;

import android.content.Context;

import com.jimmy.common.bean.EventSet;
import com.jimmy.common.data.EventSetDao;
import com.jimmy.common.base.task.BaseAsyncTask;
import com.jimmy.common.listener.OnTaskFinishedListener;

import java.util.List;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class LoadEventSetTask extends BaseAsyncTask<List<EventSet>> {

    private Context mContext;

    public LoadEventSetTask(Context context, OnTaskFinishedListener<List<EventSet>> onTaskFinishedListener) {
        super(context, onTaskFinishedListener);
        mContext = context;
    }

    @Override
    protected List<EventSet> doInBackground(Void... params) {
        EventSetDao dao = EventSetDao.getInstance(mContext);
        return dao.getAllEventSet();
    }

}
