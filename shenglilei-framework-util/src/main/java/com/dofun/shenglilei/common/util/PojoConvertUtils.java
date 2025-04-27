package com.dofun.shenglilei.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PojoConvertUtils {

    /**
     * BeanCopier的缓存
     * BeanCopier效率比BeanUtils.copyProperties效率快很多，但重复create性能会降低，这里做缓存处理
     */
    static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();

    /**
     * 赋值实体值
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T convertPojo(Object source, Class<T> target) {
        if (source == null || target == null) return null;
        String key = genKey(source.getClass().getName(), target.getName());
        BeanCopier beanCopier;
        if (BEAN_COPIER_CACHE.containsKey(key)) {
            beanCopier = BEAN_COPIER_CACHE.get(key);
        } else {
            beanCopier = BeanCopier.create(source.getClass(), target, false);
            BEAN_COPIER_CACHE.put(key, beanCopier);
        }
        try {
            T t = target.newInstance();
            beanCopier.copy(source, t, null);
            return t;
        } catch (Exception e) {
            log.error("bean copy error reason is", e);
            return null;
        }
    }

    /**
     * 批量赋值实体的值
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> convertPojos(List source, Class<T> target) {
        if (source == null || target == null) return null;
        String key = genKey(source.getClass().getName() + source.get(0).getClass().getName(), target.getName());
        BeanCopier copier;
        if (BEAN_COPIER_CACHE.containsKey(key)) {
            copier = BEAN_COPIER_CACHE.get(key);
        } else {
            copier = BeanCopier.create(source.get(0).getClass(), target, false);
            BEAN_COPIER_CACHE.put(key, copier);
        }
        try {
            List<T> list = new ArrayList<>();
            for (Object o : source) {
                T t = target.newInstance();
                copier.copy(o, t, null);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            log.error("bean copy error reason is", e);
            return null;
        }
    }

    /**
     * 生成key
     *
     * @param sourceClassName 源文件的className
     * @param tragetClassName 目标文件的className
     * @return string
     */
    private static String genKey(String sourceClassName, String tragetClassName) {
        return sourceClassName + tragetClassName;
    }

}
