package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.dofun.shenglilei.framework.common.constants.Constants.REQUEST_HEADER_KEY_PREFIX;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:15:39
 */
@Getter
public enum RequestParamHeaderEnum {
    //不允许切换角色访问，如有需要，请重新登录，拿到新的登陆凭证
    ROLE("role", false),
    COUNTRY_Id("countryId", true),
    COUNTRY_Code("countryCode", true),
    LANGUAGE_ID("languageId", true),
    //不允许切换使用端口访问，如有需要，请重新登录，拿到新的登陆凭证,
    REQ_END_POINT("reqEndPoint", false),
    APP_Id("appId", false),
    ADD_FROM("addFromHead", false),
    APP_VERSION("appVersion", false),
    OS_VERSION("osVersion", false),
    DEVICE_INFO("deviceInfo", true),
    IP("ip", true),
    //不允许切换userId
    USER_ID("userId", false),
    ;

    private final String name;

    /**
     * 取值是从token生成时指定的
     * <p>
     * 设置是否可以被外部http header覆盖
     * <p>
     * 如果设置为true，则表示生成token时的值，与后续调用接口的值，可能不一样
     */
    private final Boolean canReplace;

    RequestParamHeaderEnum(String name, Boolean canReplace) {
        this.name = name;
        this.canReplace = canReplace;
    }

    @Deprecated
    public static RequestParamHeaderEnum forName(String name) {
        return forHeaderName(name);
    }

    public static RequestParamHeaderEnum forHeaderName(String headerName) {
        if (!StringUtils.hasLength(headerName)) {
            return null;
        }
        //此处必须要用：equalsIgnoreCase
        //HTTP报头的名称是不区分大小写，根据RFC 2616： 每个标题字段由一个名字后跟一个冒号（“：”）和字段值组成。 字段名称不区分大小写。
        for (RequestParamHeaderEnum item : RequestParamHeaderEnum.values()) {
            if (headerName.equalsIgnoreCase(item.getHeaderName())) {
                return item;
            }
        }
        return null;
    }

    public static RequestParamHeaderEnum forFieldName(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        for (RequestParamHeaderEnum item : RequestParamHeaderEnum.values()) {
            if (fieldName.equals(item.getFieldName())) {
                return item;
            }
        }
        return null;
    }

    public static Map<String, RequestParamHeaderEnum> getFieldNames() {
        Map<String, RequestParamHeaderEnum> result = new HashMap<>(RequestParamHeaderEnum.values().length);
        for (RequestParamHeaderEnum item : RequestParamHeaderEnum.values()) {
            result.put(item.getFieldName(), item);
        }
        return result;
    }

    public static Map<String, RequestParamHeaderEnum> getHeaderNames() {
        Map<String, RequestParamHeaderEnum> result = new HashMap<>(RequestParamHeaderEnum.values().length);
        for (RequestParamHeaderEnum item : RequestParamHeaderEnum.values()) {
            result.put(item.getHeaderName(), item);
        }
        return result;
    }

    public Boolean getCanReplace() {
        return canReplace;
    }

    @Deprecated
    public String getName() {
        return getHeaderName();
    }

    /**
     * Http header name
     */
    public String getHeaderName() {
        return REQUEST_HEADER_KEY_PREFIX + name;
    }

    /**
     * BaseRequestParam field name
     */
    public String getFieldName() {
        return name;
    }
}


