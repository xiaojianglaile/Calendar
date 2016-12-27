#Calendar

#注意事项：
*该Demo没有进行网络数据的联调，使用了本地数据库进行存储数据，有需要网络配置的可删除本地数据库相关代码，再去进行网络数据显示即可。
*该Demo的DragContainerLayout，即拖拽功能未实现，可忽略删除

#使用方法：
slSchedule为ScheduleLayout
slSchedule.setOnCalendarClickListener(new OnCalendarClickListener() {
            @Override
            public void onClickDate(int year, int month, int day) {
                //监听获得点击的年月日
            }
        });

#效果图:
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_1.png)
\n
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_2.png)
\n
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_3.png)
\n
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_4.png)
\n
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_5.png)
\n
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_6.png)
\n
![image](https://github.com/xiaojianglaile/Calendar/blob/master/raw/jeek_image_7.png)
\n