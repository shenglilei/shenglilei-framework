package com.dofun.shenglilei.framework.mysql.dynamic.table.name;

import com.dofun.shenglilei.framework.mysql.dynamic.table.name.impl.DynamicTableNameByEnumCountryIdResolver;
import lombok.Getter;
import lombok.Setter;

/**
 * 接口：动态表名-模式
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2022/4/12
 * Time:10:23
 */
public class DynamicTableNameMode {
    @Setter
    private DynamicTableNameByEnumCountryIdResolver dynamicTableNameByEnumCountryIdResolver;

    public DynamicTableNameResolver getResolver(Mode mode) {
        switch (mode) {
            case ENUM_COUNTRY_ID:
                return this.dynamicTableNameByEnumCountryIdResolver;
            default:
                throw new IllegalArgumentException("不支持的处理模式：" + mode);
        }
    }

    @Getter
    public enum Mode {
        /**
         * 枚举型：按照限定的country_id值进行处理
         */
        ENUM_COUNTRY_ID,
    }
}
