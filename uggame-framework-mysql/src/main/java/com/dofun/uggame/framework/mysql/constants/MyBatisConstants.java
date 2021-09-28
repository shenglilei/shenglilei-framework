package com.dofun.uggame.framework.mysql.constants;

import static com.dofun.uggame.framework.common.Constants.SYSTEM_DEFAULT_PACKAGE_ROOT;

/**
 * MyBatis常量
 */
public class MyBatisConstants {
    public static final String MAPPER_ROOT_PATH = "classpath:mapper";
    public static final String MAPPER_XML = "classpath:mapper/*.xml";
    /***MyBatis 配置**/
    public static String MAPPER_BASE_PACKAGE = SYSTEM_DEFAULT_PACKAGE_ROOT+".*.mapper;"+SYSTEM_DEFAULT_PACKAGE_ROOT+".*.*.mapper";
}
