package com.dofun.uggame.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * IP地址操作类
 */
public class IpUtils {

    public static String clientIp(Map<String, String> requestHeaders, String remoteAddr) {
        String ip = requestHeaders.get("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = requestHeaders.get("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return remoteAddr;
    }

}
