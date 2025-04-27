package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "接口出参-基类")
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
