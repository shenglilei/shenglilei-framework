package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:15:39
 */
@Getter
public enum RequestParamHeaderEnum {
    ROLE("i-role", true),
    COUNTRY_Id("i-countryId", true),
    COUNTRY_Code("i-countryCode", true),
    LANGUAGE_ID("i-languageId", true),
    REQ_END_POINT("i-reqEndPoint", true),
    DEVICE_INFO("i-deviceInfo", true),
    USER_ID("i-userId", false),
    ;

    private final String name;

    /**
     * 取值是从token生成时指定的
     * <p>
     * 设置是否可以被外部http header覆盖
     * <p>
     * 如果设置为true，则表示生成token时的值，与后续调用接口的值，可能不一样
     */
    private final Boolean canReplace;

    RequestParamHeaderEnum(String name, Boolean canReplace) {
        this.name = name;
        this.canReplace = canReplace;
    }

    public static RequestParamHeaderEnum forName(String name) {
        if (name == null) {
            return null;
        }
        for (RequestParamHeaderEnum item : RequestParamHeaderEnum.values()) {
            if (name.equals(item.getName())) {
                return item;
            }
        }
        return null;
    }
}


