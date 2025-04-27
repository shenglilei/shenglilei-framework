package com.dofun.uggame.common.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：对象映射工具
 */
public class BeanMapperUtil {

    private static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    public static void copyProperties(Object source, Object target) {
        String beankey = generateKey(JniInvokeUtils.getClass(source), JniInvokeUtils.getClass(target));
        BeanCopier copier = BeanCopier.create(JniInvokeUtils.getClass(source), JniInvokeUtils.getClass(target), false);
        BEAN_COPIER_MAP.putIfAbsent(beankey, copier);
        copier = BEAN_COPIER_MAP.get(beankey);
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }
}
