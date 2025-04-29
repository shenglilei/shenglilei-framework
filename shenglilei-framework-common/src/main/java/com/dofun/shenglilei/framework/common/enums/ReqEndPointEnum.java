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
    WINDOWS_CLIENT("windowsClient", "Windows客户端"),
    /**
     * PC端-号主使用
     */
    WEBSITE("website", "PCWeb"),
    /**
     * PC管理后台-平台运营使用
     */
    WEBSITE_MANAGER("website2Manager", "PC管理后台"),
    /**
     * H5页面
     */
    WEBSITE_H5("websiteH5", "M站"),
    /**
     * Android APP端-租客使用
     */
    ANDROID_APP("androidApp", "androidApp"),
    /**
     * ios APP端-租客使用
     */
    IOS_APP("iosApp", "iosAPP"),
    /**
     * Android 内嵌的H5页面-租客使用
     */
    ANDROID_H5("androidH5", "APP内嵌H5"),
    /**
     * 上号器
     */
    HIJACKER_V1("HIJACKERV1", "上号器"),
    /**
     * 内部系统使用
     * <p>
     * 从PHP发起的调用
     */
    INNER_SYSTEM_CLIENT_PHP("innerSystemClientPHP", "PHP"),
    /**
     * 普罗米修斯
     */
    PROMETHEUS("prometheus", "普罗米修斯"),
    /**
     * Python
     */
    INNER_SYSTEM_CLIENT_PYTHON("innerSystemClientPython", "Python"),
    /**
     * 内部系统使用
     * <p>
     * 从Nodejs发起的调用
     */
    INNER_SYSTEM_CLIENT_NODEJS("innerSystemClientNodejs", "Nodejs"),
    /**
     * 内部微服务使用
     * <p>
     * 例如从service-usercenter微服务发起的调用
     */
    INNER_MICRO_SERVICE("innerMicroService", "内部微服务"),
    /**
     * 第三方-外部系统使用
     * <p>
     * 例如Garena改密客户端，运行在Windows平台
     */
    THIRD_PARTY_SYSTEM_CLIENT("thirdPartySystemClient", "第三方"),
    ;

    private final String name;
    private final String desc;

    ReqEndPointEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
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

    public static String getDescByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ReqEndPointEnum endpoint : ReqEndPointEnum.values()) {
            if (StringUtils.equals(name, endpoint.getName())) {
                return endpoint.getDesc();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }
}
