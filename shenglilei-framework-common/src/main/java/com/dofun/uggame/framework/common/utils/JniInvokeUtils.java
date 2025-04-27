package com.dofun.uggame.framework.common.utils;

import org.apache.commons.lang3.ClassUtils;

import java.time.Instant;

/**
 * 适配JNI相关方法，
 *
 * @author LiuQing.Qin
 * @date 2020/1/10 11:25
 */
public final class JniInvokeUtils {

    /**
     * Suffix for array class names: "[]"
     */
    public static final String ARRAY_SUFFIX = "[]";
    /**
     * Prefix for internal array class names: "[L"
     */
    private static final String INTERNAL_ARRAY_PREFIX = "[L";

    public static Class<?> getClass(Object obj) {
        Class<?> clazz = null;
        if (obj == null) {
            throw new NullPointerException();
        }
        try {
            clazz = ClassUtils.getClass(ClassUtils.getName(obj));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static long currentTimeMillis() {
        return Instant.now().toEpochMilli();
    }
}
