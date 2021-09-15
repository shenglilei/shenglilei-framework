package com.dofun.uggame.common.util;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;

/**
 * 适配JNI相关方法，
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

    /**
     * 获取当前class对象的超类
     *
     * @param clazz 当前class对象
     * @return 超类的Class对象，无超类则返回null
     */
    public static Class<?> getSuperclass(Class<?> clazz) {
        List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(clazz);
        if (allSuperclasses != null && allSuperclasses.size() > 0) {
            return allSuperclasses.get(0);
        }
        return null;
    }

    /**
     * 获取对象的超类属性
     *
     * @param obj      当前对象
     * @param fileName 超类属性名
     * @return
     * @throws NoSuchFieldException
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

    /**
     * 对象是否为数组
     *
     * @param obj
     * @return
     */
    public static boolean isArray(Object obj) {
        String clazzName = getClass(obj).getName();
        int internalArrayMarker = clazzName.indexOf(INTERNAL_ARRAY_PREFIX);
        // "java.lang.String[]" style arrays
        // "[Ljava.lang.String;" style arrays
        return clazzName.endsWith(ARRAY_SUFFIX) || (internalArrayMarker != -1 && clazzName.endsWith(";"));
    }

    public static int hashCode(Object obj) {
        return org.apache.commons.lang3.ObjectUtils.hashCode(obj);
    }

    public static boolean isAssignableFrom(Class<?> cls, Class<?> toClass) {
        return ClassUtils.isAssignable(cls, toClass);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        return ClassUtils.isPrimitiveOrWrapper(type);
    }

    public static long currentTimeMillis() {
        return Instant.now().toEpochMilli();
    }
}
