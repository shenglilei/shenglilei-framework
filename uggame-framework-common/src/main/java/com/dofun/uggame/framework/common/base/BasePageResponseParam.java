package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "接口出参-分页-基类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePageResponseParam<T> extends BaseResponseParam {
    @ApiModelProperty(value = "当前页码（从1开始）", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "每页数据条数（1-1000）", example = "10")
    private Integer pageSize;
    @ApiModelProperty(value = "总页数", example = "100")
    private Integer pages;
    @ApiModelProperty(value = "总条数", example = "10000")
    private Long total;
    @ApiModelProperty(value = "数据")
    private List<T> result;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
