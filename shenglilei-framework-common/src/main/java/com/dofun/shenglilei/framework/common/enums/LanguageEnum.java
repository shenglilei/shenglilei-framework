package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的用户的语言
 * <p>
 * 0-简体中文，1-繁体中文，2-英语，3=越南语
 * <p>
 * zh-cn,zh-tw,en-us,vi-vn
 * <p>
 * https://www.cnblogs.com/Robert-huge/p/5481515.html
 * <p>
 * Created with IntelliJ IDEA.
 * author: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum LanguageEnum {
    /**
     * 简体中文
     */
    ZH_CN(0, "zh-cn", "简体中文"),
    /**
     * 繁体中文（台湾）
     */
    ZH_TW(1, "zh-tw", "繁体中文(台湾)"),
    /**
     * 英语(美国)
     */
    EN_US(2, "en-us", "英语"),
    /**
     * 越南语
     */
    VI_VN(3, "vi-vn", "越南语"),
    /**
     * 印尼语
     */
    ID_ID(4, "id-id", "印尼语"),
    /**
     * 泰语
     */
    TH_TH(5, "th-th", "泰语"),
    /**
     * 马来语
     */
    MS_MY(6, "ms-my", "马来语"),
    /**
     * 菲律宾语
     */
    TL_PH(7, "tl-ph", "菲律宾语"),
    /**
     * 繁体中文(香港)
     */
    ZH_HK(8, "zh-hk", "繁体中文(香港)"),
    /**
     * 繁体中文(澳门)
     */
    ZH_MO(9, "zh-mo", "繁体中文(澳门)"),
    /**
     * 印度(印地语)
     */
    HI_IN(10, "hi-in", "印地语"),

    /**
     * 巴西(葡萄牙语)
     */
    PT_BR(11, "pt-br", "葡萄牙语"),
    /**
     * 墨西哥(西班牙语)
     */
    ES_MX(12, "es-mx", "西班牙语"),

    /**
     * 孟加拉(孟加拉语)
     */
    BN_BD(13, "bn-bd", "孟加拉语"),
    ;

    private final Integer id;

    private final String code;

    private final String desc;

    LanguageEnum(Integer id, String code, String desc) {
        this.id = id;
        this.code = code;
        this.desc = desc;
    }

    public static LanguageEnum forId(Integer id) {
        if (id == null) {
            return null;
        }
        for (LanguageEnum item : LanguageEnum.values()) {
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public static LanguageEnum forCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (LanguageEnum item : LanguageEnum.values()) {
            if (StringUtils.equals(item.getCode(), code)) {
                return item;
            }
        }
        return null;
    }


    public boolean equals(Integer id) {
        return this.getId().equals(id);
    }
}
