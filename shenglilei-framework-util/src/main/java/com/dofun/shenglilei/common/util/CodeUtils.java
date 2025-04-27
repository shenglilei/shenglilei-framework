package com.dofun.shenglilei.common.util;

/**
 * @desc 编码号工具
 */
public class CodeUtils {

    public static String create(String parentCode, int index, int pos) {
        return parentCode + StringUtilsExt.complementZero(pos, index);
    }
}
