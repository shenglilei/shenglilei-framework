package com.dofun.uggame.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.framework.AopProxy;

import java.lang.reflect.Field;

/**
 * aop工具类
 */
@Slf4j
public class AopUtils {

    /**
     * 获取代理类
     * <p>
     * 需要在配置类中 @EnableAspectJAutoProxy(exposeProxy = true)
     *
     * @param sourceObj
     * @return
     * @deprecated 请使用ProxyAware接口实现，因为该方法在多线程环境中无法正确获取
     */
    @Deprecated
    public static Object getProxy(Object sourceObj) {
        Object proxyObj = AopContext.currentProxy();
        if (proxyObj == null) {
            return sourceObj;
        }
        return proxyObj;
    }

    public static Object getTarget(Object proxyObj) {
        Object sourceObj = null;
        if (!org.springframework.aop.support.AopUtils.isAopProxy(proxyObj)) {
            return proxyObj;// 不是代理对象
        }
        if (org.springframework.aop.support.AopUtils.isCglibProxy(proxyObj)) {
            try {
                sourceObj = getCglibProxyTargetObject(proxyObj);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
        if (org.springframework.aop.support.AopUtils.isJdkDynamicProxy(proxyObj)) {
            try {
                sourceObj = getJdkDynamicProxyTargetObject(proxyObj);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }
        return sourceObj;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxyObj) throws Exception {
        //Field h = proxyObj.getClass().getSuperclass().getDeclaredField("h");
        Field h = JniInvokeUtils.getSuperclassDeclaredField(proxyObj, "h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxyObj);

        //Field advised = aopProxy.getClass().getDeclaredField("advised");
        Field advised = JniInvokeUtils.getClass(aopProxy).getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }

    private static Object getCglibProxyTargetObject(Object proxyObj) throws Exception {
        //Field h = proxyObj.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        Field h = JniInvokeUtils.getClass(proxyObj).getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxyObj);

        //Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        Field advised = JniInvokeUtils.getClass(dynamicAdvisedInterceptor).getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }
}
