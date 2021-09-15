package com.dofun.uggame.framework.core.bootstrap;


import com.dofun.uggame.framework.core.id.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author :ChengLiang
 * @date:2018/9/27 16:00
 */
@Slf4j
@ServletComponentScan
@EnableAutoConfiguration(exclude = {
        FlywayAutoConfiguration.class
})
@ComponentScan(
        basePackages = {"com.dofun.uggame.*"},
        excludeFilters = {@ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {TypeExcludeFilter.class}
        ), @ComponentScan.Filter(
                type = FilterType.CUSTOM,
                classes = {AutoConfigurationExcludeFilter.class}
        ), @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
@SpringCloudApplication
@RibbonClients(defaultConfiguration = RibbonAutoConfiguration.class)
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
