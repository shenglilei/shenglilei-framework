package com.dofun.uggame.framework.common.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:15:39
 */
public enum RequestParamHeaderEnum {
    ROLE("i-role"),
    REGION("i-region"),
    END_POINT("i-endPoint"),
    DEVICE_INFO("i-deviceInfo"),
    USER_ID("i-userId"),
    ;

    private final String name;

    RequestParamHeaderEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


