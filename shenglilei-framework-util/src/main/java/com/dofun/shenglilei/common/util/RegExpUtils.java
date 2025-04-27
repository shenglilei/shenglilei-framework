package com.dofun.shenglilei.common.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：正则校验
 */
public class RegExpUtils {

    /**
     * 验证是否包含中文，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isCharChina(String str) {
        String regex = "^.*[\\u4e00-\\u9fa5].*$";
        return !match(regex, str);
    }

    /**
     * 验证是否包含英文大写，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isBigEnglish(String str) {
        String regex = "^.*[A-Z].*$";
        return !match(regex, str);
    }

    /**
     * 验证是否包含数字，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        String regex = "^.*[0-9].*$";
        return !match(regex, str);
    }

    /**
     * 验证只能输入1或0，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean checkInputZeroOrOne(String str) {
        String regex = "^[0-1]$";
        return !match(regex, str);
    }

    /**
     * 验证日期是否符合YYYY-MM-DD，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean checkInputDate(String str) {
        String regex = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
        return !match(regex, str);
    }

    /**
     * 验证支付申请系统：01-网销/02-收付费/03-电销/04-ECAR/05-销售支持/06-开元车险/07-网销淘宝合作业务/11-中民车险
     *
     * @param str
     * @return
     */
    public static boolean checkPaySystemCode(String str) {
        String regex = "^(0[1-7]|11)$";
        return !match(regex, str);
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
     * 验证手机号，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        String regex = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
        return !match(regex, str);
    }

    /**
     * 验证邮箱，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isMail(String str) {
        String regex = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
        return !match(regex, str);
    }

    /**
     * 验证排量格式，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isEngineCapacity(BigDecimal str) {
        String regex = "^[0-8]+([.][0-9])?$";
        return !match(regex, str.toString());
    }

    /**
     * 验证排量范围，不符合反加TRUE，符合返回FALSE
     *
     * @param str
     * @return
     */
    public static boolean isEngineCapacityRange(BigDecimal str) {
        BigDecimal low = BigDecimal.valueOf(0.4);
        BigDecimal high = BigDecimal.valueOf(8.0);
        if (str.compareTo(low) == -1) {
            return true;
        }
        if (str.compareTo(high) == 1) {
            return true;
        }
        return false;
    }


}
