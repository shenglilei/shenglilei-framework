package com.dofun.shenglilei.common.util;


import org.apache.commons.lang3.StringUtils;

/**
 * 断言工具类
 */
public class AssertUtils {

    /**
     * 如果条件为false时，抛出serviceException
     *
     * @param condition
     * @param message
     * @throws RuntimeException
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new RuntimeException(message);
        }
    }

    public static void notEmpty(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RuntimeException(message);
        }
    }

}
