package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的个端口域名信息
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum EnvDomainEnum {




    /**
     * UGEsport，M站，开发环境
     */
    ugesport_websiteH5_dev("https://m-dev.bggameen.com/"),
    /**
     * UGEsport，M站，测试环境
     */
    ugesport_websiteH5_test("https://m-test.bggameen.com/"),
    /**
     * UGEsport，M站，测试环境
     */
    ugesport_websiteH5_prod("https://m.bggameen.com/"),
    ;


    /**
     * 域名
     */
    private final String domain;


    EnvDomainEnum(String domain) {
        this.domain = domain;
    }

    public static EnvDomainEnum forName(AppEnum appEnum, ReqEndPointEnum endpoint, String env) {
        if (StringUtils.isBlank(env)) {
            return null;
        }
        String name = appEnum.getName() + "_" + endpoint.getName() + "_" + env;
        for (EnvDomainEnum item : EnvDomainEnum.values()) {
            if (name.equals(item.name())) {
                return item;
            }
        }
        return null;
    }

    /**
     * 返回数据示例：https://m-test.uggame.com/vi_VN/
     *
     * @param countryId 国家Id
     */
    public String forCountryId(Integer countryId) {
        com.dofun.shenglilei.framework.common.enums.RegionEnum regionEnum = com.dofun.shenglilei.framework.common.enums.RegionEnum.forCountryId(countryId);
        if (regionEnum == null) {
            return null;
        }
        switch (regionEnum) {
            case VIETNAM:
                return this.getDomain() + "vi_VN" + "/";
            case PHILIPPINES:
                return this.getDomain() + "tl_PH" + "/";
            case MALAYSIA:
                return this.getDomain() + "ms_MY" + "/";
            case THAILAND:
                return this.getDomain() + "th_TH" + "/";
            case INDONESIA:
                return this.getDomain() + "id_ID" + "/";
            default:
                return null;
        }
    }
}
