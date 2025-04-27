package com.dofun.uggame.framework.core.access;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CommonAccessInterceptor implements HandlerInterceptor {
    @Autowired
    private AccessParamService accessParamService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler)
            throws Exception {
        String contentType = request.getContentType();
        if (HttpMethod.POST.name().equals(request.getMethod())) {
            if (StringUtils.isNotBlank(contentType) && contentType.toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE)) {
                return true;
            }
        }
        BaseRequestParam requestParam = new BaseRequestParam();
        accessParamService.setAccessParam(requestParam, request);
        // get请求和form请求
        request.setAttribute("uggameRequestParam", requestParam);
        return true;
    }
}
