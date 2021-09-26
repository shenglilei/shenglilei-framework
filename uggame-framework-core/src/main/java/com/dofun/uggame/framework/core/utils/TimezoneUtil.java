package com.dofun.uggame.framework.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/26
 * Time:19:31
 */
@Slf4j
public class TimezoneUtil {
    public static void set(String timezoneId) {
        log.info("系统目前默认时区为：{},现在时间是：{}", TimeZone.getDefault().getID(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //设置为指定的时区
        TimeZone timeZone = TimeZone.getTimeZone(timezoneId);
        log.info("修改时区为：{}", timeZone.getID());
        TimeZone.setDefault(timeZone);
        log.info("系统目前默认时区为：{},现在时间是：{}", TimeZone.getDefault().getID(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    }
}
