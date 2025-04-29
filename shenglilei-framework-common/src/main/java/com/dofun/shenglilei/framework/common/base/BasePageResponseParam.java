package com.dofun.shenglilei.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "接口出参-分页-基类")
@Data
public class BasePageResponseParam<T> extends BaseResponseParam {
    @ApiModelProperty(value = "当前页码（从1开始）", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "每页数据条数（1-1000）", example = "10")
    private Integer pageSize;
    @ApiModelProperty(value = "总页数", example = "100")
    private Integer pages = 0;
    @ApiModelProperty(value = "总条数", example = "10000")
    private Long total = 0L;
    @ApiModelProperty(value = "数据")
    private List<T> result = new ArrayList<>();

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
