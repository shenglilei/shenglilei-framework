package com.dofun.shenglilei.framework.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/10/21
 * Time:15:43
 */
public class JacksonUtil {
    public static ObjectMapper getObjectMapper() {
//        SimpleDateFormat format = new SimpleDateFormat(SYSTEM_DEFAULT_CONFIG_DATETIME_PATTERN);
//        TimeZone timeZone = TimeZone.getTimeZone(SYSTEM_DEFAULT_CONFIG_TIMEZONE);
//        format.setTimeZone(timeZone);
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().timeZone(timeZone).dateFormat(format);
//        return builder.build();
        return new ObjectMapper();
    }
}
