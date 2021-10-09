package com.dofun.uggame.framework.common.base;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 定义当前在使用系统的用户信息
 */
@ApiModel(description = "接口入参-基类")
@Data
public class BaseRequestParam implements Serializable {
    /**
     * 详细定义见：com.dofun.uggame.framework.common.enums.RoleEnum
     */
    @ApiModelProperty(notes = "身份类型（由网关填写 前端不传 ）", hidden = true)
    public String role;
    /**
     * 详细定义见：com.dofun.uggame.framework.common.enums.RegionEnum
     */
    @ApiModelProperty(notes = "所在国家Id（由网关填写 前端不传 ）", hidden = true)
    public Integer countryId;

    /**
     * 详细定义见：com.dofun.uggame.framework.common.enums.RegionEnum
     */
    @ApiModelProperty(notes = "所在国家区号（由网关填写 前端不传 ）", hidden = true)
    public Integer countryCode;

    /**
     * 详细定义见：com.dofun.uggame.framework.common.enums.ReqEndPointEnum
     */
    @ApiModelProperty(notes = "客户端类型（由网关填写 前端不传 ）", hidden = true)
    public String reqEndPoint;

    @ApiModelProperty(notes = "客户端设备信息（由网关填写 前端不传 ）", hidden = true)
    public String deviceInfo;

    @ApiModelProperty(notes = "用户ID（由网关填写 前端不传 ）", hidden = true)
    public Long userId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
