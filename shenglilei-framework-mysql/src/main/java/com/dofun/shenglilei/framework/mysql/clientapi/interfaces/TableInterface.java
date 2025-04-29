package com.dofun.shenglilei.framework.mysql.clientapi.interfaces;

//public interface TableInterface {
//    /**
//     * 以inner开头的API，需要限定为仅内部网使用，访问方式需要限定适用人群范围
//     */
//    String MAPPING = "/inner/framework/mysql/table/";
//
//    @ApiOperation(value = "数据拆分", notes = "将一张原始表的数据，分散到多张新表中存储")
//    @PostMapping(value = MAPPING + "split")
//    void split(@RequestBody @Validated TableSplitRequestParam param);
//
//    @ApiOperation(value = "数据合并", notes = "将分开存储的多个表数据，合并到一张表中存储")
//    @PostMapping(value = MAPPING + "merge")
//    void merge(@RequestBody @Validated TableMergeRequestParam param);
//}
