package com.dofun.uggame.framework.mysql.configuration;

import com.dofun.uggame.framework.mysql.constants.MyBatisConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

@Slf4j
@ConditionalOnResource(resources = {MyBatisConstants.MAPPER_ROOT_PATH})
@ConditionalOnClass({SqlSessionFactory.class})
public class MapperScannerAutoConfiguration {

    @Bean(name = "MySQLMapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("MySQLSQLSessionFactory");
        // 设置mybatis mapper接口目录
        mapperScannerConfigurer.setBasePackage(MyBatisConstants.MAPPER_BASE_PACKAGE);
        log.info("MySQLMapperScannerConfigurer is ready to inject.");
        return mapperScannerConfigurer;
    }

}
