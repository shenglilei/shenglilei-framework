package com.dofun.shenglilei.framework.common.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SpringContextConfiguration {

    @Bean
    @ConditionalOnMissingBean(value = {com.dofun.shenglilei.framework.common.spring.SpringContextHolder.class}, name = {"SpringContextHolder", "springContextHolder"})
    public com.dofun.shenglilei.framework.common.spring.SpringContextHolder springContextHolder() {
        log.info("SpringContextHolder is ready to inject.");
        return new com.dofun.shenglilei.framework.common.spring.SpringContextHolder();
    }
}
