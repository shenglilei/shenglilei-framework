package com.dofun.uggame.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class PojoConvertUtils {

    /**
     * 赋值实体值
     *
     * @param src
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convertPojo(Object src, Class<T> targetClass) {
        if (src == null) {
            return null;
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("convertPojo 实例化异常", e);
        }
        BeanUtils.copyProperties(src, instance);
        return instance;
    }

    /**
     * 批量赋值实体的值
     *
     * @param orig
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertPojos(List orig, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(orig)) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(orig.size());
        for (Object object : orig) {
            list.add(convertPojo(object, targetClass));
        }
        return list;
    }
}
