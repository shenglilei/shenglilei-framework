package com.dofun.shenglilei.framework.common.utils;

import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.dofun.shenglilei.framework.common.constants.Constants.MDC_KEY_TIMEZONE;
import static com.dofun.shenglilei.framework.common.constants.Constants.SYSTEM_DEFAULT_CONFIG_DATETIME_PATTERN;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/26
 * Time:19:31
 */
@Slf4j
public class TimezoneUtil {
    public static void set(BaseRequestParam requestParam) {
        //多线程频繁切换时区会存在问题，timezone会大量缓存（数据库线程池），暂时不支持切换了
        /*
         if (requestParam == null) {
         return;
         }
         RegionEnum regionEnum = RegionEnum.forCountryId(requestParam.getCountryId());
         if (regionEnum == null) {
         return;
         }
         set(TimezoneEnum.forId(regionEnum.getTimezoneId()).getTimezoneId());
         */

        MDC.put(MDC_KEY_TIMEZONE, get());
    }

    public static void set(String timezoneId) {
        String key = "user.timezone";
        String oldValue = System.setProperty(key, timezoneId);
        log.info("{} value changed,{}  ->  {}", key, oldValue, timezoneId);
        log.info("{} value {}", key, System.getProperty(key));
        log.info("系统目前时区为：{},现在时间是：{}", get(), now());
        if (get().equals(timezoneId)) {
            log.debug("系统目前时区为：{},不需要修改时区,现在时间是：{}", get(), now());
            return;
        }
        //设置为指定的时区
        TimeZone.setDefault(TimeZone.getTimeZone(timezoneId));
        log.info("系统目前时区为：{},修改时区成功,现在时间是：{}", get(), now());
        MDC.put(MDC_KEY_TIMEZONE, get());
    }

    public static String get() {
        return TimeZone.getDefault().getID();
    }

    public static Date nowDate() {
        return DateTime.now(DateTimeZone.forTimeZone(TimeZone.getDefault())).toDate();
    }

    public static String now() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SYSTEM_DEFAULT_CONFIG_DATETIME_PATTERN);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(nowDate());
    }
}
