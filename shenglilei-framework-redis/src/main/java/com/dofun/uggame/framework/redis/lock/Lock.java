package com.dofun.uggame.framework.redis.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.dofun.uggame.framework.redis.lock.RedisLock.defaultExpireMsecs;
import static com.dofun.uggame.framework.redis.lock.RedisLock.defaultTimeoutMsecs;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * 锁的key
     */
    String key() default "";

    /**
     * 锁等待时间，防止线程饥饿
     * <p>
     * 指的是线程获取不到锁，进入等待状态的超时时间，超时后进入获取锁失败
     */
    int timeoutMsecs() default defaultTimeoutMsecs;


    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     * <p>
     * 指的是线程获取到锁，进入加锁状态的超时时间，超时后进入自动释放锁
     */
    int expireMsecs() default defaultExpireMsecs;
}
