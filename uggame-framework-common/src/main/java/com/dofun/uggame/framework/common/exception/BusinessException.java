package com.dofun.uggame.framework.common.exception;

import com.dofun.uggame.framework.common.error.BaseError;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务逻辑异常，当不符合业务逻辑时抛出些异常再由接口统一返回
 *
 * @author Hujie
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    /***错误码*/
    private Integer errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = CommonError.UNKNOWN_ERROR.getCode();
    }

    public BusinessException(BaseError error) {
        super(error.getMessage());
        this.errorCode = error.getCode();
    }

    public BusinessException(Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.errorCode = CommonError.UNKNOWN_ERROR.getCode();
    }

    public BusinessException(Integer erorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = erorCode;
    }

}
