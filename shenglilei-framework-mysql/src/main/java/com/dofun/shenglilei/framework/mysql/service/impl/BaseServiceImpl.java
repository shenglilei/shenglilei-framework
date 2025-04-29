package com.dofun.shenglilei.framework.mysql.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * BaseServiceImpl
 *
 * @author Achin
 * @since 2021-10-22
 */
@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<T, M extends BaseMapper<T>> extends MybatisPlusServiceImpl<M, T> implements IService<T> {

}
