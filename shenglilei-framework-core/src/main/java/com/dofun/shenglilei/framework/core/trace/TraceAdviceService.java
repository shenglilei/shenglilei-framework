package com.dofun.shenglilei.framework.core.trace;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.enums.RequestParamHeaderEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/10/19
 * Time:19:50
 */
@Slf4j
@Component
public class TraceAdviceService {

    public void setCurrentTrace(BaseRequestParam requestParam) {
        Map<String, RequestParamHeaderEnum> keys = RequestParamHeaderEnum.getFieldNames();
        JSON.parseObject(JSON.toJSONString(requestParam)).forEach((key, object) -> {
            if (StringUtils.isNotBlank(key) && keys.containsKey(key) && object != null) {
                key = keys.get(key).getFieldName();
                String oldValue = MDC.get(key);
                String newValue = object.toString();
                if (StringUtils.isNotBlank(oldValue)) {
                    if (!oldValue.equals(newValue)) {
                        log.debug("MDC {} value changed. {}  ->  {}", key, oldValue, newValue);
                        MDC.put(key, newValue);
                    }
                } else {
                    log.debug("MDC {} value created. null  ->  {}", key, newValue);
                    MDC.put(key, newValue);
                }
            }
        });
    }
}
