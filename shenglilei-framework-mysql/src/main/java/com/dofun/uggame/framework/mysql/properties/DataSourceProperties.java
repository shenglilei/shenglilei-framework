package com.dofun.uggame.framework.mysql.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = DataSourceProperties.DS)
public class DataSourceProperties {
    final static String DS = "spring.datasource";

    private String url;
    private String username;
    private String password;
    private String database;
    private String driverClassName;
    private int initialSize = 10;
    private int minIdle = 1;
    private int maxIdle = 1;
    private int maxActive = 10;
    private int maxWait = 100;
    private int timeBetweenEvictionRunsMillis = 60 * 1000;
    private int minEvictableIdleTimeMillis = 60 * 1000;
    private String validationQuery;
    private boolean testWhileIdle = true;
    private boolean testOnBorrow = true;
    private boolean testOnReturn = true;
    private boolean poolPreparedStatements;
    private int maxOpenPreparedStatements;
    private String filters;
    private String mapperLocations;
    private String typeAliasPackage;
    private int queryTimeout = 0;
}
