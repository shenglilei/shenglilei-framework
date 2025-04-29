package com.dofun.shenglilei.framework.mysql.clientapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.error.impl.CommonError;
import com.dofun.shenglilei.framework.common.exception.BusinessException;
import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.DataOperationType;
import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.TableMergeRequestParam;
import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.TableSplitRequestParam;
import com.dofun.shenglilei.framework.mysql.clientapi.service.TableService;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameMode;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class TableServiceImpl implements TableService {

    @Autowired(required = false)
    private DynamicTableNameMode dynamicTableNameMode;

    @Resource
    private JdbcTemplate template;

    /**
     * 注意：
     * <p>
     * 1.这个函数是不允许线程并发执行的，会导致分表错乱，主要是成员变量的问题
     * <p>
     * 2.没有做任何数据备份的操作，出现异常可能会导致数据丢失，建议在使用前，调用mysqldump，对表进行物理备份
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public synchronized void split(TableSplitRequestParam param) {
        log.info("开始处理数据迁移：{}", JSON.toJSONString(param));
        DynamicTableNameResolver resolver = null;
        try {
            resolver = this.dynamicTableNameMode.getResolver(param.getMode());
            resolver.setTemplate(template);
            // 1.验证输入参数
            resolver.checkSplitRequestParam(param);
            // 2. 验证表是否存在
            if (!exists(param.getTableName())) {
                throw new IllegalArgumentException("原始数据表不存在:" + param.getTableName());
            }
            Set<String> tableNameNew = resolver.getNewTables(param);
            log.info("tableNameNew:{}", JSON.toJSONString(tableNameNew));
            if (tableNameNew.isEmpty()) {
                log.warn("无需处理，新表的表名称集合为空");
                return;
            }
            boolean flag = copy(param.getTableName(), tableNameNew);
            log.info("数据表，复制结构，结果：{}", flag);
            // 锁定 相关表

            // 原始表，加上读锁
            Map<String, LockMode> locks = new LinkedHashMap<>();
            locks.put(param.getTableName(), LockMode.READ);
            // 新表，加上写锁
            tableNameNew.forEach(tableName -> locks.put(tableName, LockMode.WRITE));
            // 需要一次性完成锁定，分开多次加锁，则会导致之前的锁自动失效
            lock(locks);
            long numOrigin = resolver.countOriginTable(param);
            log.info("原始表的数据总条数：{}", numOrigin);
            if (numOrigin <= 0) {
                log.warn("无需处理，原始表数据为空");
                return;
            }
            // 3. 处理执行前的数据处理逻辑
            if (!(param.getTableNewDataBefore() == null || param.getTableNewDataBefore().equals(DataOperationType.NONE))) {
                resolver.beforeSplit(param);
            } else {
                log.info("不需要处理执行前");
            }
            // 4. 开始拆分数据
            resolver.processSplit(param);
            // 5. 处理执行后的数据处理逻辑
            if (!(param.getTableOriginDataAfter() == null || param.getTableOriginDataAfter().equals(DataOperationType.NONE))) {
                resolver.afterSplit(param);
            } else {
                log.info("不需要处理执行后");
            }
            // 6. 检验数据行数
            long numNew = resolver.countNewTable(param);
            log.info("新表的数据总条数：{}", numNew);
            if (numOrigin != numNew) {
                log.error("数据迁移处理失败：{}  -> {}", numOrigin, numNew);
                throw new BusinessException(CommonError.SYSTEM_ERROR);
            }
            log.info("数据迁移处理成功");
            //  TODO  7. 回收表空间，减少磁盘占用
        } finally {
            if (resolver != null) {
                resolver.close();
            }
            // 解锁 所有表
            unlockAll();
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void merge(TableMergeRequestParam param) {

    }

    /**
     * 验证表是否存在
     */
    private boolean exists(String tableName) {
        String s = "SHOW TABLES LIKE '" + tableName + "';";
        log.info("SQL:{}", s);
        List<String> tableNames = template.queryForList(s, String.class);
        log.info("tableNames:{}", JSON.toJSONString(tableNames));
        return tableNames.size() == 1 && tableNames.get(0).equals(tableName);
    }

    /**
     * 创建表-复制结构
     * <p>
     * 复制表的备注、索引、主键外键、存储引擎
     */
    private boolean copy(String tableName, Set<String> tableNameNew) {
        boolean isAllSuccess = true;
        for (String item : tableNameNew) {
            if (!exists(item)) {
                if (!copy0(tableName, item)) {
                    isAllSuccess = false;
                }
            }
        }
        return isAllSuccess;
    }

    private boolean copy0(String tableName, String tableNameNew) {
        String s = "CREATE TABLE " + tableNameNew + " LIKE " + tableName + ";";
        log.info("复制表结构，SQL:{}", s);
        template.execute(s);
        log.info("执行完成");
        return exists(tableNameNew);
    }

    /**
     * 锁定 相关表
     * <p>
     * <p>
     * 在使用LOCK TABLES进行加锁时，会解锁之前锁定的表。
     */
    private void lock(Map<String, LockMode> locks) {
        StringBuilder tables = new StringBuilder();
        int i = 1;
        for (Map.Entry<String, LockMode> entry : locks.entrySet()) {
            tables.append(entry.getKey()).append(" ").append(entry.getValue().name());
            if (i != locks.size()) {
                tables.append(",");
            }
            i++;
        }
        String s = "LOCK TABLES " + tables + ";";
        log.info("锁定表，SQL:{}", s);
        template.execute(s);
        log.info("执行完成");
    }

    /**
     * 解锁 所有表
     * <p>
     * UNLOCK TABLES可以解锁所有当前会话中锁定的表
     */
    private void unlockAll() {
        String s = "UNLOCK TABLES;";
        log.info("解锁所有表，SQL:{}", s);
        template.execute(s);
        log.info("执行完成");
    }

    /**
     * 锁定表的类型
     */
    enum LockMode {
        /**
         * 所有线程(包括执行加锁的线程本身)均可读取表 ，但均不能写入表
         */
        READ,
        /**
         * 只有本线程可读取和写入表 ，其他不能读也不能写入表
         */
        WRITE
    }
}