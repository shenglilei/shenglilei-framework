package com.dofun.shenglilei.framework.mysql.properties;

import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态表名配置
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = DynamicTableNameProperties.PREFIX)
public class DynamicTableNameProperties {
    public final static String PREFIX = "dynamic.table.name.mysql";

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 配置文件：表名称，与模式
     */
    private Map<String, DynamicTableNameMode.Mode> tableConfig = new HashMap<>();
}
