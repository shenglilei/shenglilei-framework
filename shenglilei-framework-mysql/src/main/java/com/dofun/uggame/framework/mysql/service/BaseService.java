package com.dofun.uggame.framework.mysql.service;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseService
 * @Description: TODO
 * @Author: caiwensheng
 * @Date: 2018/4/9 17:49
 * @Version 1.0
 **/
public interface BaseService<T> {
    /**
     * 插入数据
     *
     * @param entity
     * @return int
     */
    int insertSelective(T entity);

    /**
     * 根据ID更新数据
     *
     * @param entity
     * @return int
     */
    int updateByPrimaryKeySelective(T entity);

    /**
     * 根据ID删除数据
     *
     * @param id
     * @return int
     */
    int deleteByPrimaryKey(Object id);

    /**
     * 根据ID查询数据
     *
     * @param id
     * @return T
     */
    T selectByPrimaryKey(Object id);

    /**
     * 查询总记录
     *
     * @param parm
     * @return int
     */
    int selectCount(T parm);

    /**
     * 查询列表
     *
     * @param parm
     * @return int
     */
    List<T> select(T parm);

    /**
     * 查询单个实体
     *
     * @param parm
     * @return int
     */
    T selectOne(T parm);

    /**
     * 查询所有
     *
     * @return
     */
    List<T> selectAll();

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
     *
     * @param fieldName
     * @param value
     * @return
     * @throws TooManyResultsException
     */
    List<T> selectBy(String fieldName, Object value);

    /**
     * 通过Model中多个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
     *
     * @param paramMap
     * @return
     * @throws TooManyResultsException
     */
    List<T> selectBy(Map<String, Object> paramMap);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param parm
     * @return
     */
    PageInfo<T> pageSelect(int pageNum, int pageSize, T parm);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param count
     * @param parm
     * @return PageInfo<T>
     */
    PageInfo<T> pageSelect(int pageNum, int pageSize, boolean count, T parm);
}
