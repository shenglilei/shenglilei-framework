package com.dofun.shenglilei.framework.core.i18n.interfaces;

import com.dofun.shenglilei.framework.common.response.WebApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 接口出参的多语言输出处理器
 */
@Slf4j
public class I18n4InterfacesProcessor {
    private I18n4InterfacesProperties i18n4InterfacesProperties;

    public I18n4InterfacesProcessor(I18n4InterfacesProperties i18n4InterfacesProperties) {
        this.i18n4InterfacesProperties = i18n4InterfacesProperties;
    }

    public void translate(Object result) {
        if (result == null) {
            log.debug("result is null，ignored");
            return;
        }
        log.debug("开始-翻译接口错误信息");
        if (result instanceof WebApiResponse) {
            WebApiResponse<?> webApiResponse = (WebApiResponse<?>) result;
            if (webApiResponse.isFail() && webApiResponse.getErrcode() != null && !Objects.equals(webApiResponse.getErrcode(), WebApiResponse.SUCCESS_ERRORCODE)) {
                String oldMessage = webApiResponse.getMsg();
                if (StringUtils.isNotBlank(oldMessage)) {
                    log.debug("接口出参，message不符合翻译要求，已经有翻译文案了");
                    return;
                }
                log.debug("接口出参，message符合翻译要求，开始处理");
                log.debug("接口出参，message符合翻译要求，oldMessage: {}", oldMessage);
                String newMessage = this.i18n4InterfacesProperties.getErrorMessage(webApiResponse.getErrcode());
                log.debug("接口出参，message符合翻译要求，newMessage: {}", newMessage);
                if (StringUtils.isNotBlank(newMessage)) {
                    webApiResponse.setMsg(newMessage);
                }
                log.debug("接口出参，message符合翻译要求，结束处理");
            }
        }
        log.debug("完成-翻译接口错误信息");
    }
}
