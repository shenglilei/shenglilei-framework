package com.dofun.shenglilei.framework.core.access;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.tenant.TenantInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
public class CommonAccessInterceptor implements HandlerInterceptor {
    @Autowired
    private AccessParamService accessParamService;

    @Autowired
    private TenantInfoHolder tenantInfoHolder;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        log.debug("client:{},url:{},method:{},contentType:{}", request.getRemoteAddr(), request.getRequestURI(), request.getMethod(), request.getContentType());
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            log.debug("header key:{},value:{}", key, JSON.toJSONString(request.getHeaders(key)));
        }
        String contentType = request.getContentType();
        if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            if (StringUtils.isNotBlank(contentType) && contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE)) {
                log.debug("ignored process by not support request.");
                return true;
            }
        }
        BaseRequestParam requestParam = new BaseRequestParam();
        accessParamService.setAccessParam(requestParam, request);
        // get请求和form请求
        log.debug("success process.");
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        tenantInfoHolder.clearCurrentTenantId();
        MDC.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
