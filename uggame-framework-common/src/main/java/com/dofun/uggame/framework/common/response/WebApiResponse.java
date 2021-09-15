package com.dofun.uggame.framework.common.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.error.BaseError;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;


/**
 * api接口响应类
 */
@Data
@Slf4j
@ApiModel(value = "WebApiResponse", description = "api接口响应对象")
public class WebApiResponse<T extends BaseResponseParam> implements Serializable {
    public static final Integer SUCCESS_STATUS = 1;
    public static final Integer FAILED_STATUS = 0;
    public static final Integer SUCCESS_ERRORCODE = 200;

    private static final long serialVersionUID = -6557898536448299915L;
    /*** 响应状态 */
    @ApiModelProperty(value = "响应状态：0=失败；1=成功")
    private Integer status;
    /*** 响应代码 */
    @ApiModelProperty(value = "响应代码")
    private Integer errcode;
    /*** 响应消息 */
    @ApiModelProperty(value = "响应消息")
    private String msg;
    /*** 响应数据 */
    @ApiModelProperty(value = "响应数据")
    private T data;

    WebApiResponse() {

    }

    public static <T extends BaseResponseParam> WebApiResponse<T> success() {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(SUCCESS_STATUS);
        response.setErrcode(SUCCESS_ERRORCODE);
        response.setMsg("success");
        return response;
    }

    public static <T extends BaseResponseParam> WebApiResponse<T> success(T data) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(SUCCESS_STATUS);
        response.setErrcode(SUCCESS_ERRORCODE);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static <T extends BaseResponseParam> WebApiResponse<T> success(T data, String message) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(SUCCESS_STATUS);
        response.setErrcode(SUCCESS_ERRORCODE);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    /**
     * 返回错误消息（请使用此方法）
     */
    public static <T extends BaseResponseParam> WebApiResponse<T> error(BaseError error) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(FAILED_STATUS);
        response.setErrcode(error.getCode());
        response.setMsg(error.getMessage());
        return response;
    }

    public static <T extends BaseResponseParam> WebApiResponse<T> error(String message) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(FAILED_STATUS);
        response.setErrcode(CommonError.UNKNOWN_ERROR.getCode());
        response.setMsg(message);
        return response;
    }

    public static <T extends BaseResponseParam> WebApiResponse<T> error(Integer code, String message) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(FAILED_STATUS);
        response.setErrcode(code);
        response.setMsg(message);
        return response;
    }

    public static <T extends BaseResponseParam> WebApiResponse<T> error(Integer code, String message, T data) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setStatus(FAILED_STATUS);
        response.setErrcode(code);
        response.setMsg(message);
        response.setData(data);
        return response;
    }

    /**
     * 参数缺失
     */
    public static <T extends BaseResponseParam> WebApiResponse<T> missingParamsError(String message) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setErrcode(CommonError.MISSING_PARAMETER.getCode());
        response.setMsg(message);
        return response;
    }

    /**
     * 参数无效
     */
    public static <T extends BaseResponseParam> WebApiResponse<T> paramsError(String message) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setErrcode(CommonError.PARAMETER_ERROR.getCode());
        response.setMsg(message);
        return response;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isSuccess() {
        return this.getStatus().equals(SUCCESS_STATUS) && this.getErrcode().equals(SUCCESS_ERRORCODE);
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isSuccessAndHasContent() {
        return isSuccess()&&getData()!=null;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return JSON.toJSONString(this);
        }
    }
}


