package com.dofun.uggame.framework.core.thread;


import com.dofun.uggame.framework.core.conf.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfiguration extends AsyncConfigurerSupport {

    @Autowired(required = false)
    private ThreadPoolProperties threadPoolProperties;

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = {TaskExecutor.class, ThreadPoolTaskExecutor.class})
    ThreadPoolTaskExecutor createThreadPoolTaskExecutor(Config config) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        if (threadPoolProperties == null) {
            log.info(" config is null");
            //cpu 核心数
            int cpuCoreCount = ForkJoinPool.getCommonPoolParallelism();
            log.info("cpuCoreCount is " + cpuCoreCount);
            threadPoolTaskExecutor.setCorePoolSize(cpuCoreCount * 10);
            threadPoolTaskExecutor.setQueueCapacity(cpuCoreCount * 20);
            threadPoolTaskExecutor.setMaxPoolSize(cpuCoreCount * 100);
            threadPoolTaskExecutor.setKeepAliveSeconds(300);
        } else {
            threadPoolTaskExecutor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
            threadPoolTaskExecutor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
            threadPoolTaskExecutor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
            threadPoolTaskExecutor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        }
        threadPoolTaskExecutor.setThreadNamePrefix(config.getApplicationName() + "-");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.afterPropertiesSet();
        log.info("ThreadPoolTaskExecutor is ready to inject.");
        return threadPoolTaskExecutor;
    }
}
