package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 定义使用系统的用户所在地区
 * <p>
 * Created with IntelliJ IDEA.
 * author: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum RegionEnum {
    /**
     * 中国-大陆
     */
    CHINA(45, 86, "china", LanguageEnum.ZH_CN.getId(), TimezoneEnum.HKT.getId(), CurrencyEnum.CNY.getId(), null, "CN", "中国-大陆", null, null, null, null, null),

    /**
     * 中国-香港
     */
    HONG_KONG(104, 852, "hong kong SAR", LanguageEnum.ZH_HK.getId(), TimezoneEnum.HKT.getId(), CurrencyEnum.HKD.getId(), null, "HK", "中国-香港", null, null, null, null, null),

    /**
     * 中国-澳门
     */
    MACAO(151, 853, "macao SAR", LanguageEnum.ZH_MO.getId(), TimezoneEnum.HKT.getId(), CurrencyEnum.MOP.getId(), null, "MO", "中国-澳门", null, null, null, null, null),

    /**
     * 中国-台湾
     */
    TAIWAN(237, 886, "taiwan", LanguageEnum.ZH_TW.getId(), TimezoneEnum.HKT.getId(), CurrencyEnum.TWD.getId(), 51L, "TW", "中国-台湾", 4201, 300006L, 53L, 55L, 54L),
    /**
     * 越南
     */
    VIETNAM(251, 84, "vietnam", LanguageEnum.VI_VN.getId(), TimezoneEnum.ICT.getId(), CurrencyEnum.VND.getId(), 1L, "VN", "越南", 4301, 300001L, 3L, 5L, 4L),
    /**
     * 马来西亚
     */
    MALAYSIA(167, 60, "malaysia", LanguageEnum.MS_MY.getId(), TimezoneEnum.MYT.getId(), CurrencyEnum.MYR.getId(), 31L, "MY", "马来西亚", 4601, 300005L, 33L, 35L, 34L),
    /**
     * 菲律宾
     */
    PHILIPPINES(201, 63, "philippines", LanguageEnum.TL_PH.getId(), TimezoneEnum.PHT.getId(), CurrencyEnum.PHP.getId(), 41L, "PH", "菲律宾", 4701, 300004L, 43L, 45L, 44L),

    /**
     * 新加坡
     */
    SINGAPORE(215, 65, "singapore", LanguageEnum.EN_US.getId(), TimezoneEnum.SGT.getId(), CurrencyEnum.SGD.getId(), null, "SGP", "新加坡", null, null, null, null, null),

    /**
     * 泰国
     */
    THAILAND(227, 66, "thailand", LanguageEnum.TH_TH.getId(), TimezoneEnum.ICT2.getId(), CurrencyEnum.THB.getId(), 21L, "TH", "泰国", 4501, 300003L, 23L, 25L, 24L),

    /**
     * 印度尼西亚
     */
    INDONESIA(111, 62, "indonesia", LanguageEnum.ID_ID.getId(), TimezoneEnum.WIB.getId(), CurrencyEnum.IDR.getId(), 11L, "ID", "印度尼西亚", 4401, 300002L, 13L, 15L, 14L),

    /**
     * 印度
     */
    INDIA(113, 91, "india", LanguageEnum.HI_IN.getId(), TimezoneEnum.INT.getId(), CurrencyEnum.INR.getId(), 61L, "IN", "印度", 41001, 300007L, 63L, 65L, 64L),


    /**
     * 巴西
     */
    BRAZIL(32, 55, "brazil", LanguageEnum.PT_BR.getId(), TimezoneEnum.BRT.getId(), CurrencyEnum.BRL.getId(), 71L, "BR", "巴西", 41101, 300008L, 73L, 75L, 74L),

    /**
     * 墨西哥52    孟加拉国880
     */
    MEXICO(166, 52, "mexico", LanguageEnum.ES_MX.getId(), TimezoneEnum.CDT.getId(), CurrencyEnum.MXN.getId(), 81L, "MX", "墨西哥", 41201, 300009L, 83L, 85L, 84L),


    /**
     * 孟加拉国880
     */
    BANGLADESH(23, 880, "bangladesh", LanguageEnum.BN_BD.getId(), TimezoneEnum.DKT.getId(), CurrencyEnum.BDT.getId(), 91L, "BD", "孟加拉", 41301, 300010L, 93L, 95L, 94L),


    ;

    /**
     * 国家Id：zhw_country.code
     * <p>
     * 45-中国，237-台湾，104-香港，215-新加坡，167-马来西亚，251-越南
     */
    private final Integer countryId;
    /**
     * 国家区号:：zhw_country.cy_code
     */
    private final Integer countryCode;

    /**
     * 语言Id
     * <p>
     * <p>
     * 0-简体中文，1-繁体中文，2-英语，3=越南语
     */
    private final Integer languageId;

    /**
     * 时区Id
     * <p>
     * 详细定义见：com.dofun.uggame.framework.common.enums.TimezoneEnum     * <p>
     * 0-GMT+800，1-繁体中文，2-英语，3=越南语
     */
    private final Integer timezoneId;

    /**
     * 货币Id
     * <p>
     * 详细定义见：CurrencyEnum
     * <p>
     * 0-CNY，1-HKD，2-GBP，3=VND
     */
    private final Integer currencyId;

    /**
     * 国家名称
     */
    private final String countryName;

    /**
     * 系统用户ID（财务收款账号，用户Id）
     */
    private final Long sysUserId;

    /**
     * 金额中转用户Id）
     */
    private final Long transferUserId;

    /**

     */
    private final Long blindBoxUserId;

    /**
     * 幸运币系统用户(ugbox专用)
     */
    private final Long luckyCoinUserId;

    /**
     * 代充资金用户
     */
    private final Long replaceRechargeUserId;

    /**
     * 简称：VN ID TH
     */
    private final String alias;

    /**
     * 国家中文名
     */
    private final String nameZh;

    /**
     * 默认 addFrom
     */
    private final Integer addFrom;

    RegionEnum(Integer countryId, Integer countryCode, String countryName, Integer languageId, Integer timezoneId, Integer currencyId, Long sysUserId, String alias, String nameZh, Integer
            addFrom, Long transferUserId, Long blindBoxUserId, Long luckyCoinUserId, Long replaceRechargeUserId) {
        this.countryId = countryId;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.languageId = languageId;
        this.timezoneId = timezoneId;
        this.currencyId = currencyId;
        this.sysUserId = sysUserId;
        this.alias = alias;
        this.nameZh = nameZh;
        this.addFrom = addFrom;
        this.transferUserId = transferUserId;
        this.blindBoxUserId = blindBoxUserId;
        this.luckyCoinUserId = luckyCoinUserId;
        this.replaceRechargeUserId = replaceRechargeUserId;
    }

    public static RegionEnum forCountryId(Integer countryId) {
        if (countryId == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            if (countryId.equals(item.getCountryId())) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum forCurrencyId(Integer currencyId) {
        if (currencyId == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            if (currencyId.equals(item.getCurrencyId())) {
                return item;
            }
        }
        return null;
    }

    public static Set<Integer> getAllCountryId() {
        Set<Integer> result = new HashSet<>();
        for (RegionEnum item : RegionEnum.values()) {
            result.add(item.getCountryId());
        }
        return result;
    }

    public static RegionEnum forCountryCode(Integer countryCode) {
        if (countryCode == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            Integer code = item.getCountryCode();
            if (countryCode.equals(code)) {
                return item;
            }
        }
        return null;
    }


    public static RegionEnum forLanguageId(Integer languageId) {
        if (languageId == null) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            if (languageId.equals(item.getLanguageId())) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum forCountryName(String countryName) {
        if (StringUtils.isBlank(countryName)) {
            return null;
        }
        for (RegionEnum item : RegionEnum.values()) {
            String name = item.getCountryName();
            if (StringUtils.equals(countryName, name)) {
                return item;
            }
        }
        return null;
    }


    public boolean equals(String countryName) {
        return this.getCountryName().equals(countryName);
    }
}
