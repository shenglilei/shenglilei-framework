package com.dofun.uggame.framework.common.error.impl;

import com.dofun.uggame.framework.common.error.BaseError;

public enum CommonError implements BaseError {
    UN_AUTHORIZED(1000, "身份认证失败"),
    NO_PERMISSION(1001, "没有访问权限"),
    MISSING_PARAMETER(1002, "缺少参数"),
    ILLEGAL_PARAMETER(1003, "非法参数"),

    SYSTEM_ERROR(2000, "系统异常"),

    BUSINESS_ERROR(3000, "业务异常"),

    UNKNOWN_ERROR(99999, "未定义异常"),
    ;

    private final Integer code;

    private final String message;

    CommonError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return toString(getCode(), getMessage());
    }
}
