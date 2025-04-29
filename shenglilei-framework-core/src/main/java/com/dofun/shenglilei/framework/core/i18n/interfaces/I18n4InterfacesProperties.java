package com.dofun.shenglilei.framework.core.i18n.interfaces;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.enums.LanguageEnum;
import com.dofun.shenglilei.framework.common.enums.RequestParamHeaderEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 接口出参的多语言输出配置
 */
@RefreshScope
@Data
@Configuration
@ConfigurationProperties(prefix = "i18n.interfaces.error.msg")
@Slf4j
public class I18n4InterfacesProperties {

    private List<ErrorCodeItem> configs;

    @PostConstruct
    public void init() {
        log.debug("i18n.interfaces.error.msg: {}", JSON.toJSONString(this.getConfigs()));
    }

    public String getErrorMessage(Integer errorCode) {
        // 不指定languageId，由当前上下文languageId决定，适用于绝大多数场景
        return getErrorMessage(errorCode, null);
    }

    public String getErrorMessage(Integer errorCode, Integer languageId) {
        // 指定languageId，优先级高于当前上下文languageId，适用于定时任务或手动指定语言场景
        List<ErrorCodeItem> list = this.getConfigs();
        if (errorCode == null || list == null || list.isEmpty()) {
            log.debug("errorCode: {}", errorCode);
            return null;
        }
        log.debug("errorCode: {}", errorCode);
        LanguageEnum languageEnum = LanguageEnum.forId(languageId);
        String languageIdString = languageEnum != null ? String.valueOf(languageEnum.getId()) : MDC.get(RequestParamHeaderEnum.LANGUAGE_ID.getFieldName());
        log.debug("languageIdString: {}", languageIdString);
        if (StringUtils.isBlank(languageIdString) || !StringUtils.isNumeric(languageIdString)) {
            return null;
        }
        List<LanguageItem> languageItems = null;
        for (ErrorCodeItem item : list) {
            if (item.getErrorCode() != null && item.getErrorCode().toString().equals(errorCode.toString())) {
                languageItems = item.getConfigs();
                break;
            }
        }
        if (languageItems == null) {
            return null;
        }
        for (LanguageItem item : languageItems) {
            if (item.getLanguageId() != null && item.getLanguageId().toString().equals(languageIdString)) {
                return item.getMessage();
            }
        }
        return null;
    }

    @Data
    public static class ErrorCodeItem {
        Integer errorCode;
        List<LanguageItem> configs;
    }

    @Data
    public static class LanguageItem {
        Integer languageId;
        String message;
    }
}
