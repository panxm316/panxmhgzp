package com.hgzp.advertising.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * DateUtils
 * 创建人：lhl
 * 类描述：日期工具类
 * 创建日期：2024/1/16 12:53
 */
public class DateUtils {
    //获取当天的开始时间
    public static java.util.Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    //获取当天的结束时间
    public static java.util.Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    //获取昨天的开始时间
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    //获取昨天的结束时间
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    //获取明天的开始时间
    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }
    //获取明天的结束时间
    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }
    //获取本周的结束时间
    public static Date getEndDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }
    //获取本月的开始时间
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }
    //获取本月的结束时间
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }
    //获取本年的开始时间
    public static java.util.Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    //获取去年的开始时间
    public static java.util.Date getBeginDayOfLastYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getlastYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    //获取本年的结束时间
    public static java.util.Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    //获取去年的结束时间
    public static java.util.Date getEndDayOfLastYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getlastYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }
    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    //获取去年是哪一年
    public static Integer getlastYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1)-1);
    }

    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }
    //两个日期相减得到的天数
    public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);

        int days = new Long(diff).intValue();

        return days;
    }
    //两个日期相减得到的毫秒数
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    //两个日期相减得到的分钟数
    public static long dateDiffMinute(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return (date2ms - date1ms)/1000/60;
    }

    //获取两个日期中的最大日期
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }
    //获取两个日期中的最小日期
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }
    //返回某月该季度的第一个月
    public static Date getFirstSeasonDate(Date date) {
        final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = SEASON[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }
    //返回某个日期下几天的日期
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }
    //返回某个日期前几天的日期
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }
    //获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int endYear,
                                   int endMonth, int k) {
        List list = new ArrayList();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));

            }
        } else {
            {
                for (int j = beginMonth; j < 12; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }

                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < 12; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }
    //获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
    public static List getTimeList(int beginYear, int beginMonth, int k) {
        List list = new ArrayList();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }

    // 获取某季度第一天
    // num 0:第1季度  2：第2季度 3：第3季度 4：第4季度
    public static Date  getQuarterFirstDay(int num)
    {
        Calendar cal = null;
        switch (num) {
            case 0:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,0);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
            case 1:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,3);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
            case 2:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,6);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
            case 3:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,9);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
        }
        return null;

    }

    // 获取某季度的最后一天
    // num 0:第1季度  2：第2季度 3：第3季度 4：第4季度
    public static Date  getQuarterLastDay(int num)
    {
        Calendar cal = null;
        switch (num) {
            case 0:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,3);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());
            case 1:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,6);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());
            case 2:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,9);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());
            case 3:
                cal = Calendar.getInstance();
                cal.set(Calendar.MONTH,12);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());

        }
        return null;

    }


    // 获取去年某季度第一天
    // num 0:第1季度  2：第2季度 3：第3季度 4：第4季度
    public static Date  getLastYearQuarterFirstDay(int num)
    {
        Calendar cal = null;
        switch (num) {
            case 0:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,0);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
            case 1:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,3);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
            case 2:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,6);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
            case 3:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,9);
                cal.set(Calendar.DATE,1);
                return  new Timestamp(cal.getTimeInMillis());
        }
        return null;

    }

    // 获取去年某季度的最后一天
    // num 0:第1季度  2：第2季度 3：第3季度 4：第4季度
    public static Date  getLastYearQuarterLastDay(int num)
    {
        Calendar cal = null;
        switch (num) {
            case 0:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,3);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());
            case 1:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,6);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());
            case 2:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,9);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());
            case 3:
                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, getlastYear());
                cal.set(Calendar.MONTH,12);
                cal.set(Calendar.DATE,1);
                cal.add(Calendar.DATE,-1);
                return  new Timestamp(cal.getTimeInMillis());

        }
        return null;

    }

    // Date实例对象添加num分钟，返回新Date实例对象
    // num 分钟
    public static Date  dateAddMINUTE(Date date,int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, num);
        return cal.getTime();
    }

    // 判断startDate是否在EndDate之后，若在之后，返回true，否则返回false
    public static boolean startDateAfterEndDate(Date startDate,Date endDate)
    {
        Calendar begin = Calendar.getInstance();
        begin.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        if (begin.after(end)) {
            return true;
        } else {
            return false;
        }
    }

    // 判断currentDate是否在startDate和EndDate之间，若在之间，返回true，否则返回false
    public static boolean currentDateMiddleStartDateAndEndDate(Date currentDate, Date startDate,Date endDate)
    {
        if(startDateAfterEndDate(currentDate,startDate) && startDateAfterEndDate(endDate,currentDate))
            return  true;
        return   false;
    }

    // 获取指定年指定月的开始天数和结束天数
    public static Map<String,Date> getFirstDayAndLastDayOfTheSpecifiedMonth(int year, int month) {
        // 获取当前分区的日历信息
        Calendar calendar = Calendar.getInstance();
        // 设置年
        calendar.set(Calendar.YEAR, year);
        // 设置月，月份从0开始
        calendar.set(Calendar.MONTH, month - 1);
        // 设置为指定月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        // 获取指定月第一天的时间
        Date start = calendar.getTime();
        // 设置日历天数为当前月实际天数的最大值，即指定月份的最后一天
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        // 获取最后一天的时间
        Date end = calendar.getTime();
        // 设置返回信息,返回样式根据需求自行格式化
        Map<String,Date> dateMap = new HashMap<>();
        dateMap.put("start",start);
        dateMap.put("end",end);
        return dateMap;
    }

    // 获取年份
    public static int getYear(Date date)
    {
        Calendar cal = Calendar.getInstance();
        if(null != cal)
            cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    // 获取月份
    public static int getMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        if(null != cal)
            cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    // 获取天数
    public static int getDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        if(null != cal)
            cal.setTime(date);
        int day = cal.get(Calendar.DATE);
        return day;
    }


    // 获取指定年指季度的开始日期和结束日期
    public static Map<String,Date> getFirstDayAndLastDayOfTheSpecifiedQuarter(int year, int quarter) {
        // 获取当前分区的日历信息
        Calendar calendar = Calendar.getInstance();
        // 设置年
        calendar.set(Calendar.YEAR, year);
        switch(quarter)
        {
            case 0: {
                // 设置月，月份从0开始
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DATE, 1);
                Date start = new Timestamp(calendar.getTimeInMillis());
                calendar.set(Calendar.MONTH, 3);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DATE, -1);
                Date end = new Timestamp(calendar.getTimeInMillis());
                Map<String, Date> dateMap = new HashMap<>();
                dateMap.put("start", start);
                dateMap.put("end", end);
                return dateMap;
            }
            case 1: {
                calendar.set(Calendar.MONTH, 3);
                calendar.set(Calendar.DATE, 1);
                Date start = new Timestamp(calendar.getTimeInMillis());
                calendar.set(Calendar.MONTH, 6);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DATE, -1);
                Date end = new Timestamp(calendar.getTimeInMillis());
                Map<String, Date> dateMap = new HashMap<>();
                dateMap.put("start", start);
                dateMap.put("end", end);
                return dateMap;
            }
            case 2: {
                calendar.set(Calendar.MONTH, 6);
                calendar.set(Calendar.DATE, 1);
                Date start = new Timestamp(calendar.getTimeInMillis());
                calendar.set(Calendar.MONTH, 9);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DATE, -1);
                Date end = new Timestamp(calendar.getTimeInMillis());
                Map<String, Date> dateMap = new HashMap<>();
                dateMap.put("start", start);
                dateMap.put("end", end);
                return dateMap;

            }
            case 3: {
                calendar.set(Calendar.MONTH, 9);
                calendar.set(Calendar.DATE, 1);
                Date start = new Timestamp(calendar.getTimeInMillis());
                calendar.set(Calendar.MONTH, 12);
                calendar.set(Calendar.DATE, 1);
                calendar.add(Calendar.DATE, -1);
                Date end = new Timestamp(calendar.getTimeInMillis());
                Map<String, Date> dateMap = new HashMap<>();
                dateMap.put("start", start);
                dateMap.put("end", end);
                return dateMap;
            }

        }
        return  null;
    }

    /**
     * 获取上个月月份
     * @return
     */
    public static Date getLastMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-1);
        // 设置为上一个月
        date = calendar.getTime();
        return date;
    }

    // 获取指定月份的天数
    public static int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static Date transferString2Date(String s) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
        } catch (ParseException e) {
            //LOGGER.error("时间转换错误, string = {}", s, e);
        }
        return date;
    }

    public static String transferDate2String(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将日期按照指定格式转换为字符串
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String transferDate2StringCn(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
        // 将日期按照指定格式转换为字符串
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String getBeginDayOfMonthWithYear(String year) {
        int nYear = getYearFromStr(year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(nYear, getNowMonth() - 1, 1);
        return transferDate2String(getDayStartTime(calendar.getTime()));
    }
    //获取本月的结束时间
    public static String getEndDayOfMonthWithYear(String year) {
        int nYear = getYearFromStr(year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(nYear, getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(nYear, getNowMonth() - 1, day);
        return transferDate2String(getDayEndTime(calendar.getTime()));
    }

    //获取指定年和月的开始时间
    public static String getStartDayMonthWithYear(int year,int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int day = 1;
        calendar.set(year, month, day);
        return transferDate2String(getDayEndTime(calendar.getTime()));
    }

    //获取指定年和月的结束时间
    public static String getDayMonthWithYear(int year,int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(year, month, day);
        return transferDate2String(getDayEndTime(calendar.getTime()));
    }

    //获取指定年份的开始时间
    public static String getBeginDayOfYear(String year) {
        int nYear = getYearFromStr(year);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, nYear);
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return transferDate2String(getDayStartTime(cal.getTime()));
    }

    //获取指定年份的结束时间
    public static String getEndDayOfYear(String year) {
        int nYear = getYearFromStr(year);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, nYear);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return transferDate2String(getDayEndTime(cal.getTime()));
    }
    public static int getYearFromStr(String year) {
        String str = year;
        String digit = "";
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                digit += c;
            }
        }
        if( !digit.isEmpty() && digit.length() == 4  ) {
            return Integer.parseInt(digit);
        }
        return getNowYear();
    }

    public static String getMonthToDay() {
        Date date = getBeginDayOfYear();
        int year = getYear(date);
        int month = getMonth(date);
        Date nowDate = new Date();
        int currentMonth = getMonth(nowDate);
        return String.format("%d年%d—%d月",year,month+1,currentMonth+1);
    }

    public static String getLastYearMonthToDay() {
        Date date = getBeginDayOfLastYear();
        int year = getYear(date);
        int month = getMonth(date);
        Date nowDate = new Date();
        int currentMonth = getMonth(nowDate);
        return String.format("%d年%d—%d月",year,month+1,currentMonth+1);
    }

    // 获取当前年度开始时间
    public static String getBeginDayOfThisyear() {
        Date  beginDate = getBeginDayOfYear();
        return transferDate2String(beginDate);
    }

    // 获取去年开始时间
    public static String getBeginDayOfLastyear() {
        Date  beginDate = getBeginDayOfLastYear();
        return transferDate2String(beginDate);
    }

    // 获取今年当前时间
    public static String getThisyearCurrentTime() {
        Date  beginDate = new Date();
        return transferDate2String(beginDate);
    }

    // 获取去年当前时间
    public static String getLastyearCurrentTime() {
        // 获取当前日期
        Calendar today = Calendar.getInstance();
        // 获取去年的今天
        today.add(Calendar.YEAR, -1);
        Date  beginDate = today.getTime();
        return transferDate2String(beginDate);
    }

}


