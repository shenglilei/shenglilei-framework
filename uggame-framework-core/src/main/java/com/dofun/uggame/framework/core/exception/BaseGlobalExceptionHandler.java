package com.dofun.uggame.framework.core.exception;

import brave.Span;
import brave.Tracer;
import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.dofun.uggame.framework.common.exception.BusinessException;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.framework.core.utils.BeanValidatorUtils;
import com.dofun.uggame.framework.core.utils.ResponseUtil;
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
import javax.servlet.ServletException;
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

    @ExceptionHandler(value = BusinessException.class)
    public WebApiResponse<BaseResponseParam> businessExceptionHandler(HttpServletRequest req, BusinessException e) {
        log.error("---businessException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());

        Throwable throwable = e.getCause();
        if (throwable instanceof BindException) {
            BindException bindException = (BindException) throwable;
            BindingResult bindingResult = bindException.getBindingResult();
            String errorMsg = BeanValidatorUtils.getErrorMsg(bindingResult);
            return WebApiResponse.error(CommonError.PARAMETER_ERROR.getCode(), "参数校验错误: " + errorMsg);
        }
        return WebApiResponse.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = ServletException.class)
    public WebApiResponse<BaseResponseParam> servletExceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
        log.error("---ServletException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());

        return responseError(CommonError.UNKNOWN_ERROR.getCode(), e, response);
    }

    /**
     * hibernate validater验证错误
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public WebApiResponse<BaseResponseParam> paramNotValidErrorHandler(HttpServletRequest req, Exception e) {
        log.error("---MethodArgumentNotValidException | BindException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());

        BindingResult bindingResult;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            bindingResult = methodArgumentNotValidException.getBindingResult();
        } else {
            BindException bindException = (BindException) e;
            bindingResult = bindException.getBindingResult();
        }
        String errorMsg = BeanValidatorUtils.getErrorMsg(bindingResult);

        return WebApiResponse.error(CommonError.PARAMETER_ERROR.getCode(), "参数校验错误: " + errorMsg);
    }

    private WebApiResponse<BaseResponseParam> responseError(int errorCode, Exception e, HttpServletResponse response) {
        String errorMsg;
        if (errorVisible) {
            errorMsg = e.getMessage();
        } else {
            errorMsg = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        Span span = tracer.currentSpan();
        if (span != null) {
            ResponseUtil.trace(response, span.context().traceIdString());
        }
        return WebApiResponse.error(errorCode, errorMsg);
    }
}
