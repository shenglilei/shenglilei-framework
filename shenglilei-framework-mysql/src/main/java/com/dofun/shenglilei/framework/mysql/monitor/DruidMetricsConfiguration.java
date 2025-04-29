/*
package com.dofun.shenglilei.framework.mysql.monitor;


import com.alibaba.druid.pool.DruidDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

*/
/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2022/4/12
 * Time:20:02
 *//*

@Configuration
@ConditionalOnClass({DruidDataSource.class, MeterRegistry.class})
@Slf4j
public class DruidMetricsConfiguration {
    @Resource
    private MeterRegistry registry;

    @Autowired
    public void bindMetricsRegistryToDruidDataSources(Collection<DataSource> dataSources) throws SQLException {
        List<DruidDataSource> druidDataSources = new ArrayList<>(dataSources.size());
        for (DataSource dataSource : dataSources) {
            DruidDataSource druidDataSource = dataSource.unwrap(DruidDataSource.class);
            if (druidDataSource != null) {
                druidDataSources.add(druidDataSource);
            }
        }
        DruidCollector druidCollector = new DruidCollector(druidDataSources, registry);
        druidCollector.register();
        log.info("finish register metrics to micrometer");
    }
}
*/
