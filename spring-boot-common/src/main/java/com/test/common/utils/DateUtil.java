package com.test.common.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    // ==格式到年==
    /**
     * 日期格式，年份，例如：2004，2008
     */
    public static final String DATE_FORMAT_YYYY = "yyyy";


    // ==格式到年月 ==
    /**
     * 日期格式，年份和月份，例如：200707，200808
     */
    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

    /**
     * 日期格式，年份和月份，例如：200707，2008-08
     */
    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";


    // ==格式到年月日==
    /**
     * 日期格式，年月日，例如：050630，080808
     */
    public static final String DATE_FORMAT_YYMMDD = "yyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：06-12-25，08-08-08
     */
    public static final String DATE_FORMAT_YY_MM_DD = "yy-MM-dd";

    /**
     * 日期格式，年月日，例如：20050630，20080808
     */
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    /**
     * 日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
     */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 日期格式，年月日，例如：2016.10.05
     */
    public static final String DATE_FORMAT_POINTYYYYMMDD = "yyyy.MM.dd";

    /**
     * 日期格式，年月日，例如：2016年10月05日
     */
    public static final String DATE_TIME_FORMAT_YYYY年MM月DD日 = "yyyy年MM月dd日";


    // ==格式到年月日 时分 ==

    /**
     * 日期格式，年月日时分，例如：200506301210，200808081210
     */
    public static final String DATE_FORMAT_YYYYMMDDHHmm = "yyyyMMddHHmm";

    /**
     * 日期格式，年月日时分，例如：20001230 12:00，20080808 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDD_HH_MI = "yyyyMMdd HH:mm";

    /**
     * 日期格式，年月日时分，例如：2000-12-30 12:00，2008-08-08 20:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";


    // ==格式到年月日 时分秒==
    /**
     * 日期格式，年月日时分秒，例如：20001230120000，20080808200808
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";

    /**
     * 日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开
     * 例如：2005-05-10 23：20：00，2008-08-08 20:08:08
     */
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";


    // ==格式到年月日 时分秒 毫秒==
    /**
     * 日期格式，年月日时分秒毫秒，例如：20001230120000123，20080808200808456
     */
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISSSSS = "yyyyMMddHHmmssSSS";


    // ==特殊格式==
    /**
     * 日期格式，月日时分，例如：10-05 12:00
     */
    public static final String DATE_FORMAT_MMDDHHMI = "MM-dd HH:mm";



    /**
     * 获取某日期的年份
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取某日期的月份
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某日期的天数，20211011 返回11
     * @param date
     * @return
     */
    public static Integer getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day=cal.get(Calendar.DATE);//获取日
        return day;
    }

    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurrentDate(String pattern) {
        return format(new Date(),pattern);
    }

    //日期格式转换
    public static String format(Date date, String pattern) {
       return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 日期字符串转Date
     * @param sDate
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String sDate, String format) throws ParseException {
        return FastDateFormat.getInstance(format).parse(sDate);
    }

    /**
     * 获取某个日期为星期几
     * @param date
     * @return String "星期*"
     */
    public static String getDayWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获得指定日期的星期几数 ,从星期天开始，星期天是1，依次类推
     * @param date
     * @return int
     */
    public static Integer getDayWeekOfDate2(Date date){
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(date);
        int weekDay = aCalendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }


    /**
     * 获得两个日期的相差多少天
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDiffDays(Date startDate,Date endDate){
        long between = Math.abs((endDate.getTime()-startDate.getTime())/1000);
        long day = between /60/60/24;
        return(long) Math.floor(day);
    }

    /**
     * 获得两个日期的相差多少分
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDiffMinutes(Date startDate,Date endDate){
        return  getDiffSeconds(startDate,endDate)/60;
    }

    /**
     * 获得两个日期的相差多少秒
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDiffSeconds(Date startDate,Date endDate){
        return  Math.abs((endDate.getTime()-startDate.getTime())/1000);
    }

    /**
     * 添加小时
     *
     * @param date 日期
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date addHour(Date date, int amount) {
        return add(date, Calendar.HOUR, amount);
    }

    /**
     * 添加分钟
     *
     * @param date 日期
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date addMinute(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    /**
     * 添加秒
     *
     * @param date 日期
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date addSecond(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    /**
     * 添加年份
     *
     * @param date 日期
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date addYear(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    /**
     * 添加月份
     *
     * @param date 日期
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date addMonth(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    /**
     * 添加日期
     *
     * @param date 日期
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date addDay(Date date, int amount) {
        return add(date, Calendar.DATE, amount);
    }


    /**
     * 日期添加
     *
     * @param date 日期
     * @param field 添加区域 {@link Calendar#DATE} {@link Calendar#MONTH} {@link Calendar#YEAR}
     * @param amount 数量
     *
     * @return 添加后的日期
     */
    public static Date add(Date date, int field, int amount) {
        Calendar CALENDAR = Calendar.getInstance();
        CALENDAR.setTime(date);
        CALENDAR.add(field, amount);
        return CALENDAR.getTime();
    }
}
