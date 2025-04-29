package com.dofun.shenglilei.framework.mysql.clientapi.pojo.request;

import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.mysql.dynamic.table.name.DynamicTableNameMode;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 原始表  ->  新表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TableSplitRequestParam extends BaseRequestParam {
    /**
     * 原始表名称
     */
    @NotBlank
    private String tableName;
    /**
     * 拆分数据模式
     */
    @NotNull
    private DynamicTableNameMode.Mode mode;
    /**
     * 拆分数据-参数
     */
    private String modeOption;

    /**
     * 新表在拆分数据操作前的数据处理
     */
    @NotNull
    private DataOperationType tableNewDataBefore = DataOperationType.NONE;

    /**
     * 原始表在拆分数据操作后的数据处理
     */
    @NotNull
    private DataOperationType tableOriginDataAfter = DataOperationType.NONE;
}
