package com.dofun.uggame.framework.core.exception;

import brave.Span;
import brave.Tracer;
import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.dofun.uggame.framework.common.exception.BusinessException;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.framework.core.utils.BeanValidatorUtils;
import com.dofun.uggame.framework.core.utils.ResponseUtil;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class BaseGlobalExceptionHandler {

    @SuppressWarnings("all")
    @Autowired
    private Tracer tracer;

    @Value("${server.error.visible:true}")
    private Boolean errorVisible;

    @PostConstruct
    public void init() {
        log.info("BaseGlobalExceptionHandler is ready to inject");
    }

    /**
     * 业务逻辑错误，转换为自定义结构
     */
    @ExceptionHandler(value = BusinessException.class)
    public WebApiResponse<BaseResponseParam> businessExceptionHandler(HttpServletRequest req, HttpServletResponse response, BusinessException e) {
        log.error("---BusinessException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());
        return responseError(e.getErrorCode(), e.getMessage(), response);
    }

    /**
     * Feign远程调用的错误，原路返回
     */
    @ExceptionHandler(value = FeignException.class)
    public void feignExceptionHandler(HttpServletRequest req, HttpServletResponse response, FeignException e) {
        log.error("---FeignException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());
        Span span = tracer.currentSpan();
        if (span != null) {
            ResponseUtil.trace(response, span.context().traceIdString());
        }
    }

    /**
     * 其它异常，转换为自定义结构
     */
    @ExceptionHandler(value = Throwable.class)
    public WebApiResponse<BaseResponseParam> defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Throwable e) {
        log.error("---DefaultException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());
        return responseError(CommonError.UNKNOWN_ERROR.getCode(), CommonError.UNKNOWN_ERROR.getMessage() + ": " + e.getMessage(), response);
    }

    /**
     * 参数校验错误，转换为自定义结构
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class})
    public WebApiResponse<BaseResponseParam> paramNotValidErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
        log.error("---{} Handler---Host {} invokes url {} ERROR: {}", e.getClass().getSimpleName(), req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());
        BindingResult bindingResult;
        String errorMsg = e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            bindingResult = methodArgumentNotValidException.getBindingResult();
            errorMsg = BeanValidatorUtils.getErrorMsg(bindingResult);
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            bindingResult = bindException.getBindingResult();
            errorMsg = BeanValidatorUtils.getErrorMsg(bindingResult);
        } else if (e instanceof IllegalArgumentException) {
            errorMsg = e.getMessage();
        }
        return responseError(CommonError.ILLEGAL_PARAMETER.getCode(), CommonError.ILLEGAL_PARAMETER.getMessage() + ": " + errorMsg, response);
    }

    private WebApiResponse<BaseResponseParam> responseError(int errorCode, String errorMsg, HttpServletResponse response) {
        if (!errorVisible) {
            errorMsg = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        Span span = tracer.currentSpan();
        if (span != null) {
            ResponseUtil.trace(response, span.context().traceIdString());
        }
        return WebApiResponse.error(errorCode, errorMsg);
    }
}
