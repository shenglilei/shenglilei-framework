package com.dofun.uggame.framework.core.id;


import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * id生成器
 */
@Slf4j
public class IdUtil {
    private final String applicationName;

    public IdUtil(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<Long> next(int size) {
        return new ArrayList<>(batchCreateID(this.applicationName, size));
    }

    public Long next() {
        return createId(this.applicationName);
    }

    /**
     * 优点：设置好worker+datacenter,重复率非常低，生产性能非常高，不依赖外部数据库
     * <p>
     * 缺点：在for循环等短时间大批量场景下，产生数据会非常近似，从而容易被预判到：
     * <p>
     * 以下为样本数据
     * <p>
     * 720608303755749240
     * <p>
     * 720608303747359167
     * <p>
     * 720608303747359169
     */
    private Set<Long> batchCreateID(String workerId, int size) {
        log.info("开始批量生产唯一Id,workerId:" + workerId + ",expect size:" + size);
        SnowflakeIdWorker idWorker = createWorker(workerId);
        Set<Long> result = IntStream.range(0, size).mapToObj(i -> idWorker.nextId()).collect(Collectors.toCollection(() -> new HashSet<>(size)));
        log.info("完成批量生产唯一Id,result size:" + result.size() + "," + (result.size() == size));
        return result;
    }

    private Long createId(String workerId) {
        SnowflakeIdWorker idWorker = createWorker(workerId);
        return idWorker.nextId();
    }

    private SnowflakeIdWorker createWorker(String workerId) {
        return new SnowflakeIdWorker(Math.abs(workerId.hashCode()) % 31, Math.abs(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH")).hashCode() % 31));
    }
}
