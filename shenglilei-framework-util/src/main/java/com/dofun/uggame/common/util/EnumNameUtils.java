package com.dofun.uggame.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 描述：枚举处理
 */
@Slf4j
public class EnumNameUtils {

    /**
     * 描述：根据枚举名称值得到枚举对象，如：A01("A01", "上海市浦东区仲裁委员会")
     *
     * @param clazz                 EarthEnums.ArbitBoardname.class
     * @param getTypeNameMethodName getName
     * @param name                  上海市浦东区仲裁委员会
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T getByStringTypeName(Class<T> clazz, String getTypeNameMethodName, String name) {
        T result = null;
        try {
            T[] arr = clazz.getEnumConstants();
            Method targetMethod = clazz.getDeclaredMethod(getTypeNameMethodName);
            String typeNameVal;
            for (T entity : arr) {
                typeNameVal = targetMethod.invoke(entity).toString();
                if (typeNameVal.contentEquals(name)) {
                    result = entity;
                    break;
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

}
