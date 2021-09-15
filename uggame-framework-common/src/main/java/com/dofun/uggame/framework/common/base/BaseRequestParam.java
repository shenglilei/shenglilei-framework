package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口入参-基类
 */
@Data
public class BaseRequestParam implements Serializable {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码（从1开始）：(不需要时可以不传该字段)", example = "1")
    private Integer pageNum;
    /**
     * 每页数据条数
     */
    @ApiModelProperty(value = "每页数据条数（1-100）：(不需要时可以不传该字段)", example = "10")
    private Integer pageSize;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
