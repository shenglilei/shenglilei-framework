package com.dofun.shenglilei.framework.common.base;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.enums.AppEnum;
import com.dofun.shenglilei.framework.common.enums.LanguageEnum;
import com.dofun.shenglilei.framework.common.enums.RegionEnum;
import com.dofun.shenglilei.framework.common.error.impl.CommonError;
import com.dofun.shenglilei.framework.common.exception.BusinessException;
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
     * 详细定义见：com.dofun.uggame.framework.common.enums.AppEnum
     */
    @ApiModelProperty(notes = "客户端产品Id（由网关填写 前端不传 ）", hidden = true)
    public String appId = AppEnum.UGGame.getName();

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
    @ApiModelProperty(notes = "所在国家语言Id（由网关填写 前端不传 ）", hidden = true)
    public Integer languageId;

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

    @ApiModelProperty(notes = "系统版本", hidden = true)
    public String osVersion;

    @ApiModelProperty(notes = "app数字版本号", hidden = true)
    public Integer appVersion;

    @ApiModelProperty(notes = "ip（由网关填写 前端不传 ）", hidden = true)
    public String ip;

    @ApiModelProperty(notes = "用户ID（由网关填写 前端不传 ）", hidden = true)
    public Long userId;

    @ApiModelProperty(notes = "具体来源 如4301 越南, 4309国际版越南, 4409国际版印尼（由网关填写 前端不传 ）", hidden = true)
    public Integer addFromHead;

    /**
     * 校验 countryId 是否有效，如果无效会抛出异常
     */
    public void exceptValidCountryId() {
        RegionEnum regionEnum = RegionEnum.forCountryId(this.getCountryId());
        if (regionEnum == null) {
            throw new BusinessException(CommonError.ILLEGAL_PARAMETER.getCode(), "Unrecognized data：countryId=：" + this.getCountryId());
        }
        //如果此时 countryCode 不匹配，则会重新设置
        if (this.getCountryCode() == null || !this.getCountryCode().equals(regionEnum.getCountryCode())) {
            this.setCountryCode(regionEnum.getCountryCode());
        }
    }

    public void setDefaultLanguageIdId() {
        RegionEnum regionEnum = RegionEnum.forCountryId(this.getCountryId());
        if (regionEnum == null) {
            throw new BusinessException(CommonError.ILLEGAL_PARAMETER.getCode(), "Unrecognized data：countryId=：" + this.getCountryId());
        }

        if (this.getLanguageId() == null || LanguageEnum.forId(this.getLanguageId()) == null) {
            // 设置默认语言
            this.setLanguageId(regionEnum.getLanguageId());
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
