package com.dofun.uggame.framework.redis.lock;


import com.dofun.uggame.framework.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
@Slf4j
public class RedisLockAspect {
    private final RedisService redisService;

    public RedisLockAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 切点(控制器中返回值为WebApiResponse且添加RedisLockAop注解的方法)
     */
    @Pointcut("@annotation(com.dofun.uggame.framework.redis.lock.Lock)")
    public void redisLockAopPointcut() {

    }

    @Around("redisLockAopPointcut()")
    public Object lock(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        try (RedisLock redisLock = new RedisLock(redisService, lock.key(), lock.timeoutMsecs(), lock.expireMsecs())) {
            if (redisLock.lock()) {
                return pjp.proceed();
            } else {
                throw new RuntimeException("获取锁失败");
            }
        }
    }
}
