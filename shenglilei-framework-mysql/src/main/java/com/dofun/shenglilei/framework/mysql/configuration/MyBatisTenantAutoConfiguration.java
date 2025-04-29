package com.dofun.shenglilei.framework.mysql.configuration;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.dofun.shenglilei.framework.common.tenant.TenantInfoHolder;
import com.dofun.shenglilei.framework.mysql.properties.TenantProperties;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Slf4j
@Configuration
@ConditionalOnBean(MySQLAutoConfiguration.class)
@EnableConfigurationProperties({TenantProperties.class})
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = {"enabled"}, havingValue = "true")
public class MyBatisTenantAutoConfiguration {
    /**
     * 租户ID管理器
     */
    @Resource
    private TenantInfoHolder tenantInfoHolder;

    @Resource
    private TenantProperties tenantProperties;

    @Bean("MySQLTenantInterceptor")
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户插件
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(tenantInfoHolder.getCurrentTenantId());
            }

            @Override
            public String getTenantIdColumn() {
                return tenantProperties.getColumnName();
            }

            @Override
            public boolean ignoreTable(String tableName) {
                if (StringUtils.isEmpty(tableName)) {
                    return true;
                }
                // 处理特殊字符
                String originTabledName = tableName;
                tableName = tableName.replaceAll("`", "");
                tableName = tableName.replaceAll(" ", "");
                log.debug("tableName replaced：{}  ->  {}", originTabledName, tableName);

                Long currentTenantId = tenantInfoHolder.getCurrentTenantId();
                boolean flag = currentTenantId == null
                        || StringUtils.isEmpty(tenantProperties.getColumnName())
                        || StringUtils.isEmpty(tableName)
                        || tenantProperties.getIgnoreTableName().contains(tableName);
                if (flag) {
                    log.debug("数据表：{}，忽略执行租户字段处理，tenantProperties：{}", tableName, JSON.toJSONString(tenantProperties));
                } else {
                    log.debug("数据表：{}，可以执行租户字段处理，字段名：{}，租户Id：{}", tableName, tenantProperties.getColumnName(), currentTenantId);
                }
                return flag;
            }
        });
        interceptor.addInnerInterceptor(tenantInterceptor);
        log.info("MySQLTenantInterceptor is ready to inject.");
        return interceptor;
    }
}
