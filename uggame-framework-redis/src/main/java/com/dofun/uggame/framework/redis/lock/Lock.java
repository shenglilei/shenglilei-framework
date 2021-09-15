package com.dofun.uggame.framework.redis.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description：
 * @author: ZZH
 * @date: 2018/11/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    String key() default "";

    /**
     * 锁等待时间，防止线程饥饿
     */
    int timeoutMsecs() default 10000;


    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
    int expireMsecs() default 60000;
}
