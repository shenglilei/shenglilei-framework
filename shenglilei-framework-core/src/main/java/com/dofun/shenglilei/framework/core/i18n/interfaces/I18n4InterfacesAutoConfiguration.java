package com.dofun.shenglilei.framework.core.i18n.interfaces;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 接口出参的多语言输出处理器
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/11
 * Time:10:05
 */
@Slf4j
@Configuration
public class I18n4InterfacesAutoConfiguration {
    @Autowired
    private I18n4InterfacesProperties i18n4InterfacesProperties;

    @Bean
    public com.dofun.shenglilei.framework.core.i18n.interfaces.I18n4InterfacesProcessor create4I18n4InterfacesProcessor() {
        log.debug("I18n4InterfacesProcessor loaded.");
        return new com.dofun.shenglilei.framework.core.i18n.interfaces.I18n4InterfacesProcessor(i18n4InterfacesProperties);
    }
}
