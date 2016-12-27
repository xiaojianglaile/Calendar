#Calendar(觉得好用记得帮忙Star哦)

#注意事项：
*该Demo没有进行网络数据的联调，使用了本地数据库进行存储数据，有需要网络配置的可删除本地数据库相关代码，再去进行网络数据显示即可。<br/>
*该Demo的DragContainerLayout，即拖拽功能未实现，可忽略删除。

#交流QQ群：
小江Android交流群(259194144)，进群备注Android、Github之类的关键词即可。

#使用方法：
###MonthCalendarView的使用
        <com.jeek.calendar.widget.calendar.month.MonthCalendarView
                  android:id="@+id/mcvCalendar"
                  android:layout_width="match_parent"
                  android:layout_height="@dimen/small_month_calendar_height"
                    app:month_day_text_size="@integer/small_calendar_text_size"
                    app:month_selected_circle_color="@color/color_select_date_dialog_edit_text_bg_focus"
                    app:month_selected_circle_today_color="@color/color_select_date_dialog_edit_text_bg_focus"
                    app:month_show_task_hint="false" <!-是否显示圆点提示->
                    app:week_text_size="@integer/small_calendar_text_size"/>

###ScheduleLayout的使用
layout_schedule.xml文件，必须包含MonthCalendarView、WeekCalendarView和ScheduleRecyclerView，可以直接引用改文件作为布局。<br/>

###设置日期监听
        slSchedule.setOnCalendarClickListener(new OnCalendarClickListener() {
                    @Override
                    public void onClickDate(int year, int month, int day) {
                        //监听获得点击的年月日
                    }
                });

###跳转到今天
        slSchedule.getMonthCalendar().setTodayToView();

#效果图:
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_0.gif)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_1.png)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_2.png)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_3.png)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_4.png)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_5.png)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_6.png)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_7.png)