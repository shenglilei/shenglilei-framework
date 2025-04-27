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
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum LanguageEnum {
    /**
     * 简体中文
     */
    ZH_CN(0, "zh-cn", "简体中文"),
//    ZH_HK(1, "zh-hk", "繁体中文(香港)"),
    /**
     * 繁体中文（台湾）
     */
    ZH_TW(1, "zh-hk", "繁体中文(台湾)"),
    /**
     * 英语(美国)
     */
    EN_US(2, "en-us", "英语"),
    /**
     * 越南语
     */
    VI_VN(3, "vi-vn", "越南语"),
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
