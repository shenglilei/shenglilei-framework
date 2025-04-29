package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的时区
 * <p>
 * http://www.shijian.cc/shiqu/
 * <p>
 * Created with IntelliJ IDEA.
 * 以东七区为基准  offset 为 60 是 东八区   offset 为 -60 是 东六区 单位是分钟
 * User: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum TimezoneEnum {

    /**
     * http://www.shijian.cc/105/1102/
     */
    WIB(0, "UTC+700", "Asia/Jakarta", "印尼西部时间", "印尼-雅加达", 0),
    /**
     * http://www.shijian.cc/152/1557/
     */
    ICT(1, "UTC+700", "Asia/Ho_Chi_Minh", "印度支那时间", "越南-胡志明市", 0),
    /**
     * http://www.shijian.cc/116/1304/
     */
    CST(2, "UTC+800", "Asia/Shanghai", "中国标准时间", "中国-上海市", 60),

    /**
     * http://www.shijian.cc/139/1404/
     */
    HKT(3, "UTC+800", "Asia/Hong_Kong", "香港时间", "中国-香港", 60),

    /**
     * http://www.shijian.cc/121/1282/
     */
    MYT(4, "UTC+800", "Asia/Kuala_Lumpur", "马来西亚时间", "马来西亚-吉隆坡", 60),

    /**
     * http://www.shijian.cc/137/1386/
     */
    PHT(5, "UTC+800", "Asia/Manila", "菲律宾时间", "菲律宾-马尼拉", 60),

    /**
     * http://www.shijian.cc/112/1137/
     */
    SGT(6, "UTC+800", "Asia/Singapore", "新加坡时间", "新加坡-新加坡", 60),

    /**
     * http://www.shijian.cc/106/1055/
     */
    ICT2(7, "UTC+700", "Asia/Bangkok", "印度支那时间", "泰国-曼谷", 0),
    /**
     * http://www.shijian.cc/106/1055/
     */
    INT(8, "UTC+550", "Asia/Kolkata", "印度时间", "印度-加尔各答", (-90)),

    /**
     * http://www.shijian.cc/106/1055/
     */
    BRT(9, "UTC-300", "America/Sao_Paulo", "巴西时间", "巴西-巴西利亚", (-600)),

    /**
     * https://www.zeitverschiebung.net/cn/all-time-zones.html
     */
    CDT(10, "UTC-600", "America/Mexico_City", "墨西哥时间", "墨西哥-墨西哥城", (-780)),

    /**
     * https://www.zeitverschiebung.net/cn/all-time-zones.html
     */
    DKT(11, "UTC+600", "Asia/Dhaka", "孟加拉时间", "墨西哥-达卡", (-60)),


    ;

    private final Integer id;

    private final String code;

    private final String timezoneId;

    private final String timezoneName;

    private final String region;

    private final Integer offset;


    TimezoneEnum(Integer id, String code, String timezoneId, String timezoneName, String region, Integer offset) {
        this.id = id;
        this.code = code;
        this.timezoneId = timezoneId;
        this.timezoneName = timezoneName;
        this.region = region;
        this.offset = offset;
    }

    public static TimezoneEnum forId(Integer id) {
        if (id == null) {
            return null;
        }
        for (TimezoneEnum item : TimezoneEnum.values()) {
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public static TimezoneEnum forCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (TimezoneEnum item : TimezoneEnum.values()) {
            if (StringUtils.equals(item.getCode(), code)) {
                return item;
            }
        }
        return null;
    }


    public boolean equals(Integer id) {
        return this.getId().equals(id);
    }
}
