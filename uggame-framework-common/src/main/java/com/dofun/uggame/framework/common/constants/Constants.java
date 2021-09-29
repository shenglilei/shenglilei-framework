package com.dofun.uggame.framework.common.constants;

import org.springframework.http.MediaType;

/**
 * 系统常量
 */
public class Constants {
    /**
     * 系统默认时区：越南时区，比北京时间晚一小时整
     * 示例
     * <p>
     * 北京时间：2021-09-26 20:08:41
     * 越南时间：2021-09-26 19:08:41
     */
    public static final String SYSTEM_DEFAULT_CONFIG_TIMEZONE = "Asia/Ho_Chi_Minh";

    public static final String SYSTEM_DEFAULT_PACKAGE_ROOT = "com.dofun.uggame";

    public static final String RESPONSE_HEADER_KEY_TRACE_ID = "traceId";

    public static final String RESPONSE_HEADER_VALUE_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;
    /**
     * 当前应用的名称
     */
    public static String APPLICATION_NAME;

    /**
     * 当前应用的访问入口
     * <p>
     * 示例：BASEURL="/"，入口为：http://ip:port/swagger-ui.html
     * <p>
     * 示例：BASEURL="/api/v1"，入口为：http://ip:port/api/v1/swagger-ui.html
     */
    public static String BASEURL;
}
