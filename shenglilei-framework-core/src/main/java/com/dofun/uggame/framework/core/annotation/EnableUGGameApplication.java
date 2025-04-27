package com.dofun.uggame.framework.core.annotation;

import com.dofun.uggame.framework.core.bootstrap.BootStrapAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * 仅限微服务的启动类使用
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/11
 * Time:10:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BootStrapAutoConfiguration.class)
public @interface EnableUGGameApplication {

}
