package com.dofun.shenglilei.common.util;

import org.springframework.context.ApplicationContext;

public class EnvUtils {

    /**
     * 获取当前环境名称
     *
     * @param context
     * @return
     */
    public static String getEnvName(ApplicationContext context) {
        String envName = "开发";
        for (String a : context.getEnvironment().getActiveProfiles()) {
            if (a.equals("local") || a.equals("dev")) {
                return envName;
            } else if (a.equals("test")) {
                return "测试";
            } else if (a.equals("prod")) {
                return "生产";
            } else if (a.equals("preprod")) {
                return "预发布";
            }
        }
        return envName;
    }
}
