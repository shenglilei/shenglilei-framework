package com.dofun.uggame.framework.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的用户所在地区
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
public enum RegionEnum {
    /**
     * 越南
     */
    VIETNAM("vietnam"),
    /**
     * 印度尼西亚
     */
    INDONESIA("indonesia"),
    /**
     * 马来西亚
     */
    MALAYSIA("malaysia"),
    /**
     * 泰国
     */
    THAILAND("thailand"),
    ;

    private final String name;

    RegionEnum(String name) {
        this.name = name;
    }

    public static boolean isExisted(String name) {
        return null != forName(name);
    }

    public static RegionEnum forName(String name) {
        for (RegionEnum endpoint : RegionEnum.values()) {
            String epName = endpoint.getName();
            if (StringUtils.equals(name, epName)) {
                return endpoint;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }
}
