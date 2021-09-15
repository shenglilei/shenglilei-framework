package com.dofun.uggame.framework.core.conf;

import com.netflix.hystrix.HystrixCommandProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.PostConstruct;

import static com.dofun.uggame.framework.common.constants.Constants.APPLICATION_NAME;
import static com.dofun.uggame.framework.common.constants.Constants.HYSTRIX_STRATEGY_THREAD;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/18
 * Time:19:10
 *
 * @author steven
 */
@RefreshScope
@Slf4j
@Getter
public class Config {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${feign.hystrix.enabled: true}")
    private boolean hystrixEnabled;

    @Value("${hystrix.command.default.execution.isolation.strategy: THREAD}")
    private HystrixCommandProperties.ExecutionIsolationStrategy hystrixStrategy;

    @PostConstruct
    public void initConstants() {
        APPLICATION_NAME = applicationName;
        log.info("当前应用名:" + APPLICATION_NAME);
        if (hystrixEnabled && hystrixStrategy.equals(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)) {
            HYSTRIX_STRATEGY_THREAD = true;
        }
        log.info("HYSTRIX_STRATEGY_THREAD:" + HYSTRIX_STRATEGY_THREAD);
    }
}
