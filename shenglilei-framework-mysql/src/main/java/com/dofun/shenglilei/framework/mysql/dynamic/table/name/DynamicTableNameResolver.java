package com.dofun.shenglilei.framework.mysql.dynamic.table.name;

import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.TableSplitRequestParam;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Set;

/**
 * 接口：动态表名-解释器
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2022/4/12
 * Time:10:23
 */
public interface DynamicTableNameResolver {
    /**
     * 动态表名-解释器
     *
     * @param tableName 原始表名称
     * @return 新的表名称
     */
    String resolve(String tableName);

    void setTemplate(JdbcTemplate template);

    void checkSplitRequestParam(TableSplitRequestParam param);

    Set<String> getNewTables(TableSplitRequestParam param);

    void beforeSplit(TableSplitRequestParam param);

    void processSplit(TableSplitRequestParam param);

    void afterSplit(TableSplitRequestParam param);

    void close();

    long countOriginTable(TableSplitRequestParam param);

    long countNewTable(TableSplitRequestParam param);
}
