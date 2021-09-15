package com.dofun.uggame.framework.redis.lock;


import com.dofun.uggame.framework.common.spring.SpringContextHolder;
import com.dofun.uggame.framework.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLock implements AutoCloseable {
    private volatile RLock lock;

    private int expireMsecs = 60 * 1000;
    private int timeoutMsecs = 10 * 1000;

    public RedisLock(RedisService redisService, String lockKey) {
        log.debug("lockKey：{}，create instance start.", lockKey);
        RedissonClient redissonClient = SpringContextHolder.getBean(RedissonClient.class);
        if (redissonClient == null) {
            throw new RuntimeException("redissonClient can not be null.");
        }
        this.lock = redissonClient.getLock(lockKey);
        if (lock == null) {
            throw new RuntimeException("lock can not be null.");
        }
        log.debug("lockKey：{}，create instance end.", lockKey);
    }

    public RedisLock(RedisService redisService, String lockKey, int timeoutMsecs) {
        this(redisService, lockKey);
        this.timeoutMsecs = timeoutMsecs;
    }

    public RedisLock(RedisService redisService, String lockKey, int timeoutMsecs, int expireMsecs) {
        this(redisService, lockKey, timeoutMsecs);
        this.expireMsecs = expireMsecs;
    }

    public String getLockKey() {
        if (lock == null) {
            throw new RuntimeException("lock can not be null.");
        }
        return lock.getName();
    }

    public boolean lock() throws InterruptedException {
        log.debug("lockKey：{}，timeoutMsecs：{}，expireMsecs：{},lock start.", getLockKey(), timeoutMsecs, expireMsecs);
        boolean ret = lock.tryLock(timeoutMsecs, expireMsecs, TimeUnit.MILLISECONDS);
        log.debug("lockKey：{}，timeoutMsecs：{}，expireMsecs：{},lock end:{}.", getLockKey(), timeoutMsecs, expireMsecs, ret);
        return ret;
    }

    public void unlock() {
        log.debug("lockKey：{}，unlock start.", getLockKey());
        if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.debug("lockKey：{}，unlock success.", getLockKey());
        } else {
            if (lock == null) {
                return;
            }
            if (!lock.isLocked()) {
                lock = null;
                return;
            }
            log.warn("lockKey：{}，unlock failed.", getLockKey());
            log.warn("lockKey：{}，lock is null，{}", getLockKey(), lock == null);
            if (lock != null) {
                log.warn("lockKey：{}，lock is locked，{}", getLockKey(), lock.isLocked());
                log.warn("lockKey：{}，lock hold count:{}", getLockKey(), lock.getHoldCount());
                log.warn("lockKey：{}，lock isHeldByCurrentThread，{}", getLockKey(), lock.isHeldByCurrentThread());
            }
        }
        String lockKey = lock == null ? "null" : getLockKey();
        lock = null;
        log.debug("lockKey：{}，unlock end.", lockKey);
    }

    @Override
    public void close() {
        unlock();
    }
}
