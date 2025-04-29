package com.dofun.shenglilei.framework.mysql.configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.dofun.shenglilei.framework.mysql.constants.MyBatisConstants;
import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@ConditionalOnResource(resources = {MyBatisConstants.MAPPER_ROOT_PATH})
@ConditionalOnClass(value = {SqlSessionFactory.class})
public class MyBatisAutoConfiguration {

    @Resource
    @Qualifier("MySQLPageInterceptor")
    private PageInterceptor pageInterceptor;

    @Autowired(required = false)
    @Qualifier("MySQLTenantInterceptor")
    private MybatisPlusInterceptor tenantInterceptor;

    @Autowired(required = false)
    @Qualifier("MySQLDynamicTableNameInterceptor")
    private MybatisPlusInterceptor dynamicTableNameInterceptor;

    @Bean(name = "MySQLSQLSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("MySQLDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置mybatis XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(MyBatisConstants.MAPPER_XML));
        //配置插件
        setPlugins(bean, pageInterceptor, tenantInterceptor, dynamicTableNameInterceptor);
        log.info("mybatis plugins is set");
        SqlSessionFactory sqlSessionFactory = bean.getObject();
        //-自动使用驼峰命名属性映射字段   userId    user_id
        assert sqlSessionFactory != null;
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        log.info("MySQLSQLSessionFactory is ready to inject.");
        return sqlSessionFactory;
    }

    private void setPlugins(MybatisSqlSessionFactoryBean bean, Interceptor... interceptors) {
        if (interceptors == null || interceptors.length == 0) {
            return;
        }
        List<Interceptor> result = new ArrayList<>(interceptors.length);
        for (Interceptor interceptor : interceptors) {
            if (interceptor != null) {
                result.add(interceptor);
                log.debug("插件加载完成：{}", interceptor.getClass().toString());
            }
        }
        if (result.isEmpty()) {
            return;
        }
        Interceptor[] adds = new Interceptor[result.size()];
        adds = result.toArray(adds);
        bean.setPlugins(adds);
    }

    @Bean(name = "MySQLDataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("MySQLDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "MySQLSqlSessionTemplateForFramework")
    @ConditionalOnMissingBean(value = SqlSessionTemplate.class, name = {"sqlSessionTemplateForFramework", "SqlSessionTemplateForFramework"})
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("MySQLSQLSessionFactory") SqlSessionFactory sqlSessionFactory) {
        log.info("MySQLSqlSessionTemplateForFramework is ready to inject.");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
