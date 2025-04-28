package com.dofun.shenglilei.framework.core.bootstrap;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import static com.dofun.shenglilei.framework.common.constants.Constants.SYSTEM_DEFAULT_PACKAGE_ROOT;


@Slf4j
@ServletComponentScan
@EnableAutoConfiguration
@ComponentScan(basePackages = {SYSTEM_DEFAULT_PACKAGE_ROOT + ".*"})
@SpringBootConfiguration
//@EnableDiscoveryClient
@EnableFeignClients(basePackages = {SYSTEM_DEFAULT_PACKAGE_ROOT})
public class BootStrapAutoConfiguration {
}
