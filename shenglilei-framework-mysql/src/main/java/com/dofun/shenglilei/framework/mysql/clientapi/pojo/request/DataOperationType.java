package com.dofun.shenglilei.framework.mysql.clientapi.pojo.request;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2022/4/12
 * Time:13:58
 */
public enum DataOperationType {
    NONE("不做处理"),
    DELETE("删除相关数据"),
    DELETE_ALL("删除所有数据"),
    ;
    private final String desc;

    DataOperationType(String desc) {
        this.desc = desc;
    }
}
