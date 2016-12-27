package com.jeek.calendar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jimmy on 2016/10/14 0014.
 */
public class DateUtils {

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param time   时间戳
     * @param format
     * @return
     */
    public static String timeStamp2Date(long time, String format) {
        if (time == 0) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date   字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
