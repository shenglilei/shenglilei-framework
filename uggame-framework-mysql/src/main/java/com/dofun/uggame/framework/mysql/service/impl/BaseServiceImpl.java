package com.dofun.uggame.framework.mysql.service.impl;

import com.dofun.uggame.framework.common.utils.JniInvokeUtils;
import com.dofun.uggame.framework.mysql.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseServiceImpl<T, U extends Mapper<T>> implements BaseService<T> {
    /**
     * 当前泛型真实类型的Class
     */
    private final Class<T> modelClass;
    @Autowired
    protected U mapper;

    /**
     * 构造函数
     */
    public BaseServiceImpl() {
        ParameterizedType pt = (ParameterizedType) JniInvokeUtils.getClass(this).getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public int insertSelective(T entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int deleteByPrimaryKey(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public T selectByPrimaryKey(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public int selectCount(T parm) {
        return mapper.selectCount(parm);
    }

    @Override
    public List<T> select(T parm) {
        return mapper.select(parm);
    }

    @Override
    public T selectOne(T parm) {
        return mapper.selectOne(parm);
    }

    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> selectBy(String fieldName, Object value) {
        try {
            T model = modelClass.newInstance();
            Field field = modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return mapper.select(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<T> selectBy(Map<String, Object> paramMap) {
        try {
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            T model = modelClass.newInstance();
            for (Map.Entry<String, Object> entry : entrySet) {
                Field field = modelClass.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(model, entry.getValue());
            }
            return mapper.select(model);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public PageInfo<T> pageSelect(int pageNum, int pageSize, T parm) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = mapper.select(parm);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<T> pageSelect(int pageNum, int pageSize, boolean count, T parm) {
        PageHelper.startPage(pageNum, pageSize, count);
        List<T> list = mapper.select(parm);
        return new PageInfo<>(list);
    }
}
