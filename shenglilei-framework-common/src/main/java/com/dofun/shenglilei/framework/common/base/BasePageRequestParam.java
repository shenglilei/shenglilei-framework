package com.dofun.shenglilei.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "接口入参-分页-基类")
@EqualsAndHashCode(callSuper = true)
@Data
public class BasePageRequestParam extends BaseRequestParam {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码（从1开始）", example = "1")
    @NotNull(message = "pageNum must not null")
    @Range(message = "pageNum range [1,]", min = 1)
    private Integer pageNum;
    /**
     * 每页数据条数
     */
    @ApiModelProperty(value = "每页数据条数（1-1000）", example = "10")
    @NotNull(message = "pageSize must not null")
    @Range(message = "pageSize range [1-1000]", min = 1, max = 1000)
    private Integer pageSize;

    @ApiModelProperty(value = "排序字段")
    @Pattern(regexp = "(^[a-zA-Z_]+$)|", message = "sortField illegal")
    private String sortField;

    @ApiModelProperty(value = "排序方式[ASC DESC]")
    @Pattern(regexp = "(asc)|(desc)|(ASC)|(DESC)|", message = "sortWay illegal")
    private String sortWay;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
