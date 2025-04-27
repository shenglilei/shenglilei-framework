package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@ApiModel(description = "接口入参-分页-基类")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePageRequestParam extends BaseRequestParam {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码（从1开始）", example = "1")
    @NotNull(message = "当前页码:不能为空")
    @Range(message = "每页数据条数的值[1,]", min = 1)
    private Integer pageNum;
    /**
     * 每页数据条数
     */
    @ApiModelProperty(value = "每页数据条数（1-1000）", example = "10")
    @NotNull(message = "每页数据条数:不能为空")
    @Range(message = "每页数据条数的值[1-1000]", min = 1, max = 1000)
    private Integer pageSize;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
