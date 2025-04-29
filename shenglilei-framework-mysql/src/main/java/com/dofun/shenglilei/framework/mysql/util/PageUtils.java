package com.dofun.shenglilei.framework.mysql.util;

import com.dofun.shenglilei.framework.common.base.BasePageResponseParam;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils {

    /**
     * 转换成基础的分页响应参数对象
     */
    public static <T> BasePageResponseParam<T> asPageResponse(PageInfo<T> pageInfo) {
        BasePageResponseParam<T> pageResponseParam = new BasePageResponseParam<>();
        pageResponseParam.setPageNum(pageInfo.getPageNum());
        pageResponseParam.setPageSize(pageInfo.getPageSize());
        pageResponseParam.setTotal(pageInfo.getTotal());
        pageResponseParam.setPages(pageInfo.getPages());
        pageResponseParam.setResult(pageInfo.getList());
        return pageResponseParam;
    }

    /**
     * 转换成基础的分页响应参数对象
     */
    public static <R> BasePageResponseParam<R> asPageResponse(PageInfo<?> pageInfo, List<R> list) {
        BasePageResponseParam<R> pageResponseParam = new BasePageResponseParam<>();
        pageResponseParam.setPageNum(pageInfo.getPageNum());
        pageResponseParam.setPageSize(pageInfo.getPageSize());
        pageResponseParam.setTotal(pageInfo.getTotal());
        pageResponseParam.setPages(pageInfo.getPages());
        pageResponseParam.setResult(list);
        return pageResponseParam;
    }

    public static <T, R> void convert(PageInfo<T> pageInfo, PageInfo<R> newPageInfo) {
        newPageInfo.setPageNum(pageInfo.getPageNum());
        newPageInfo.setPageSize(pageInfo.getPageSize());
        newPageInfo.setSize(pageInfo.getSize());
        newPageInfo.setStartRow(pageInfo.getStartRow());
        newPageInfo.setEndRow(pageInfo.getEndRow());
        newPageInfo.setTotal(pageInfo.getTotal());
        newPageInfo.setPages(pageInfo.getPages());
        newPageInfo.setPrePage(pageInfo.getPrePage());
        newPageInfo.setNextPage(pageInfo.getNextPage());
        newPageInfo.setIsFirstPage(pageInfo.isIsFirstPage());
        newPageInfo.setIsLastPage(pageInfo.isIsLastPage());
        newPageInfo.setHasPreviousPage(pageInfo.isHasPreviousPage());
        newPageInfo.setHasNextPage(pageInfo.isHasNextPage());
        newPageInfo.setNavigatePages(pageInfo.getNavigatePages());
        newPageInfo.setNavigatepageNums(pageInfo.getNavigatepageNums());
        newPageInfo.setNavigateFirstPage(pageInfo.getNavigateFirstPage());
        newPageInfo.setNavigateLastPage(pageInfo.getNavigateLastPage());
    }

    /**
     * 计算总页数
     *
     * @param totalCount 数据总条数
     * @param pageSize   每页显示数量
     * @return 总页数
     */
    public static Long getTotalPage(Long totalCount, Long pageSize) {
        Long totalPage;

        // 默认共1页
        if (totalCount <= 0) {
            totalPage = 1L;
        } else {
            totalPage = new Double(Math.ceil(new Double(totalCount) / new Double(pageSize))).longValue();
        }

        return totalPage;
    }

    public static <T, E> PageInfo<T> buildPageResult(List<E> dataList, Class<T> clazz) {
        List<T> dtos = new ArrayList<>();
        dataList.forEach((data) -> {
            try {
                T t = clazz.newInstance();
                BeanUtils.copyProperties(data, t);
                dtos.add(t);
            } catch (InstantiationException | IllegalAccessException e) {
            }
        });
        PageInfo<T> result = new PageInfo<>(dtos);
        convert(new PageInfo<>(dataList), result);
        return result;
    }
}
