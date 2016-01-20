package com.xecoder.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by yanglu
 */
public class DateUtils {

    public static String DATE_PATTEN_TM = "yyyy-MM-dd HH:mm:ss";
    /**
     * 获取所给时间的当晚午夜时间。
     * @param time 时间，毫秒数级
     * @return 午夜时间毫秒数
     */
    public static long getMidNightMS(long time) {
        Calendar midNight = Calendar.getInstance();
        midNight.setTimeInMillis(time);
        midNight.add(Calendar.DATE, 1);
        midNight.set(Calendar.HOUR_OF_DAY, 0);
        midNight.set(Calendar.SECOND, 0);
        midNight.set(Calendar.MINUTE, 0);
        midNight.set(Calendar.MILLISECOND, 0);
        return midNight.getTimeInMillis();
    }

    /**
     * 比较两个时间相差天数
     * @param today
     * @param time
     * @return
     */
    public static long compareDays(long today, long time) {
        today = getTodayMS(today);
        time = getTodayMS(time);
        return  (today - time)/(24*3600*1000);

    }


    /**
     * 比较两个时间相差秒
     * @param today
     * @param time
     * @return
     */
    public static long compareSeconds(long today, long time) {
        return  (today - time)/(1000);
    }

    public static Date addDay(Date date , int day)
    {
        Calendar calendar   =   new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, day);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime();
        return date;
    }



    /**
     * 获取当天00:00时间
     * @param time  时间，毫秒数级
     * @return      时间毫秒数
     */
    public static long getTodayMS(long time) {
        return getTodayDate(time).getTime();
    }

    public static long getTodayMidNightMS(){
        return getMidNightMS(System.currentTimeMillis());
    }

    public static Date getTodayDate(long time) {
        Calendar midNight = Calendar.getInstance();
        midNight.set(Calendar.HOUR_OF_DAY, 0);
        midNight.set(Calendar.SECOND, 0);
        midNight.set(Calendar.MINUTE, 0);
        midNight.set(Calendar.MILLISECOND, 0);
        return midNight.getTime();
    }

    public static String getTodayDate() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

    public static String getNowTimeString() {
        DateFormat format = new SimpleDateFormat("hh:mm");
        return format.format(new Date());
    }

    public static long stringToTime(String time) {
        DateFormat format = new SimpleDateFormat("hh:mm");
        try {
            return format.parse(time).getTime();
        } catch (Exception e) {
            return 0;
        }

    }
}
