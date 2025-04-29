package com.dofun.shenglilei.framework.mysql.configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.dofun.shenglilei.framework.mysql.properties.DataSourceProperties;
import com.mysql.jdbc.JDBC4Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/11
 * Time:10:13
 */
@Slf4j
@Configuration
@ConditionalOnClass({JDBC4Connection.class})
@EnableTransactionManagement
@EnableConfigurationProperties({DataSourceProperties.class})
@Import({MyBatisAutoConfiguration.class, MapperScannerAutoConfiguration.class})
public class MySQLAutoConfiguration {

    @Resource
    private DataSourceProperties dataSourceProperties;

    @Bean(name = "MySQLDataSource", destroyMethod = "close", initMethod = "init")
    public DataSource druidDataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dataSourceProperties.getUrl());
        datasource.setUsername(dataSourceProperties.getUsername());
        datasource.setDbType(JdbcUtils.MYSQL);
        datasource.setPassword(dataSourceProperties.getPassword());
        datasource.setDriverClassName(dataSourceProperties.getDriverClassName());
        datasource.setInitialSize(dataSourceProperties.getInitialSize());
        datasource.setMinIdle(dataSourceProperties.getMinIdle());
        datasource.setMaxActive(dataSourceProperties.getMaxActive());
        datasource.setMaxWait(dataSourceProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dataSourceProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dataSourceProperties.getMinEvictableIdleTimeMillis());
        //开启心跳检测功能
        datasource.setKeepAlive(true);
        //配置心跳检测语句
        datasource.setValidationQuery(dataSourceProperties.getValidationQuery());
        datasource.setTestWhileIdle(dataSourceProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(dataSourceProperties.isTestOnBorrow());
        datasource.setTestOnReturn(dataSourceProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(false);
        datasource.setFilters("stat,wall,slf4j");
        datasource.setQueryTimeout(dataSourceProperties.getQueryTimeout());
        List<Filter> filters = datasource.getProxyFilters();
        for (Filter filter : filters) {
            if (filter instanceof WallFilter) {
                //修改允许执行批量更新SQL语句
                WallFilter wallFilter = (WallFilter) filter;
                wallFilter.setConfig(wallConfig());
                //刚开始引入WallFilter的时候，把logViolation设置为true，而throwException设置为false。就可以观察是否存在违规的情况，同时不影响业务运行。
                wallFilter.setLogViolation(true);
                wallFilter.setThrowException(false);
                log.debug("WallFilter is loaded.");
            }
            if (filter instanceof LogFilter) {
                LogFilter logFilter = (LogFilter) filter;
                //输出可执行的SQL
                logFilter.setStatementExecutableSqlLogEnable(true);
                logFilter.setStatementSqlPrettyFormat(true);
                log.debug("LogFilter is loaded.");
            }
            if (filter instanceof StatFilter) {
                StatFilter statFilter = (StatFilter) filter;
                statFilter.setMergeSql(true);
                //记录慢SQL
                statFilter.setLogSlowSql(true);
                statFilter.setSlowSqlMillis(1500);
                log.debug("StatFilter is loaded.");
            }
        }

        log.info("MySQL druid dataSource is ready to inject , datasource url: {}", dataSourceProperties.getUrl());
        return datasource;
    }


    @Bean(name = "ServletRegistrationBeanStatViewServlet")
    @ConditionalOnMissingBean(name = {"ServletRegistrationBeanStatViewServlet"})
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        //白名单：
        //servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的即提示:Sorry, you are not permitted to view this page.
        //servletRegistrationBean.addInitParameter("deny", "192.168.1.1");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "3ixbcf2jxlNdJDkK");
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        log.info("MySQLServletRegistrationBean is ready to inject.");
        return servletRegistrationBean;
    }

    //https://www.bookstack.cn/read/Druid/ffdd9118e6208531.md
    @Bean(name = "MySQLWallConfig")
    public WallConfig wallConfig() {
        WallConfig wallConfig = new WallConfig();
        wallConfig.setReplaceAllow(false);
        wallConfig.setCallAllow(false);
        wallConfig.setSetAllow(false);
        wallConfig.setTruncateAllow(true);
        wallConfig.setCreateTableAllow(false);
        wallConfig.setAlterTableAllow(false);
        wallConfig.setDropTableAllow(false);
        wallConfig.setCommentAllow(false);
        wallConfig.setMultiStatementAllow(false);
        wallConfig.setNoneBaseStatementAllow(false);
        wallConfig.setUseAllow(false);
        wallConfig.setDescribeAllow(false);
        wallConfig.setShowAllow(false);

        wallConfig.setDeleteWhereNoneCheck(true);
        wallConfig.setUpdateWhereNoneCheck(true);
        wallConfig.setConditionAndAlwayFalseAllow(true);
        wallConfig.setConditionAndAlwayTrueAllow(true);

        wallConfig.setConditionOpBitwseAllow(false);
        wallConfig.setConditionDoubleConstAllow(false);
        wallConfig.setMinusAllow(false);
        wallConfig.setIntersectAllow(false);
        /*
          这2行代码不能打开注释，会导致分页插件出问题
         wallConfig.setLimitZeroAllow(false);
         wallConfig.setSelectLimit(10 * 10000);
          */
        wallConfig.setMetadataAllow(true);
        wallConfig.setWrapAllow(false);

        return wallConfig;
    }
}
