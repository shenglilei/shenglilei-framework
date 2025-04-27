package com.dofun.shenglilei.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * @Description： 随机数生成
 */
@Slf4j
public class RandomUtil {

    private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERCHAR = "0123456789";

    public static int getInt(int bound) {
        SecureRandom random = CommonUtils.getRandom();
        return (int) ((random.nextDouble() * 9 + 1) * bound);
    }

    /*public static int getInt(int bound) {
        //Math.random() * 9 + 1 获取1到10（不包括10）的随机数
        return (int) ((Math.random() * 9 + 1) * bound);
    }*/

    public static String getFixedSeqNum(int l) {
        return String.format("%011d", l);
    }

    public static String getCode(int id) {
        return DateUtils.formatDateTimeString(DateUtils.asUnixTime(LocalDateTime.now()), "yyyyMMddHHmmss")
                + getFixedSeqNum(id)
                + CommonUtils.getRandNum(6);
    }

    /**
     * 返回长度为length的随机字符串，由中文组成
     * <p>
     * 例如：length为10时，返回十个随机中文
     */
    public static String getChinese(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(getChinese());
        }
        return builder.toString();
    }

    /**
     * 返回1个中文
     */
    public static String getChinese() {
        String str = null;
        int highCode;
        int lowCode;

        highCode = (176 + Math.abs(CommonUtils.getRandByBound(39)));
        lowCode = (161 + Math.abs(CommonUtils.getRandByBound(93)));
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highCode)).byteValue();
        b[1] = (Integer.valueOf(lowCode)).byteValue();
        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return str;
    }

    /**
     * 返回长度为length的随机字符串，由数字+大小写字母组成
     */
    public static String getMixed(int length) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = CommonUtils.getRandom();
        for (int i = 0; i < length; i++) {
            builder.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return builder.toString();
    }

    /**
     * 返回长度为length的随机字符串，由小写字母组成
     */
    public static String getLetter(int length) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = CommonUtils.getRandom();
        for (int i = 0; i < length; i++) {
            builder.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return builder.toString();
    }

    /**
     * 返回长度为length的随机字符串，由数字组成
     */
    public static String getNumber(int length) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = CommonUtils.getRandom();
        for (int i = 0; i < length; i++) {
            builder.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return builder.toString();
    }
}
