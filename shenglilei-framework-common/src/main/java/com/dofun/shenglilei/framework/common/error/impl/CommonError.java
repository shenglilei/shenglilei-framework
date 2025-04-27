package com.dofun.shenglilei.framework.common.error.impl;

import com.dofun.shenglilei.framework.common.error.BaseError;

public enum CommonError implements BaseError {
    //------------------1000-1999  定义基本的通用错误----------------------------
    UN_AUTHORIZED(1000, "身份认证失败"),
    NO_PERMISSION(1001, "没有访问权限"),
    MISSING_PARAMETER(1002, "缺少参数"),
    ILLEGAL_PARAMETER(1003, "非法参数"),

    //------------------2000-2999  定义系统层面的错误，包含:jvm、中间件、磁盘、网络等----------------------------
    SYSTEM_ERROR(2000, "系统异常"),
    DATABASE_SYSTEM_ERROR(2001, "数据库异常"),
    CACHE_SYSTEM_ERROR(2002, "缓存异常"),
    MQ_SYSTEM_ERROR(2003, "消息队列异常"),
    NETWORK_SYSTEM_ERROR(2004, "网络通讯异常"),

    //------------------3000-3999  定义通用的业务异常，比如手机号码重复、手机号码格式不对等等----------------------------
    BUSINESS_ERROR(3000, "业务异常"),

    //------------------99999 未定义异常----------------------------
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
