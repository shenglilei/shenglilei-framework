package com.dofun.uggame.framework.mysql.configuration;

import com.dofun.uggame.framework.mysql.constants.MyBatisConstants;
import com.github.pagehelper.PageInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

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

    @Bean(name = "MySQLSQLSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("MySQLDataSource") DataSource dataSource,
                                                   @Qualifier("MySQLMapperHelper") MapperHelper mapperHelper) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // 设置mybatis XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(MyBatisConstants.MAPPER_XML));
        //配置通用mapper
        tk.mybatis.mapper.session.Configuration configuration = new tk.mybatis.mapper.session.Configuration();
        configuration.setMapperHelper(mapperHelper);
        bean.setConfiguration(configuration);
        //配置插件
        setPlugins(bean, pageInterceptor);
        log.info("mybatis plugins is set");
        SqlSessionFactory sqlSessionFactory = bean.getObject();
        //-自动使用驼峰命名属性映射字段   userId    user_id
        assert sqlSessionFactory != null;
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        log.info("MySQLSQLSessionFactory is ready to inject.");
        return sqlSessionFactory;
    }

    private void setPlugins(SqlSessionFactoryBean bean, Interceptor... interceptors) {
        if (interceptors == null || interceptors.length == 0) {
            return;
        }
        List<Interceptor> result = new ArrayList<>(interceptors.length);
        for (Interceptor interceptor : interceptors) {
            if (interceptor != null) {
                result.add(interceptor);
            }
        }
        if (result.isEmpty()) {
            return;
        }
        Interceptor[] adds = new Interceptor[result.size()];
        adds = result.toArray(adds);
        bean.setPlugins(adds);
    }

    /**
     * 通用mapper，配置说明详见https://github.com/abel533/Mapper/wiki/3.config
     */
    @Bean(name = "MySQLMapperHelper")
    public MapperHelper mapperHelper() {
        Config config = new Config();
        config.setIDENTITY("MYSQL");
        config.setNotEmpty(false);
        config.setOrder("AFTER");
        config.setEnableMethodAnnotation(true);
        config.setCheckExampleEntityClass(true);
        config.setUseSimpleType(true);
        config.setEnumAsSimpleType(true);
        config.setWrapKeyword("`{0}`");
        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.setConfig(config);
        log.info("tk.mybatis.mapper is ready to inject.");
        return mapperHelper;
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
