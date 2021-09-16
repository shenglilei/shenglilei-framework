package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel(description = "接口入参-基类")
@Data
public class BaseRequestParam implements Serializable {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
