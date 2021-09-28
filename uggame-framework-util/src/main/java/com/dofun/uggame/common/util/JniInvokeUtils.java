package com.dofun.uggame.common.util;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;

/**
 * 适配JNI相关方法，
 */
public final class JniInvokeUtils {

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

    /**
     * 获取对象的超类属性
     *
     * @param obj      当前对象
     * @param fileName 超类属性名
     */
    public static Field getSuperclassDeclaredField(Object obj, String fileName) throws NoSuchFieldException {
        Class<?> aClass = getClass(obj);
        List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(aClass);
        for (Class<?> cl : allSuperclasses) {
            try {
                return cl.getDeclaredField(fileName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        throw new NoSuchFieldException(fileName);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type);
    }

    public static long currentTimeMillis() {
        return Instant.now().toEpochMilli();
    }
}
