package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口出参-基类
 */
@Data
public class BaseResponseParam implements Serializable {

    public static BaseResponseParam empty() {
        return new BaseResponseParam();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
