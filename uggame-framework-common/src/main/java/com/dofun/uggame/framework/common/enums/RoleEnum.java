package com.dofun.uggame.framework.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的用户类型
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
public enum RoleEnum {
    /**
     * 号主
     */
    SELLER("seller"),

    /**
     * 租客
     */
    BUYER("buyer"),
    /**
     * 平台运营人员
     * <p>
     * 这部分会包含多种子角色(客服、财务、运营)，在业务层面去细分，不再统一处理了
     */
    MANAGER("manager"),
    /**
     * 超级管理员
     */
    SUPER_ADMIN("superAdmin"),
    /**
     * 内部系统使用
     * <p>
     * 例如从PHP发起的调用
     */
    INNER_CLIENT("innerClient"),
    /**
     * 第三方-外部系统使用
     * <p>
     * 例如Garena改密客户端，运行在Windows平台
     */
    THIRD_PARTY_CLIENT("thirdPartyClient"),
    ;

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public static boolean isExisted(String name) {
        return null != forName(name);
    }

    public static RoleEnum forName(String name) {
        for (RoleEnum endpoint : RoleEnum.values()) {
            String epName = endpoint.getName();
            if (StringUtils.equals(name, epName)) {
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
