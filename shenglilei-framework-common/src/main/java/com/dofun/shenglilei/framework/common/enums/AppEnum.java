package com.dofun.shenglilei.framework.common.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的客户端产品
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
public enum AppEnum {


    UGGame("uggame", "uggame App"),
    /**
     * UGesport
     * <p>
     * UGGame Android/iOS 马甲包
     */
    UGEsport("ugesport", "ugesport App"),
    /**
     * UGBox
     * <p>
     * 盲盒 Android/iOS
     */
    UGBox("ugbox", "ugbox App"),
    /**
     * HappySkin
     * <p>
     * UGGame Android 马甲包
     */
    HappySkin("happyskin", "HappySkin App"),
    /**
     * efun
     * <p>
     * UGGame Android 马甲包
     */
    Efun("efun", "Efun App"),
    /**
     * AccEval
     * <p>
     * UGGame iOS 马甲包
     */
    AccEval("acceval", "AccEval App"),

    XmFun("xmfun", "XmFun App"),
    /**
     * 国际版
     */
    TopAcc("topAcc", "topAcc App"),
    ;

    private final String name;
    private final String desc;

    AppEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static boolean isExisted(String name) {
        return null != forName(name);
    }

    public static AppEnum forName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (AppEnum endpoint : AppEnum.values()) {
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
        for (AppEnum endpoint : AppEnum.values()) {
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
