package com.dofun.uggame.framework.common.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author likejie
 * @version 1.0
 * @description: SpringContextAutoConfiguration
 * @date 2019/9/3 19:48
 */
@Configuration
@Slf4j
public class SpringContextConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {SpringContextHolder.class}, name = {"SpringContextHolder", "springContextHolder"})
    public SpringContextHolder springContextHolder() {
        log.info("SpringContextHolder is ready to inject.");
        return new SpringContextHolder();
    }
}
