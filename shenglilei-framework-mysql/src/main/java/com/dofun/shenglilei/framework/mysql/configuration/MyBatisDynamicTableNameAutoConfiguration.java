package com.dofun.shenglilei.framework.mysql.configuration;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.dofun.shenglilei.framework.common.tenant.TenantInfoHolder;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameMode;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.impl.DynamicTableNameByEnumCountryIdResolver;
import com.dofun.shenglilei.framework.mysql.properties.DynamicTableNameProperties;
import lombok.extern.slf4j.Slf4j;
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
@EnableConfigurationProperties({DynamicTableNameProperties.class})
@ConditionalOnProperty(prefix = DynamicTableNameProperties.PREFIX, name = {"enabled"}, havingValue = "true")
public class MyBatisDynamicTableNameAutoConfiguration {
    /**
     * 租户ID管理器
     */
    @Resource
    private TenantInfoHolder tenantInfoHolder;

    @Resource
    private DynamicTableNameProperties dynamicTableNameProperties;

    @Resource
    private DynamicTableNameMode dynamicTableNameMode;

    @Bean("MySQLDynamicTableNameInterceptor")
    public MybatisPlusInterceptor paginationInterceptor() {
        log.debug("dynamicTableNameProperties：{}", JSON.toJSONString(dynamicTableNameProperties));
        MybatisPlusInterceptor dynamicTableNameInterceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            log.debug("开始处理动态表名，原始表名称：{}", tableName);
            if (this.dynamicTableNameProperties == null || !this.dynamicTableNameProperties.isEnabled()) {
                log.debug("结束处理动态表名，现在的表名称：{}，功能配置为关闭", tableName);
                return tableName;
            }
            if (StringUtils.isEmpty(tableName)) {
                log.debug("结束处理动态表名，现在的表名称：{}，表名称为空", tableName);
                return tableName;
            }
            // 处理特殊字符
            String originTabledName = tableName;
            tableName = tableName.replaceAll("`", "");
            tableName = tableName.replaceAll(" ", "");
            log.debug("tableName replaced：{}  ->  {}", originTabledName, tableName);

            DynamicTableNameMode.Mode mode = this.dynamicTableNameProperties.getTableConfig().get(tableName);
            if (mode == null) {
                log.debug("结束处理动态表名，现在的表名称：{}，配置的处理模式无效", tableName);
                return tableName;
            }
            log.debug("匹配处理模式成功，表名称：{}，处理模式:{}", tableName, mode.name());
            String newTableName = this.dynamicTableNameMode.getResolver(mode).resolve(tableName);
            log.debug("结束处理动态表名，表名称：{} -> {}，处理成功", tableName, newTableName);
            return newTableName;
        });
        dynamicTableNameInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        log.info("MySQLDynamicTableNameInterceptor is ready to inject.");
        return dynamicTableNameInterceptor;
    }

    @Bean
    public DynamicTableNameMode createDynamicTableNameMode() {
        DynamicTableNameMode dynamicTableNameMode = new DynamicTableNameMode();
        dynamicTableNameMode.setDynamicTableNameByEnumCountryIdResolver(new DynamicTableNameByEnumCountryIdResolver(this.tenantInfoHolder));
        return dynamicTableNameMode;
    }
}
