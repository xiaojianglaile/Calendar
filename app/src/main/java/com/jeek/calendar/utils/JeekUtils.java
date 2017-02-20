package com.jeek.calendar.utils;

import com.jeek.calendar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jimmy on 2016/10/10 0010.
 */
public class JeekUtils {

    public static String timeStamp2Time(long time) {
        return new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(time));
    }

    public static int getEventSetColor(int color) {
        switch (color) {
            case 0:
                return R.color.holiday_text_color;
            case 1:
                return R.color.color_schedule_blue;
            case 2:
                return R.color.color_schedule_green;
            case 3:
                return R.color.color_schedule_pink;
            case 4:
                return R.color.color_schedule_orange;
            case 5:
                return R.color.color_schedule_yellow;
            default:
                return R.color.holiday_text_color;
        }
    }

    public static int getEventSetCircle(int color) {
        switch (color) {
            case 0:
                return R.drawable.purple_circle;
            case 1:
                return R.drawable.blue_circle;
            case 2:
                return R.drawable.green_circle;
            case 3:
                return R.drawable.pink_circle;
            case 4:
                return R.drawable.orange_circle;
            case 5:
                return R.drawable.yellow_circle;
            default:
                return R.drawable.purple_circle;
        }
    }

    public static int getScheduleBlockView(int color) {
        switch (color) {
            case 0:
                return R.drawable.purple_schedule_left_block;
            case 1:
                return R.drawable.blue_schedule_left_block;
            case 2:
                return R.drawable.green_schedule_left_block;
            case 3:
                return R.drawable.pink_schedule_left_block;
            case 4:
                return R.drawable.orange_schedule_left_block;
            case 5:
                return R.drawable.yellow_schedule_left_block;
            default:
                return R.drawable.purple_schedule_left_block;
        }
    }

}
