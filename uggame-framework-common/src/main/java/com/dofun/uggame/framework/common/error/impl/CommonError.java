package com.dofun.uggame.framework.common.error.impl;

import com.dofun.uggame.framework.common.error.BaseError;

/**
 * 定义公共的一些错误码
 *
 * @author likejie
 * @version 1.0
 * @date 2019/4/15 14:19
 */
public enum CommonError implements BaseError {

    /***状态码*/
    UNAUTHORIZED(1002, "token过期，需要重新登录"),
    UNKNOWN_ERROR(99999, "未定义异常"),
    MISSING_PARAMETER(10000, "缺少参数"),
    PARAMETER_ERROR(10002, "非法参数"),
    PROPAGATION_ERROR(10006, ""),
    INTERNAL_SERVER_ERROR(10007, "内部服务请求异常"),
    ;

    private Integer code;
    private String message;

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
