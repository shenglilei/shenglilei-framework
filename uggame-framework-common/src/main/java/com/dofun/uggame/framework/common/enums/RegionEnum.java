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
     * 香港
     */
    HONG_KONG(104, 852, 1, "hong kong SAR"),
    /**
     * 越南
     */
    VIETNAM(251, 84, 3, "vietnam"),
    /**
     * 马来西亚
     */
    MALAYSIA(167, 60, 2, "malaysia"),
    /**
     * 菲律宾
     */
    PHILIPPINES(201, 63, 2, "philippines"),

    /**
     * 新加坡
     */
    SINGAPORE(215, 65, 2, "singapore"),

    /**
     * 泰国
     */
    THAILAND(227, 66, 2, "thailand"),

    /**
     * 印度尼西亚
     */
    INDONESIA(111, 62, 2, "indonesia"),
    ;

    /**
     * 国家Id：zhw_country.code
     * <p>
     * 45-中国，237-台湾，104-香港，215-新加坡，167-马来西亚，251-越南
     */
    private final Integer countryId;
    /**
     * 国家区号:：zhw_country.cy_code
     */
    private final Integer countryCode;

    /**
     * 语言Id
     * <p>
     * 详细定义见：com.dofun.uggame.framework.common.enums.LanguageEnum
     * <p>
     * 0-简体中文，1-繁体中文，2-英语，3=越南语
     */
    private final Integer languageId;

    /**
     * 国家名称
     */
    private final String countryName;

    RegionEnum(Integer countryId, Integer countryCode, Integer languageId, String countryName) {
        this.countryId = countryId;
        this.countryCode = countryCode;
        this.languageId = languageId;
        this.countryName = countryName;
    }

    public static RegionEnum forCountryId(Integer countryId) {
        if (countryId == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            if (countryId.equals(item.getCountryId())) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum forCountryCode(Integer countryCode) {
        if (countryCode == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            Integer code = item.getCountryCode();
            if (countryCode.equals(code)) {
                return item;
            }
        }
        return null;
    }


    public static RegionEnum forLanguageId(Integer languageId) {
        if (languageId == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            if (languageId.equals(item.getLanguageId())) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum forCountryName(String countryName) {
        if (StringUtils.isBlank(countryName)) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            String name = item.getCountryName();
            if (StringUtils.equals(countryName, name)) {
                return item;
            }
        }
        return null;
    }


    public boolean equals(String countryName) {
        return this.getCountryName().equals(countryName);
    }
}
