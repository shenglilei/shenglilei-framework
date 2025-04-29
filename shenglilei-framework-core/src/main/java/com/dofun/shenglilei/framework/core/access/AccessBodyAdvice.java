package com.dofun.shenglilei.framework.core.access;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.tenant.TenantInfoHolder;
import com.dofun.shenglilei.framework.common.utils.TimezoneUtil;
import com.dofun.shenglilei.framework.core.trace.TraceAdviceService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

import static com.dofun.shenglilei.framework.common.constants.Constants.*;

/**
 * 扩展springmvc的入参，针对@RequestBody的参数做拦截处理；
 * 这里必须使用@ControllerAdvice注解，因为RequestMappingHandlerAdapter在执行initControllerAdviceCache()初始化的时候，只会把标注了@ControllerAdvice注解的类扫描进来
 *
 * @author chenfanglin
 * @date: 2018年11月14日下午2:10:25
 */
@Slf4j
@ControllerAdvice
public class AccessBodyAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    Tracer tracer;

    @Autowired
    private TraceAdviceService traceAdviceService;

    @Autowired
    private com.dofun.shenglilei.framework.core.access.AccessParamService accessParamService;

    @Autowired
    private TenantInfoHolder tenantInfoHolder;

    /**
     * 返回true表示启动拦截
     */
    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 把网关放在请求头中的数据封装到BaseRequestParam中,子类继承这个类就能取到网关请求头传过来的数据
     */
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        //设置时区
        MDC.put(MDC_KEY_TIMEZONE, TimezoneUtil.get());
        //设置trace、span
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        String spanId = Objects.requireNonNull(tracer.currentSpan()).context().spanId();
        if (Objects.requireNonNull(tracer.currentSpan()).context() != null) {
            MDC.put(MDC_KEY_TRACE_ID, traceId);
            MDC.put(MDC_KEY_SPAN_ID, spanId);
        }
        if (body instanceof BaseRequestParam) {
            BaseRequestParam requestParam = (BaseRequestParam) body;
            HttpHeaders httpHeaders = inputMessage.getHeaders();
            log.debug("请求参数处理之前 {} \n{}", requestParam.getClass().getSimpleName(), JSON.toJSONString(requestParam));
            accessParamService.setAccessParam(requestParam, httpHeaders);
            traceAdviceService.setCurrentTrace(requestParam);
            tenantInfoHolder.setCurrentTenantId(requestParam);
            TimezoneUtil.set(requestParam);
            log.debug("请求参数处理之后 {} \n{}", requestParam.getClass().getSimpleName(), JSON.toJSONString(requestParam));
        }
        return body;
    }
}
