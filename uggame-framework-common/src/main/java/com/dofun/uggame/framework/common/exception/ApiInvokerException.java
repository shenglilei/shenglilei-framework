package com.dofun.uggame.framework.common.exception;

import com.dofun.uggame.framework.common.error.BaseError;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API:异常基类
 * 微服务内部RPC调用，在提供方产生，传递到调用方。
 *
 * @author 邹伟
 * @date 2018年4月2日
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiInvokerException extends RuntimeException {

    /***错误码*/
    private Integer errorCode;

    public ApiInvokerException(String message) {
        super(message);
        this.errorCode = CommonError.PROPAGATION_ERROR.getCode();
    }

    public ApiInvokerException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.errorCode = CommonError.PROPAGATION_ERROR.getCode();
    }

    public ApiInvokerException(BaseError error) {
        super(error.getMessage());
        this.errorCode = error.getCode();
    }

    public ApiInvokerException(Integer erorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = erorCode;
    }


}