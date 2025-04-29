package com.dofun.shenglilei.framework.mysql.dynamic.table.name.impl;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.tenant.TenantInfoHolder;
import com.dofun.shenglilei.framework.mysql.clientapi.pojo.request.TableSplitRequestParam;
import com.dofun.shenglilei.framework.mysql.clientapi.service.impl.TableServiceImpl;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2022/4/12
 * Time:10:25
 */
@Slf4j
public class DynamicTableNameByEnumCountryIdResolver extends TableServiceImpl implements DynamicTableNameResolver {
    /**
     * 租户ID管理器
     */
    private final TenantInfoHolder tenantInfoHolder;

    private JdbcTemplate template;

    /**
     * 限定迁移数据的countryId
     */
    private Set<Integer> countryIds;

    private Set<String> newTables;

    public DynamicTableNameByEnumCountryIdResolver(TenantInfoHolder tenantInfoHolder) {
        this.tenantInfoHolder = tenantInfoHolder;
    }


    @Override
    public String resolve(String tableName) {
        Long tenantId = this.tenantInfoHolder == null ? null : this.tenantInfoHolder.getCurrentTenantId();
        if (tenantId != null && tenantId > 0) {
            //固定拼接格式，需要与物理表名完整对应
            return resolve(tableName, tenantId.intValue());
        } else {
            log.warn("当前租户Id为空，忽略处理，返回原始表名称");
            return tableName;
        }
    }

    private String resolve(String tableName, Integer countryId) {
        //固定拼接格式，需要与物理表名完整对应
        return tableName + "_" + countryId;
    }

    @Override
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void close() {
        this.template = null;
    }

    @Override
    public long countOriginTable(TableSplitRequestParam param) {
        StringBuilder ids = new StringBuilder();
        int i = 1;
        for (Integer id : this.countryIds) {
            ids.append(id);
            if (i != this.countryIds.size()) {
                ids.append(",");
            }
            i++;
        }
        return countTable("SELECT COUNT(*) FROM " + param.getTableName() + " WHERE country_id in(" + ids + ");");
    }

    @Override
    public long countNewTable(TableSplitRequestParam param) {
        return this.countryIds.stream().mapToLong(id -> {
            String tableName = resolve(param.getTableName(), id);
            long c = countTable("SELECT COUNT(*) FROM " + tableName + " WHERE country_id =" + id + ";");
            log.info("{}表的数据总条数：{}", tableName, c);
            return c;
        }).sum();
    }

    public long countTable(String s) {
        log.info("查询表数据条数，SQL:{}", s);
        Long count = template.queryForObject(s, Long.class);
        log.info("count:{}", count);
        return count == null || count < 0 ? 0 : count;
    }

    @Override
    public void checkSplitRequestParam(TableSplitRequestParam param) {
        if (StringUtils.isEmpty(param.getModeOption())) {
            throw new IllegalArgumentException("modeOption 不能为空");
        }
        this.countryIds = new LinkedHashSet<>(JSON.parseArray(param.getModeOption(), Integer.class));
        log.info("countryIds:{}", JSON.toJSONString(this.countryIds));
        if (this.countryIds.size() <= 0) {
            throw new IllegalArgumentException("没有指定countryId");
        }
    }

    @Override
    public Set<String> getNewTables(TableSplitRequestParam param) {
        Set<String> result = new LinkedHashSet<>();
        this.countryIds.forEach(id -> result.add(resolve(param.getTableName(), id)));
        this.newTables = result;
        return result;
    }

    @Override
    public void beforeSplit(TableSplitRequestParam param) {
        // 这里是指删除新表的数据策略
        final Set<String> s = new LinkedHashSet<>();
        switch (param.getTableNewDataBefore()) {
            case DELETE:
                this.countryIds.forEach(id ->
                        s.add("DELETE FROM " + resolve(param.getTableName(), id) + " WHERE country_id =" + id + ";")
                );
                break;
            case DELETE_ALL:
                this.newTables.forEach(t ->
                        s.add("DELETE FROM " + t + ";")
                );
                break;
            default:
                throw new IllegalArgumentException("不支持的参数：" + param.getTableNewDataBefore());
        }
        s.forEach(sql -> {
            log.info("删除新表数据SQL：{}", sql);
            template.execute(sql);
            log.info("执行完成");
        });
    }

    @Override
    public void processSplit(TableSplitRequestParam param) {
        final Set<String> s = new LinkedHashSet<>();
        this.countryIds.forEach(id ->
                s.add("INSERT INTO " + resolve(param.getTableName(), id) + " SELECT * FROM " + param.getTableName() + " WHERE country_id =" + id + ";")
        );
        s.forEach(sql -> {
            log.info("迁移数据SQL：{}", sql);
            template.execute(sql);
            log.info("执行完成");
        });
    }

    @Override
    public void afterSplit(TableSplitRequestParam param) {
        // 这里是指删除原始表的数据策略
        final Set<String> s = new LinkedHashSet<>();
        switch (param.getTableOriginDataAfter()) {
            case DELETE:
                this.countryIds.forEach(id ->
                        s.add("DELETE FROM " + param.getTableName() + " WHERE country_id =" + id + ";")
                );
                break;
            case DELETE_ALL:
                this.newTables.forEach(t ->
                        s.add("DELETE FROM " + param.getTableName() + ";")
                );
                break;
            default:
                throw new IllegalArgumentException("不支持的参数：" + param.getTableNewDataBefore());
        }
        s.forEach(sql -> {
            log.info("删除原始表数据SQL：{}", sql);
            template.execute(sql);
            log.info("执行完成");
        });
    }


}
