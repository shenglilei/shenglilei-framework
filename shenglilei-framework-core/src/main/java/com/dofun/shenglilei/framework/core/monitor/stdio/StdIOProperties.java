package com.dofun.shenglilei.framework.core.monitor.stdio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 接口入参日志输出配置，过滤文件参数
 */
@RefreshScope
@Data
@Configuration
@ConfigurationProperties(prefix = "stdio.filter")
@Slf4j
public class StdIOProperties {

    private Item outFilter;

    private Item inFilter;

    @Data
    public static class Item {
        List<String> cls;
        List<String> meths;
    }

    @PostConstruct
    public void initConstants() {
        log.info("当前outFilter配置列表:{}", outFilter);
        log.info("当前inFilter配置列表:{}", inFilter);
    }
}
