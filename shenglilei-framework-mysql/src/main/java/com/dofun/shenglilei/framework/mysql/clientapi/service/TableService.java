package com.dofun.shenglilei.framework.mysql.clientapi.service;

import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.TableMergeRequestParam;
import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.TableSplitRequestParam;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2022/4/12
 * Time:14:28
 */
public interface TableService {
    void split(TableSplitRequestParam param);

    void merge(TableMergeRequestParam param);
}
