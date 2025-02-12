package com.dofun.uggame.framework.mysql.mappers;

import com.dofun.uggame.framework.mysql.entity.BaseEntity;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper<T extends BaseEntity> extends tk.mybatis.mapper.common.BaseMapper<T>, Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
