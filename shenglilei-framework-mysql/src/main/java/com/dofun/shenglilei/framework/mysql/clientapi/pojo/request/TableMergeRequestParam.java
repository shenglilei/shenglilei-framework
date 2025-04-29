package com.dofun.shenglilei.framework.mysql.clientapi.pojo.request;

import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameMode;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新表  ->  原始表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TableMergeRequestParam extends BaseRequestParam {
    /**
     * 原始表名称
     */
    @NotBlank
    private String tableName;
    /**
     * 合并数据模式
     */
    @NotNull
    private DynamicTableNameMode.Mode mode;


    /**
     * 合并数据-参数
     */
    private String modeOption;

    /**
     * 新表在拆分数据操作后的数据处理
     */
    @NotNull
    private DataOperationType tableNewDataAfter = DataOperationType.NONE;

    /**
     * 原始表在拆分数据操作前的数据处理
     */
    @NotNull
    private DataOperationType tableOriginDataBefore = DataOperationType.NONE;
}
