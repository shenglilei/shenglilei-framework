package com.dofun.uggame.framework.core.bootstrap;


import com.dofun.uggame.framework.core.id.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static com.dofun.uggame.framework.common.Constants.SYSTEM_DEFAULT_PACKAGE_ROOT;

@Slf4j
@ServletComponentScan
@EnableAutoConfiguration
@ComponentScan(basePackages = {SYSTEM_DEFAULT_PACKAGE_ROOT+".*"})
@SpringBootConfiguration
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {SYSTEM_DEFAULT_PACKAGE_ROOT})
public class BootStrapAutoConfiguration {
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    @ConditionalOnMissingBean(value = {IdUtil.class})
    public IdUtil createIdUtil() {
        log.info("IdUtil is ready to inject.");
        return new IdUtil(applicationName);
    }
}
