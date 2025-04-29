package com.dofun.shenglilei.framework.common.enums;

/**
 * 通用判断枚举
 *
 * @author Achin
 * @since 2021-10-14
 */
public enum JudgeEnum {
    /**
     * 否
     */
    FALSE(0),
    /**
     * 是
     */
    TRUE(1);

    private final Integer code;

    JudgeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public boolean equals(Integer code) {
        return this.code.equals(code);
    }
}
