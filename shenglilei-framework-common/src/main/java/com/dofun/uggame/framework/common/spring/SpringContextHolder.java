package com.dofun.uggame.framework.common.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * spring容器
 *
 * @since 2017年6月22日
 */
@Slf4j
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
        log.info("get springContext is success");
    }

    /**
     * 获取spring bean
     */
    public static <T> T getBean(String beanName) {
        checkApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 获取spring bean
     */
    public static <T> T getBean(String beanName, @NonNull Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(beanName, clazz);
    }

    /**
     * 获取spring bean
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        Map<String, T> beans = applicationContext.getBeansOfType(clazz);
        if (beans.isEmpty()) {
            return null;
        }
        return beans.values().iterator().next();
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext 为空，请检查配置");
        }
    }
}
