package com.base.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static long getTimeExamined(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(time);
            long dateTime = date.getTime();
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeVip(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return formatter.format(date);

    }

    public static String getTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return formatter.format(date);

    }

    public static String getTimeEnforcement(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatter.format(date);

    }

    public static String getTimeSongTest(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
        return formatter.format(date);

    }

    public static String getTimeSongRecord(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年 MM月dd日 HH:mm", Locale.getDefault());
        return formatter.format(date);

    }

    public static long getTimeDogAge(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = dateFormat.parse(time);
            long dateTime = date.getTime();
            return dateTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMessageTime(long time) {
        DateFormat oldDay = new SimpleDateFormat("dd");
        DateFormat newDay = new SimpleDateFormat("dd");
        DateFormat dfHour = new SimpleDateFormat("a h:mm");
        DateFormat dfDay = new SimpleDateFormat("MM月dd日");
        try {
            Date cur = new Date();
            Date chatTime = new Date(time);
            long diff = cur.getTime() - chatTime.getTime();
            if (diff / (1000 * 60 * 60 * 24) >= 1) {
                return dfDay.format(chatTime);
            } else {
                if (Integer.parseInt(oldDay.format(chatTime)) < Integer.parseInt(newDay.format(new Date()))) {
                    return dfDay.format(chatTime);
                } else {
                    return dfHour.format(chatTime);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return dfHour.format(new Date());
        }
    }

    public static String getTimeWord(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日", Locale.getDefault());
        return formatter.format(date);

    }

    public static String getTimeToHour(long time) {
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日 H时");
        Date chatTime = new Date(time);
        return dateFormat.format(chatTime);

    }

    public static String getTimeToDay(long time) {
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        Date chatTime = new Date(time);
        return dateFormat.format(chatTime);

    }

    /**
     * 时间格式化
     *
     * @param time 时间
     */
    public static String getTimeActivity(long time) {
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
//        SimpleDateFormat sdfone = new SimpleDateFormat("HH:mm");
        if (time >= todayStartMillis) {
            return "今天";
        }
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (time >= yesterdayStartMilis) {
            return "昨天";
        }
        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
        if (time >= yesterdayBeforeStartMilis) {
            return "前天";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(time));
    }

}
