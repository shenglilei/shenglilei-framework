package com.dofun.shenglilei.framework.common.error;

/**
 * 描述： 错误定义
 */
public interface BaseError {

    /**
     * 获取错误码
     *
     * @return code
     */
    int getCode();

    /**
     * 获取错误消息
     *
     * @return message
     */
    String getMessage();

    /**
     * 转换为消息
     *
     * @param code
     * @param message
     * @return string
     */
    default String toString(int code, String message) {
        return code + "：" + message;
    }
}
