package com.dofun.shenglilei.framework.core.conf;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.PostConstruct;

import static com.dofun.shenglilei.framework.common.constants.Constants.APPLICATION_NAME;

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


    @PostConstruct
    public void initConstants() {
        APPLICATION_NAME = applicationName;
        log.info("当前应用名:" + APPLICATION_NAME);
    }
}
