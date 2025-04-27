package com.dofun.uggame.common.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：日期处理
 */
@Slf4j
public class DateUtils {

    /**
     * 年-月-日
     */
    public static final String DATE_FORMAT_DATEONLY = "yyyy-MM-dd";

    /**
     * 年-月-日
     */
    public static final String DATE_FORMAT_DATEONLY_MONTH = "yyyyMM";
    /**
     * 年-月-日 时分秒
     */
    public static final String DATETIME_FORMAT_DATEONLY = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年-月-日 时分
     */
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd HH:mm";

    /**
     * 年-月-日 时分
     */
    public static final String DATE_FORMAT_DATE_DAY = "yyyy-MM-dd";

    /**
     * dateTime formatter
     */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATEONLY);
    /**
     * date formatter
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY);

    private static final ZoneId ZONE = ZoneId.systemDefault();

    /**
     * 将字符串转换成日期，只到年月日
     *
     * @param str
     * @return
     */
    public static LocalDateTime strToLocalDateTime(String str) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY);
            LocalDateTime time = LocalDateTime.from(LocalDate.parse(str, formatter).atStartOfDay());
            return time;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串转换成日期，只到年月日
     *
     * @param str
     * @return
     */
    public static Date strToDate(String str, String format) {
        try {
            if (StringUtils.isBlank(format)) {
                format = DATETIME_FORMAT_DATEONLY;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 取得两个日期之间相差的年数
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return
     */
    public static long getYearsBetween(LocalDate t1, LocalDate t2) {
        return t1.until(t2, ChronoUnit.YEARS);
    }

    /**
     * 取得两个日期之间相差的月数
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return
     */
    public static long getMonthsBetween(LocalDate t1, LocalDate t2) {
        return t1.until(t2, ChronoUnit.MONTHS);
    }

    /**
     * 取得两个日期之间相差的日数
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return
     */
    public static long getDaysBetween(LocalDate t1, LocalDate t2) {
        return t1.until(t2, ChronoUnit.DAYS);
    }

    /**
     * 比较两个日期的大小
     *
     * @param str1 YYYY-MM-DD
     * @param str2 YYYY-MM-DD
     * @return 1 表示str1比str2 大，0 表示str1等于str2 大,-1 表示str1比str2 小
     */
    public static int getComparisonSize(String str1, String str2) {
        LocalDate v = strToLocalDateTime(str1).toLocalDate();
        LocalDate v1 = strToLocalDateTime(str2).toLocalDate();
        return v.compareTo(v1);
    }

    /**
     * 获取当前日期 yyyy-MM-dd
     *
     * @return
     */
    public static String getDateNow() {
        LocalDate localDate = LocalDate.now();
        return localDate.toString();
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateNowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATEONLY));
    }

    /**
     * 获取当前时间 yyyyMMdd
     *
     * @return
     */
    public static String getDateNowShort() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 获取当前时间 yyyyMM
     *
     * @return
     */
    public static String getDateNowOnlyYm() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    /**
     * 获取昨天日期
     *
     * @return
     */
    public static String getDateLast() {
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(1).toString();
    }

    public static String getDateTimeLast() {
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
    }

    /**
     * 获取几天前日期
     *
     * @return
     */
    public static String getDateSomedaysAgo(Integer days) {
        LocalDate localDate = LocalDate.now();
        return localDate.minusDays(days).toString();
    }

    /**
     * 得到startDate 时间加一年减一天，刚好在投保要求的一年整
     *
     * @param startDate
     * @return
     */
    public static String getDateNowNextYear(String startDate) {
        LocalDateTime time = strToLocalDateTime(startDate);
        if (time == null) {
            return null;
        }
        LocalDate date = time.toLocalDate().plusYears(1).minusDays(1);
        return date.toString();
    }

    /**
     * 得到startDate 时间加一年，得到下一次投保起期
     *
     * @param startDate
     * @return
     */
    public static String getNextInsureInitDate(String startDate) {
        LocalDateTime time = strToLocalDateTime(startDate);
        if (time == null) {
            return null;
        }
        String date = DATETIME_FORMATTER.format(time.plusYears(1));
        return date;
    }

    /**
     * 得到startDate 时间减一月加一天
     *
     * @param startDate
     * @return
     */
    public static String getDateNowLastMonth(String startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATEONLY);
        LocalDateTime time = LocalDateTime.from(LocalDate.parse(startDate, formatter).atStartOfDay());
        if (time == null) {
            return null;
        }
        time = time.minusMonths(1).plusDays(1);
        return formatter.format(time);
    }

    // ***************************LocalDate LocalDateTime Date之间互转*******************************

    /**
     * java.time.LocalDateTime --> java.util.Date
     *
     * @param localDateTime
     * @return
     */
    public static Date date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * java.time.LocalDate --> java.util.Date
     *
     * @param localDate
     * @return
     */
    public static Date date(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * java.util.Date --> java.time.LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime localDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    /**
     * java.util.Date --> java.time.LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate localDate(Date date) {
        if (date == null) {
            return null;
        }
        return localDateTime(date).toLocalDate();
    }

    /**
     * 将Date日期转换成String
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_DATEONLY);
        String dateString = formatter.format(dateDate);
        return dateString;
    }


    public static String getLastMonthCurrentDate(LocalDate now) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(now.toString());
        Calendar c = Calendar.getInstance();
        //设置为指定日期
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        //获取最终的时间
        Date lastDateOfPrevMonth = c.getTime();

        return DateUtils.dateToStr(lastDateOfPrevMonth);
    }

    /**
     * 将Date日期转换成String
     *
     * @param dateDate
     * @return
     */
    public static String dateToStr(Date dateDate, String formatter) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
        String dateString = simpleDateFormat.format(dateDate);
        return dateString;
    }

    /**
     * 比较 time1 time2大小
     *
     * @param time1
     * @param time2
     * @return
     */
    public static int compareTime(String time1, String time2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATEONLY);
        if (time1 == null && time2 == null) {
            return 0;
        }
        if (time1 == null) {
            return -1;
        }
        if (time2 == null) {
            return 1;
        }
        LocalDateTime timeOne = LocalDateTime.parse(time1, formatter);
        LocalDateTime timeTwo = LocalDateTime.parse(time2, formatter);
        return timeOne.compareTo(timeTwo);

    }

    /**
     * 比较两个日期大小
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int compareDate(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY);
        LocalDate dateOne = LocalDate.parse(date1, formatter);
        LocalDate dateTwo = LocalDate.parse(date2, formatter);
        if (dateOne == null || dateTwo == null) {
            throw new RuntimeException("日期格式错误");
        }
        return dateOne.compareTo(dateTwo);

    }

    //long形式的time处理  add by qiuwei
    public static long asUnixTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }


    public static LocalDateTime asLocalDateTime(long unixTime) {
        return Instant.ofEpochSecond(unixTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    public static String formatDateTimeString(long unixTime, String format) {
        if (unixTime == 0) {
            return "";
        }
        LocalDateTime dateTime = asLocalDateTime(unixTime);
        Date date = date(dateTime);
        SimpleDateFormat formator = new SimpleDateFormat(format);
        return formator.format(date);
    }

    public static String formatDateTimeString(long unixTime) {
        return formatDateTimeString(unixTime, DATETIME_FORMAT_DATEONLY);
    }

    public static long asUnixTime(String str) {

        Date date = strToDate(str, DATETIME_FORMAT_DATEONLY);

        return asUnixTime(localDateTime(date));

    }

    /**
     * 获取本周开始的日期（周一的日期）
     *
     * @return
     */
    public static String getWeekStart() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TemporalAdjuster FIRST_OF_WEEK =
                TemporalAdjusters.ofDateAdjuster(localDate ->
                        localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue()));
        String weekStart = df.format(today.with(FIRST_OF_WEEK));
        return weekStart;
    }


    /**
     * 获取上周开始的日期（上周一的日期）
     *
     * @return
     */
    public static String getEarlyWeekStart() {
        Calendar cal = Calendar.getInstance();
        //将每周第一天设为星期一，默认是星期天
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.add(Calendar.DATE, -1 * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String monday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return monday;
    }


    /**
     * 获取上周的今天 （如 今天是星期三，则获取上周的星期三的日期）
     *
     * @return
     */
    public static String getEarlyWeekToday() {
        Calendar cs = Calendar.getInstance();
        cs.add(Calendar.WEEK_OF_YEAR, -1);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(cs.getTime());
    }

    /**
     * 获取本月的第一天的日期
     *
     * @return
     */
    public static String getCurrStartDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        String first = format.format(c.getTime());
        return first;
    }


    /**
     * 获取上个月的第一天的日期
     *
     * @return
     */
    public static String getEarlyStartDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal_1 = Calendar.getInstance();
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = format.format(cal_1.getTime());
        return firstDay;
    }

    /***
     * 获取下月第一天
     * @return
     */
    public static Date getFirstDayOfNextMonth(String fmt) {
        if (StringUtils.isBlank(fmt)) {
            fmt = DATETIME_FORMAT_DATEONLY;
        }
        SimpleDateFormat dft = new SimpleDateFormat(fmt);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return strToDate(dft.format(calendar.getTime()), fmt);
    }

    /***
     * 获取上个月 年份月份 yyyyMM
     * @return
     */
    public static String getLastMonth() {
        LocalDate today = LocalDate.now();
        today = today.minusMonths(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMM");
        return formatters.format(today);
    }

    /***
     * 获取上个月 年份月份 yyyyMM
     * @return
     */
    public static String getLastMonth(LocalDate today) {
        today = today.minusMonths(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMM");
        return formatters.format(today);
    }

    /**
     * 获取上个月的今天的日期
     *
     * @return
     */
    public static final LocalDate getLastMonthCurrentDate() {

        LocalDate now = LocalDate.now();

        int dayOfMonth = now.getDayOfMonth();

        return now.minusMonths(1).withDayOfMonth(dayOfMonth);
    }

    /**
     * 获取上个月的今天的第前7天
     *
     * @return
     */
    public static final String getLastMonthCurrent7daysAgo() {
        return getLastMonthCurrentDate().plusDays(-6).toString();
    }

    /**
     * 获取七天前的日期
     *
     * @return
     */
    public static String getSevenDaysAgo() {
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar1.add(Calendar.DATE, -6);
        String seven_days_ago = sdf1.format(calendar1.getTime());
        return seven_days_ago;
    }


    /**
     * 获取----
     *
     * @return
     */
    public static String get13DaysAgo() {
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        calendar2.add(Calendar.DATE, -13);
        String three_days_ago1 = sdf2.format(calendar2.getTime());
        return three_days_ago1;
    }

    public static String get60DaysAgo() {
        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        calendar2.add(Calendar.DATE, -60);
        String sixty_days_ago = sdf2.format(calendar2.getTime());
        return sixty_days_ago + " 00:00:00";
    }


    /**
     * 获取当前日期的7天前的日期
     *
     * @return
     */
    public static String get7DaysAgo() {
        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar1.add(Calendar.DATE, -7);
        String three_days_ago = sdf1.format(calendar1.getTime());
        return three_days_ago;
    }

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static String getearlyDay() {
        LocalDate today = LocalDate.now().plusDays(-1);
        return today.toString();
    }

    /**
     * 获取前天的日期
     *
     * @return
     */
    public static String getEarlyTwoDay() {
        LocalDate today = LocalDate.now().plusDays(-2);
        return today.toString();
    }

    /**
     * 获取当前日期所在月份的最后一天
     *
     * @return
     */
    public static LocalDate getLastDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * localDate 转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate == null ? null : localDate.atTime(LocalTime.parse("00:00:00"));
    }

    public static String formatDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATEONLY);
        return sdf.format(date);
    }

    /**
     * 获取指定日期的起始时间
     */
    public static Date getCurrentDateStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的结束时间
     */
    public static Date getCurrentDateEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 校验日期是否大于当前日期
     *
     * @param saleDate
     * @return
     */
    public static boolean checkDate(Date saleDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATEONLY);
        String today = sdf.format(new Date());
        String saleDateStr = sdf.format(saleDate);

        return saleDateStr.compareTo(today) <= 0;
    }

    /**
     * 校验日期是否是今天
     *
     * @param
     * @return
     */
    public static boolean checkIsToday(String saleDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATEONLY);
        String today = sdf.format(new Date());
        return saleDateStr.compareTo(today) == 0;
    }

    public static String timeToStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_DATEONLY);
        return sdf.format(date);
    }


    public static String dateToStrs(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DATEONLY);
        return sdf.format(date);
    }


    //上周六
    public static String szl(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, 7);
        c.add(Calendar.WEEK_OF_YEAR, -1);
        return format.format(c.getTime());
    }

    //这周五
    public static String zzw(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 5);
        return format.format(c.getTime());
    }


    //这周六
    public static String zzl(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 6);
        return format.format(c.getTime());
    }


    //下周五
    public static String xzw(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 12);
        return format.format(c.getTime());
    }


    public static Boolean isWeekend(Date bDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bDate);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }


    /**
     * 获取上上周六时间
     */
    public static String sszl() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 1 - dayOfWeek;
        calendar.add(Calendar.DATE, offset - 7);
        return getFirstDayOfWeek(calendar.getTime(), 0);
    }

    /**
     * 获取上周六时间
     */
    public static String szl() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 1 - dayOfWeek;
        return getFirstDayOfWeek(calendar.getTime(), 0);
    }


    /**
     * 获取上周五时间
     */
    public static String szw() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int offset = 1 - dayOfWeek;
        return getFirstDayOfWeek(calendar.getTime(), -1);
    }


    /**
     * 得到某一天的该星期的第一日 00:00:00
     *
     * @param date
     * @param firstDayOfWeek 一个星期的第一天为星期几
     * @return
     */
    public static String getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(firstDayOfWeek);//设置一星期的第一天是哪一天
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);//指示一个星期中的某天
        cal.set(Calendar.HOUR_OF_DAY, 0);//指示一天中的小时。HOUR_OF_DAY 用于 24 小时制时钟。例如，在 10:04:15.250 PM 这一时刻，HOUR_OF_DAY 为 22。
        cal.set(Calendar.MINUTE, 0);//指示一小时中的分钟。例如，在 10:04:15.250 PM 这一时刻，MINUTE 为 4。
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        return format.format(cal.getTime());
    }


    //上周一
    public static String geLastWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, -7);
        return dateToStr(cal.getTime(), DATE_FORMAT_DATEONLY);
    }

    //这周一
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public static String getThisMonday(Date date) {
        Date date1 = getThisWeekMonday(date);
        return dateToStr(date1, DATE_FORMAT_DATEONLY);
    }


    //下周一
    public static String getNextWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 7);
        return dateToStr(cal.getTime(), DATE_FORMAT_DATEONLY);
    }

    //获取周日
    public static String getSunDay(int n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return dateToStr(cal.getTime(), DATE_FORMAT_DATE_DAY);
    }


    /**
     * @Description: 根据月份获取该月最后一天
     * @Param: month 月份 : 201808
     * @return: 日期  2018-08-31
     * @Author: ZZH
     * @Date: 2018/9/4
     */
    public static LocalDate getLastDayByMonth(String month) {

        LocalDate now = LocalDate.now();


        if (StringUtils.isEmpty(month)) {

            return now.with(TemporalAdjusters.lastDayOfMonth());

        }
        Integer year = Integer.valueOf(StringUtils.substring(month, 0, 4));

        Integer iMonth = Integer.valueOf(StringUtils.substring(month, 4, 6));

        LocalDate firstday = LocalDate.of(year, iMonth, 1);

        return firstday.with(TemporalAdjusters.lastDayOfMonth());

    }


    /**
     * 获取今天0点
     *
     * @return
     */
    public static String getToday() {
        LocalDate now = LocalDate.now();
        String today = now.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
        return today + " 00:00:00";
    }

    /**
     * 获取明天0点
     *
     * @return
     */
    public static String getTomorrow() {
        LocalDate now = LocalDate.now();
        String tomorrow = now.plusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
        return tomorrow + " 00:00:00";
    }

    /***
     * 获取去年的这个时候
     * @param date
     * @return str
     */
    public static String getLastYearThisTime(Date date) {
        Date lastYearTime = org.apache.commons.lang3.time.DateUtils.addYears(date, -1);
        return dateToStr(lastYearTime);
    }

    /**
     * 得到startDate 时间加一年，yyyy-MM-dd 格式
     *
     * @param startDate
     * @return
     */
    public static String omsUsedCarDate(String startDate) {
        LocalDateTime time = strToLocalDateTime(startDate);
        if (time == null) {
            return null;
        }
        String date = DATE_FORMATTER.format(time.plusYears(1));
        return date;
    }

    /**
     * 根据指定时间获取前几天的时间
     *
     * @param currentDate
     * @param days
     * @return
     */
    public static String getSomeDaysBefore(Date currentDate, Integer days) {
        Calendar now = Calendar.getInstance();
        now.setTime(currentDate);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        return parseDateToString(now.getTime(), "yyyyMMdd");
    }

    /**
     * Date类型转换为string类型
     *
     * @return
     */
    public static String parseDateToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String dateString = formatter.format(date);
        return dateString;
    }

    /***
     * 获取某年的开始时间
     * @param year
     * @return
     */
    public static Date getYearStartDateTime(int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_YEAR, 1);
        setStartDateTime(c);
        return c.getTime();
    }

    /***
     * 获取某年的结束时间
     * @param year
     * @return
     */
    public static Date getYearEndDateTime(int year) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);
        c.roll(Calendar.DAY_OF_YEAR, -1);
        setEndDateTime(c);
        return c.getTime();
    }

    public static void setStartDateTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    public static void setEndDateTime(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
    }

    /**
     * 获取传入日期所在月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayDateOfMonth(final Date date) {

        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        final int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);

        return cal.getTime();

    }

    /**
     * 获取传入日期所在月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(final Date date) {

        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);

        return cal.getTime();

    }

    /**
     * 判断是否同一个月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        try {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
            return isSameMonth;
            /*boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
            return isSameDate;*/
        } catch (Exception e) {
            log.error("[RatingEngine] error occurred: ERROR ", e);
        }
        return false;
    }

    public static String dateToStr2(Date dateDate) {
        if (dateDate == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT_DATEONLY);
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String getTenMinsAgo(String end) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATEONLY);
        LocalDateTime ldt = LocalDateTime.parse(end, df);
        return ldt.minusMinutes(10).format(df);
    }

    public static Boolean isSunDay(Date bDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(bDate);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

}
