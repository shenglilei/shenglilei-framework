package com.dofun.shenglilei.framework.mysql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * UpdateIgnore
 * 全字段更新字段注解（当value = true时表示全字段更新忽略此字段的空值）
 *
 * @author Achin
 * @since 2019-04-11 14:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpdateAllColumnIgnore {
    boolean value() default true;
}
