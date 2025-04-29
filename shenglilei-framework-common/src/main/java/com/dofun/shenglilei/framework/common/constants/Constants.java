package com.dofun.shenglilei.framework.common.constants;

import com.dofun.shenglilei.framework.common.enums.TimezoneEnum;
import com.dofun.shenglilei.framework.common.utils.TimezoneUtil;
import org.springframework.http.MediaType;

/**
 * 系统常量
 */
public class Constants {
    /**
     * 印度尼西亚-雅加达 时区，比北京时间晚一小时整
     * * 示例
     * * <p>
     * * 北京时间：2021-09-26 20:08:41
     * * 印度尼西亚-雅加达时间：2021-09-26 19:08:41
     */
    public static final String IDN_JAKARTA_DEFAULT_CONFIG_TIMEZONE = TimezoneEnum.WIB.getTimezoneId();


    /**
     * 越南-胡志明 时区，比北京时间晚一小时整
     * 示例
     * <p>
     * 北京时间：2021-09-26 20:08:41
     * 越南-胡志明时间：2021-09-26 19:08:41
     */
    public static final String VI_HO_CHI_MINH_DEFAULT_CONFIG_TIMEZONE = TimezoneEnum.ICT.getTimezoneId();
    /**
     * 系统默认时区：取操作系统时区
     */
    public static final String SYSTEM_DEFAULT_CONFIG_TIMEZONE = VI_HO_CHI_MINH_DEFAULT_CONFIG_TIMEZONE;

    public static final String SYSTEM_DEFAULT_CONFIG_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String SYSTEM_DEFAULT_PACKAGE_ROOT = "com.dofun";

    public static final String REQUEST_HEADER_KEY_PREFIX = "i-";

    public static final String RESPONSE_HEADER_KEY_TRACE_ID = "traceId";

    public static final String RESPONSE_HEADER_KEY_SPAN_ID = "spanId";

    public static final String RESPONSE_HEADER_VALUE_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    public static final String MDC_KEY_TRACE_ID = RESPONSE_HEADER_KEY_TRACE_ID;

    public static final String MDC_KEY_SPAN_ID = RESPONSE_HEADER_KEY_SPAN_ID;

    public static final String MDC_KEY_TIMEZONE = "timezone";
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
    public static final String BASEURL = "/api/v1/";

    static {
        TimezoneUtil.set(SYSTEM_DEFAULT_CONFIG_TIMEZONE);
    }
}
