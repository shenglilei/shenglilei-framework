package com.dofun.shenglilei.framework.core.feign;

import com.dofun.shenglilei.framework.common.base.BaseResponseParam;
import com.dofun.shenglilei.framework.common.exception.BusinessException;
import com.dofun.shenglilei.framework.common.response.WebApiResponse;
import com.dofun.shenglilei.framework.common.utils.JacksonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/10/14
 * Time:21:05
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                ObjectMapper mapper = JacksonUtil.getObjectMapper();
                String originResponseString = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
                log.debug("originResponseString:{}", originResponseString);
                WebApiResponse<BaseResponseParam> webApiResponse = mapper.readValue(originResponseString, new TypeReference<WebApiResponse<BaseResponseParam>>() {
                });
                return new BusinessException(webApiResponse.getErrcode(), webApiResponse.getMsg());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return FeignException.errorStatus(methodKey, response);
    }
}
