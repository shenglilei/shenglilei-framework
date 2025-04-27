package com.dofun.shenglilei.common.util;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：类型转换扩展类
 */
public class StringUtilsExt {


    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String trim(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            return obj.trim();
        }
        return "";

    }

    /**
     * 转换为int
     */
    public static Integer toInteger(String obj) {
        if (StringUtils.isBlank(obj)) {
            return -1;
        }
        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为int
     */
    public static Long toLong(String obj) {

        if (StringUtils.isBlank(obj)) {
            return -1L;
        }
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为double
     */
    public static Double toDouble(String obj) {

        if (StringUtils.isBlank(obj)) {
            return -1D;
        }
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为BigDecimal
     */
    public static BigDecimal toBigDecimal(String obj) {
        if (StringUtils.isBlank(obj)) {
            return BigDecimal.valueOf(0);
        }
        try {
            return new BigDecimal(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为BigDecimal string
     */
    public static String toString(Integer obj) {
        if (obj == null) {
            return Integer.toString(0);
        }
        try {
            return obj.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为BigDecimal string
     */
    public static String toString(BigDecimal obj) {
        if (obj == null) {
            return BigDecimal.valueOf(0).toString();
        }
        try {
            return obj.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为date
     */
    public static Date toDate(String obj) {
        if (StringUtils.isBlank(obj)) {
            return null;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            ParsePosition pos = new ParsePosition(0);
            return formatter.parse(obj, pos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为date
     */
    public static Date toDate(String obj, String formatter) {
        if (StringUtils.isBlank(obj)) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatter);
            ParsePosition pos = new ParsePosition(0);
            return simpleDateFormat.parse(obj, pos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为LocalDate
     */
    public static LocalDate toLocalDate(String obj) {
        if (StringUtils.isBlank(obj)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            return LocalDate.parse(obj, formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换为LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(String obj) {
        if (StringUtils.isBlank(obj)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            return LocalDateTime.parse(obj, formatter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 补齐n位
     *
     * @return
     */
    public static String complementZero(int pos, int n) {
        String str = n + "";
        int len = str.length();
        if (len > pos)
            throw new RuntimeException("位数不合法，pos：" + pos + "，len：" + len);

        for (int i = 0; i < pos - len; i++)
            str = "0" + str;

        return str;
    }

    /**
     * 获取年月，格式：yyyyMM,201809
     *
     * @return
     */
    public static String getYearMonth(LocalDateTime localDateTime) {
        //当前年
        String year = Integer.toString(localDateTime.getYear());
        //当前月
        String month = Integer.toString(localDateTime.getMonth().getValue());
        int size = 2;
        if (month.length() < size) {
            month = "0" + month;
        }
        return year + month;
    }

    /**
     * 替换 _ 为 \_
     *
     * @param str
     * @return
     */
    public static String underlineReplace(String str) {

        if (StringUtils.isNotBlank(str)) {
            char[] chars = str.toCharArray();
            String newStr = "";
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '_') {
                    newStr += "\\_";
                } else {
                    newStr += chars[i];
                }
            }
            return newStr;
        } else {
            return str;
        }

    }

    public static int getCharacterPosition(Pattern pattern, String str, int count) {
        Matcher findMatcher = pattern.matcher(str);
        int number = 0;
        while (findMatcher.find()) {
            number++;
            if (number == count) {
                break;
            }
        }
        int i = findMatcher.start();
        return i;
    }

}
