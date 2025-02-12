package com.dofun.uggame.framework.core.base;


import com.dofun.uggame.framework.core.conf.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/9/18
 * Time:13:26
 *
 * @author steven
 */
@Configuration
@Slf4j
public class BaseAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(Config.class)
    public Config createBaseConfig() {
        log.info("Config is ready to inject.");
        return new Config();
    }
}
