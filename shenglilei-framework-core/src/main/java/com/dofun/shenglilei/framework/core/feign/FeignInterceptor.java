package com.dofun.shenglilei.framework.core.feign;

import com.dofun.shenglilei.framework.common.enums.RequestParamHeaderEnum;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Slf4j
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //将languageId公共参数透传给下游服务
        Map<String, Collection<String>> headMap = requestTemplate.headers();
        Arrays.stream(RequestParamHeaderEnum.values()).filter(requestParamHeaderEnum -> RequestParamHeaderEnum.LANGUAGE_ID.name().equals(requestParamHeaderEnum.name())).forEach(item -> {
            String oldValue = headMap.getOrDefault(item.getHeaderName(), new ArrayList<>(1)).toString();
            String newValue = MDC.get(item.getFieldName());
            if (StringUtils.isNotBlank(newValue)) {
                requestTemplate.header(item.getHeaderName(), newValue);
                log.debug("Inner microservice method invoke , changed header value [{}] {}  --->  {}", item.getHeaderName(), oldValue, newValue);
            } else {
                if (headMap.containsKey(item.getHeaderName())) {
                    requestTemplate.removeHeader(item.getHeaderName());
                    log.debug("Inner microservice method invoke , removed header value [{}] {}  --->  {}", item.getHeaderName(), oldValue, newValue);
                }
            }
        });
    }
}
