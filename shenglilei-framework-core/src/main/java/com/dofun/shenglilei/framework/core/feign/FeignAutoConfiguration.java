package com.dofun.shenglilei.framework.core.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/9/18
 * Time:13:26
 */
@Slf4j
@Configuration
public class FeignAutoConfiguration {
    @Bean
    public FeignInterceptor createFeignInterceptor() {
        log.info("FeignInterceptor is ready to inject.");
        return new FeignInterceptor();
    }
}
