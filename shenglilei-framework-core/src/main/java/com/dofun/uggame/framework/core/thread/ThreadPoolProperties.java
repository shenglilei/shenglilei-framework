package com.dofun.uggame.framework.core.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

@Data
@Configuration
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {

    private final int cpuCoreCount = ForkJoinPool.getCommonPoolParallelism();

    private int corePoolSize = cpuCoreCount * 2;
    private int maxPoolSize = cpuCoreCount * 10;
    private int queueCapacity = cpuCoreCount * 100;
    private int keepAliveSeconds = 300;


}
