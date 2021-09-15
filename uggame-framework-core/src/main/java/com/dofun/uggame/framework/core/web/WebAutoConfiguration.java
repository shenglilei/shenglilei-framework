package com.dofun.uggame.framework.core.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/11
 * Time:10:05
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "global-exception", name = "enable", matchIfMissing = true, havingValue = "true")
public class WebAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/info");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        log.info("homepage view is ready to inject.");
    }
}