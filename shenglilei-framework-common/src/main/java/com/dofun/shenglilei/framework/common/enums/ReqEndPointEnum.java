package com.dofun.shenglilei.framework.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的客户端类型
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
public enum ReqEndPointEnum {
    /**
     * Windows客户端-租客使用
     */
    WINDOWS_CLIENT("windowsClient"),
    /**
     * PC端-号主使用
     */
    WEBSITE("website"),
    /**
     * PC管理后台-平台运营使用
     */
    WEBSITE_MANAGER("website2Manager"),
    /**
     * Android APP端-租客使用
     */
    ANDROID_APP("androidApp"),
    /**
     * Android 内嵌的H5页面-租客使用
     */
    ANDROID_H5("androidH5"),
    /**
     * 内部系统使用
     * <p>
     * 从PHP发起的调用
     */
    INNER_SYSTEM_CLIENT_PHP("innerSystemClientPHP"),

    /**
     * 内部系统使用
     * <p>
     * 从Nodejs发起的调用
     */
    INNER_SYSTEM_CLIENT_NODEJS("innerSystemClientNodejs"),
    /**
     * 内部微服务使用
     * <p>
     * 例如从service-usercenter微服务发起的调用
     */
    INNER_MICRO_SERVICE("innerMicroService"),
    /**
     * 第三方-外部系统使用
     * <p>
     * 例如Garena改密客户端，运行在Windows平台
     */
    THIRD_PARTY_SYSTEM_CLIENT("thirdPartySystemClient"),
    ;

    private final String name;

    ReqEndPointEnum(String name) {
        this.name = name;
    }

    public static boolean isExisted(String name) {
        return null != forName(name);
    }

    public static ReqEndPointEnum forName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ReqEndPointEnum endpoint : ReqEndPointEnum.values()) {
            if (StringUtils.equals(name, endpoint.getName())) {
                return endpoint;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }
}
