package com.dofun.shenglilei.framework.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义使用系统的货币
 * <p>
 * 0-CNY，1-HKD，2-GBP，3=VND
 * <p>
 * 详细信息：http://xh.5156edu.com/page/z8728m9608j20097.html
 * <p>
 * Created with IntelliJ IDEA.
 * author: Steven Cheng(成亮)
 * Date:2021/9/30
 * Time:13:58
 */
@Getter
public enum CurrencyEnum {
    /**
     * 人民币
     */
    CNY(0, "CNY", "元", "人民币", ","),
    /**
     * 港币
     */
    HKD(1, "HKD", "HKD", "港币", ","),
    /**
     * 英镑
     */
    GBP(2, "GBP", "￡", "英镑", ","),
    /**
     * 越南盾
     */
    VND(3, "VND", "₫", "越南盾", ","),
    /**
     * 印尼盾
     */
    IDR(4, "IDR", "Rp", "印尼盾", ","),
    /**
     * 马来西亚林吉特
     */
    MYR(5, "MYR", "RM", "马来西亚林吉特", ","),
    /**
     * 菲律宾比索
     */
    PHP(6, "PHP", "₱", "菲律宾比索", ","),
    /**
     * 新加坡元
     */
    SGD(7, "SGD", "S$", "新加坡元", ","),
    /**
     * 泰国铢
     */
    THB(8, "THB", "฿", "泰国铢", ","),
    /**
     * 美元
     */
    USD(9, "USD", "$", "美元", ","),
    /**
     * 新台币
     */
    TWD(10, "TWD", "NT$", "新台币", ","),
    /**
     * 澳门币
     */
    MOP(11, "MOP", "MOP$", "澳门币", ","),
    /**
     * 印度卢比
     */
    INR(12, "INR", "₹", "卢比", ","),
    /**
     * 巴西
     */
    BRL(13, "BRL", "R$", "巴西雷亚尔", ","),

    /**
     * 墨西哥
     */
    MXN(14, "MXN", "Mex$", "墨西哥比索", ","),

    /**
     * 孟加拉国
     */
    BDT(15, "BDT", "৳", "孟加拉塔卡", ","),

    ;

    /**
     * 货币Id
     */
    private final Integer id;

    /**
     * 货币编号
     */
    private final String code;

    /**
     * 货币符号
     */
    private final String symbol;

    /**
     * 货币说明
     */
    private final String desc;

    /**
     * 千位分割符
     */
    private final String splitter;

    CurrencyEnum(Integer id, String code, String symbol, String desc, String splitter) {
        this.id = id;
        this.code = code;
        this.symbol = symbol;
        this.desc = desc;
        this.splitter = splitter;
    }

    public static CurrencyEnum forId(Integer id) {
        if (id == null) {
            return null;
        }
        for (CurrencyEnum item : CurrencyEnum.values()) {
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public static CurrencyEnum forCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (CurrencyEnum item : CurrencyEnum.values()) {
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
