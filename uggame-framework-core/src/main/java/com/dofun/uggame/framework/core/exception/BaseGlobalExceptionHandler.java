package com.dofun.uggame.framework.core.exception;

import brave.Span;
import brave.Tracer;
import com.dofun.uggame.framework.common.base.BaseResponseParam;
import com.dofun.uggame.framework.common.error.impl.CommonError;
import com.dofun.uggame.framework.common.exception.ApiInvokerException;
import com.dofun.uggame.framework.common.exception.BusinessException;
import com.dofun.uggame.framework.common.response.WebApiResponse;
import com.dofun.uggame.framework.core.utils.BeanValidatorUtils;
import com.dofun.uggame.framework.core.utils.ResponseUtil;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "global-exception", name = "enable", matchIfMissing = true, havingValue = "true")
public class BaseGlobalExceptionHandler {

    private static final String CONTENT_SPLIT_KEY = "content:";
    private static final String STATUS_SPLIT_KEY = "status ";

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

    /**
     * 这个异常不捕捉，传递给调用方，可以层层回传到起点
     */
    @ExceptionHandler(value = ApiInvokerException.class)
    public void apiInvokerExceptionHandler(HttpServletRequest req, HttpServletResponse response, ApiInvokerException e) {
        log.error("---ApiInvokerException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());

        ResponseUtil.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, WebApiResponse.error(e.getErrorCode(), e.getMessage()).toString());
    }

    @ExceptionHandler(value = {HystrixRuntimeException.class})
    public void hystrixRuntimeExceptionHandler(HystrixRuntimeException e, HttpServletRequest req, HttpServletResponse response) {
        log.error("---HystrixRuntimeException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.getMessage());

        log.error("HystrixRuntimeException detail:  failureType=" + e.getFailureType() + ",implementingClass=" + e.getImplementingClass());
        log.error("HystrixRuntimeException detail:  fallbackException=" + (e.getFallbackException() == null ? "null" : e.getFallbackException().getMessage()), (e.getFallbackException() == null ? "null" : e.getFallbackException()));
        Throwable throwable = e.getCause();
        if (throwable == null) {
            log.error("throwable is null,return default exception.");
            ResponseUtil.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, WebApiResponse.error(CommonError.PROPAGATION_ERROR.getCode(), e.getMessage()).toString());
            return;
        }
        String message = throwable.getMessage();
        if (message == null || message.isEmpty()) {
            log.error("message is null,return default exception.");
            ResponseUtil.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, WebApiResponse.error(CommonError.PROPAGATION_ERROR.getCode(), e.getMessage()).toString());
            return;
        }
        log.error("origin message:{}", message);
        int messageIndex = message.indexOf(CONTENT_SPLIT_KEY);
        if (messageIndex < 0) {
            log.error("messageIndex is " + messageIndex + " less than zero,return default exception.");
            ResponseUtil.writeErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, WebApiResponse.error(CommonError.PROPAGATION_ERROR.getCode(), e.getMessage()).toString());
            return;
        }
        int statusIndex = message.indexOf(STATUS_SPLIT_KEY);
        HttpStatus httpStatus;
        if (statusIndex < 0) {
            log.warn("statusIndex is " + statusIndex + " less than zero,set to default error status.");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            int beginIndex = statusIndex + STATUS_SPLIT_KEY.length();
            httpStatus = HttpStatus.valueOf(Integer.parseInt(message.substring(beginIndex, beginIndex + 3)));
        }
        if (httpStatus.is2xxSuccessful()) {
            log.warn("httpStatus is " + httpStatus.value() + ",should be error status.");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        message = message.substring(messageIndex + CONTENT_SPLIT_KEY.length() + 1);
        ResponseUtil.writeErrorMessage(response, httpStatus, message);
    }

    /**
     * 其它异常统一处理
     */
    @ExceptionHandler(value = Exception.class)
    public WebApiResponse<BaseResponseParam> defaultErrorHandler(HttpServletRequest req, HttpServletResponse response, Exception e) {
        log.error("---DefaultException Handler---Host {} invokes url {} ERROR: {}", req.getRemoteHost() + ":" + req.getRemotePort(), req.getRequestURL(), e.toString());

        return responseError(CommonError.UNKNOWN_ERROR.getCode(), e, response);
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
