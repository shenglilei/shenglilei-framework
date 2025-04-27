package com.dofun.shenglilei.framework.core.monitor.stdio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 接口入参日志输出配置，过滤文件参数
 *
 * @author likejie
 * @version 1.0
 * @date 2018/11/3 11:50
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "stdio.filter")
public class StdIOProperties {

    private Item outFilter;

    private Item inFilter;


    @Data
    public static class Item {

        /***过滤包*/
        private List<String> classNames;

        /***过滤具体方法*/
        private List<String> methods;

    }
}
