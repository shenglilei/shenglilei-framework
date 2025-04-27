package com.dofun.uggame.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：身份证信息算法类
 */
public class CardUtils {
    /**
     * 18位身份证 得到性别
     *
     * @return
     */
    public static String getSex(String card) {
        String sex = "1";
        if (StringUtils.isBlank(card)) {
            return sex;
        }
        try {
            if (card.length() == 15) {
                String usex = card.substring(14, 15);
                if (Integer.parseInt(usex) % 2 == 0) {
                    // 女
                    sex = "2";
                } else {
                    // 男
                    sex = "1";
                }
            } else if (card.length() == 18) {
                // 判断性别
                if (Integer.parseInt(card.substring(16).substring(0, 1)) % 2 == 0) {
                    // 女
                    sex = "2";
                } else {
                    // 男
                    sex = "1";
                }
            }
        } catch (Exception e) {
            return "1";
        }
        return sex;
    }

    /**
     * 根据身份证获取出生日期
     *
     * @param certificateNo
     * @return
     */
    public static Date getBirthday(String certificateNo) {
        if (CardUtils.checkCard(certificateNo)) {
            return null;
        }
        String birthday = "";
        if (certificateNo.length() == 15) {
            birthday = "19" + certificateNo.substring(6, 8) + "-"
                    + certificateNo.substring(8, 10) + "-"
                    + certificateNo.substring(10, 12);
        } else if (certificateNo.length() == 18) {
            birthday = certificateNo.substring(6, 10) + "-"
                    + certificateNo.substring(10, 12) + "-"
                    + certificateNo.substring(12, 14);
        }
        Date date = DateUtils.strToDate(birthday, DateUtils.DATE_FORMAT_DATEONLY);
        return date;
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 校验身份证是否符合规则
     *
     * @param card
     * @return
     */
    public static boolean checkCard(String card) {
        if (card.length() == 15) {
            String regex = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";
            return !match(regex, card);
        } else if (card.length() == 18) {
            String regex = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
            return !match(regex, card);
        } else {
            return true;
        }
    }
}
