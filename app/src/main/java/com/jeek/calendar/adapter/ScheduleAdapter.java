package com.jeek.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeek.calendar.R;
import com.jeek.calendar.bean.Schedule;

import java.util.List;

/**
 * Created by Jimmy on 2016/10/8 0008.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private Context mContext;
    private List<Schedule> mSchedules;

    public ScheduleAdapter(Context context, List<Schedule> schedules) {
        mContext = context;
        mSchedules = schedules;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }

    protected class ScheduleViewHolder extends RecyclerView.ViewHolder {

        public ScheduleViewHolder(View itemView) {
            super(itemView);
        }

    }

    public void changeAllData(List<Schedule> schedules) {
        mSchedules.clear();
        mSchedules.addAll(schedules);
        notifyDataSetChanged();
    }

}
