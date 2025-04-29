package com.dofun.shenglilei.framework.mysql.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 多租户配置
 */
@Data
@ConfigurationProperties(prefix = TenantProperties.PREFIX)
public class TenantProperties {
    public final static String PREFIX = "tenant.mysql";

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 隔离级别是行级时，字段名称
     */
    private String columnName = "country_id";

    /**
     * 忽略处理的表名称
     */
    private Set<String> ignoreTableName;

    public Set<String> getIgnoreTableName() {
        return ignoreTableName == null ? new HashSet<>() : ignoreTableName;
    }
}
