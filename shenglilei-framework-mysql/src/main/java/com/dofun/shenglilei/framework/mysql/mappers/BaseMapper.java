package com.dofun.shenglilei.framework.mysql.mappers;

import com.dofun.shenglilei.framework.mysql.entity.BaseEntity;

public interface BaseMapper<T extends BaseEntity> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
