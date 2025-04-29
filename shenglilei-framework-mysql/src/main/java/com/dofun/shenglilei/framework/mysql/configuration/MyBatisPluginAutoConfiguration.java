package com.dofun.shenglilei.framework.mysql.configuration;

import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Slf4j
@Configuration
@ConditionalOnBean(MySQLAutoConfiguration.class)
public class MyBatisPluginAutoConfiguration {
    /**
     * 分页插件pageHelper 配置
     */
    @Bean(name = "MySQLPageInterceptor")
    public PageInterceptor pageHelper() {
        Properties properties = new Properties();
        //reasonable：分页合理化参数，默认值为false。
        // 当该参数设置为 true 时，pageNum<=0 时会查询第一页
        // pageNum>pages（超过总数时），会查询最后一页。默认false 时，直接根据参数进行查询。
        properties.setProperty("reasonable", "false");
        //supportMethodsArguments：支持通过 Mapper 接口参数来传递分页参数，默认值false，
        // 分页插件会从查询方法的参数值中，自动根据上面 params 配置的字段中取值，查找到合适的值时就会自动分页。
        properties.setProperty("supportMethodsArguments", "true");
        //指定为MySQL数据库
        properties.setProperty("helperDialect", "mysql");
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(properties);
        log.info("MySQLPageInterceptor is ready to inject.");
        return pageInterceptor;
    }
}
