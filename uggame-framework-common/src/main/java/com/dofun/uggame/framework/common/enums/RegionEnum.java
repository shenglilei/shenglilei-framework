package com.dofun.uggame.framework.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的用户所在地区
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum RegionEnum {
    /**
     * 越南
     */
    VIETNAM(3, "vietnam", 84),
//    /**
//     * 印度尼西亚
//     */
//    INDONESIA("indonesia",62),
//    /**
//     * 马来西亚
//     */
//    MALAYSIA("malaysia",60),
//    /**
//     * 泰国
//     */
//    THAILAND("thailand",66),
    ;

    /**
     * 国家Id
     */
    private final Integer countryId;
    /**
     * 国家名称
     */
    private final String countryName;
    /**
     * 国家区号
     */
    private final Integer countryCode;

    RegionEnum(Integer countryId, String countryName, Integer countryCode) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public static boolean isExisted(String countryName) {
        return null != forName(countryName);
    }

    public static RegionEnum forId(Integer countryId) {
        for (RegionEnum item : RegionEnum.values()) {
            Integer id = item.getCountryCode();
            if (countryId.equals(id)) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum forName(String countryName) {
        for (RegionEnum item : RegionEnum.values()) {
            String name = item.getCountryName();
            if (StringUtils.equals(countryName, name)) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum forCode(Integer countryCode) {
        for (RegionEnum item : RegionEnum.values()) {
            Integer code = item.getCountryCode();
            if (countryCode.equals(code)) {
                return item;
            }
        }
        return null;
    }

    public boolean equals(String countryName) {
        return this.getCountryName().equals(countryName);
    }
}
