package com.xecoder.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by yanglu
 */
public class DateTools {


    public static String DATE_PATTEN = "yyyy-MM-dd";

    public static String DATE_PATTEN_MM = "yyyy-MM-dd HH:mm";

    public static String DATE_PATTEN_TM = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_PATTEN_TM_1 = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String DATE_PATTEN_ZH = "yyyy年MM月dd日";

    public static String DATETM_PATTEN_ZH = "yyyy年MM月dd日 HH点mm分ss秒";

    public static String TM_PATTEN_ZH = "HH点mm分ss秒";

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

    public static String formatDateTime(Date date) {
        String s = DateFormatUtils.format(date, DATE_PATTEN_TM);
        return s;
    }

    public static String format(Date date) {
        String s = DateFormatUtils.format(date, DATE_PATTEN);
        return s;
    }

    public static String formatZH(Date date) {
        String s = DateFormatUtils.format(date, DATE_PATTEN_ZH);
        return s;
    }

    public static String format(Date date, String patten) {
        String s = DateFormatUtils.format(date, patten);
        return s;
    }

    public static String format(long time, String patten) {
        String s = DateFormatUtils.format(new Date(time), patten);
        return s;
    }

    public static Date strToDate(String str) {
        try {
            Date date = DateUtils.parseDate(str, new String[]{DATE_PATTEN, DATE_PATTEN_MM, DATE_PATTEN_ZH, DATE_PATTEN_TM,
                    DATE_PATTEN_TM_1});
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date getDateTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date strToDate(String str, String pattern) {
        try {
            Date date = DateUtils.parseDate(str, new String[]{pattern});
            return date;
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date getDayStart(Date date) {
        return getDayStart(date, 0);
    }

    public static Date getDayStart(Date date, int daysLater) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(date);
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        tempCal.set(Calendar.MILLISECOND, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return tempCal.getTime();
    }

    public static Date getDayEnd(Date date) {
        return getDayEnd(date, 0);
    }

    public static Date getDayEnd(Date date, int daysLater) {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(date);
        tempCal.set(tempCal.get(Calendar.YEAR), tempCal.get(Calendar.MONTH), tempCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        tempCal.set(Calendar.MILLISECOND, 0);
        tempCal.add(Calendar.DAY_OF_MONTH, daysLater);
        return tempCal.getTime();
    }

    public static Date intToDateDaily(int dailyInt) {
        String daily = String.valueOf(dailyInt);
        try {
            return DateUtils.parseDate(daily, new String[]{"yyyyMMdd"});
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date intToDateMonth(int dailyInt) {
        String moth = String.valueOf(dailyInt);
        try {
            return DateUtils.parseDate(moth, new String[]{"yyyyMM"});
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String fmtIntDaily(int dailyInt) {
        String daily = String.valueOf(dailyInt);
        return fmtDaily(daily);
    }

    public static String fmtDaily(String daily) {
        if (StringUtils.isEmpty(daily)) {
            return StringUtils.EMPTY;
        }
        return String.format("%s-%s-%s", daily.substring(0, 4), daily.substring(4, 6), daily.substring(6));
    }

    public static int toIntDaily(Date date) {
        String fmt = format(date, "yyyyMMdd");
        return NumberUtils.toInt(fmt);
    }

    public static int toIntMonth(Date date) {
        String fmt = format(date, "yyyyMM");
        return NumberUtils.toInt(fmt);
    }

    public static Date localDate(Date date,int i)
    {
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(date);
        tempCal.add(tempCal.DATE,i);
        return tempCal.getTime();
    }
}
