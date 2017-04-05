Calendar(觉得好用记得帮忙Star哦)
===
注意事项：
---
该Demo没有进行网络数据的联调，使用了本地数据库进行存储数据，有需要网络配置的可删除本地数据库相关代码，再去进行网络数据显示即可。<br/>

交流QQ群：
---
小江Android交流群(259194144)，进群备注Android、Github之类的关键词即可。

使用方法：
---
* MonthCalendarView的使用<br/>
```
<com.jeek.calendar.widget.calendar.month.MonthCalendarView
    android:id="@+id/mcvCalendar"
    android:layout_width="match_parent"
    android:layout_height="@dimen/small_month_calendar_height"
    app:month_day_text_size="@integer/small_calendar_text_size"
    app:month_selected_circle_color="@color/color_select_date_dialog_edit_text_bg_focus"
    app:month_selected_circle_today_color="@color/color_select_date_dialog_edit_text_bg_focus"
    app:month_show_lunar="true"
    app:month_show_task_hint="false"
    app:month_show_holiday_hint="true"
    app:month_text_size="@integer/small_calendar_text_size"/>
```

* ScheduleLayout的使用<br/>

layout_schedule.xml文件，必须包含MonthCalendarView、WeekCalendarView和ScheduleRecyclerView，可以直接引用改文件作为布局。<br/>

```
ScheduleLayout：
app:default_view="week" <!-默认周视图->
app:default_view="month" <!-默认月视图->
app:auto_change_month_row="false" <!-不自动改变五六行->
app:auto_change_month_row="true" <!-自动改变五六行->
```

* 设置日期监听<br/>
```
slSchedule.setOnCalendarClickListener(new OnCalendarClickListener() {
    @Override
    public void onClickDate(int year, int month, int day) {
        //监听获得点击的年月日
    }
});
```
* 跳转到今天<br/>
```
slSchedule.getMonthCalendar().setTodayToView();
```
* 跳转到某一天<br/>
```
slSchedule.initData(year, month, day);
```
* 缺点<br/>

该日历暂时没有支持无限循环，需要查看很多日期的朋友可以在MonthAdapter和WeekAdapter调高SIZE的大小即可。

效果图:<br/>
---
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_1.gif)
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_2.png)
