package com.dofun.shenglilei.framework.core.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CommonAccessInterceptorConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonAccessInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/info")
                .excludePathPatterns("/actuator/**");
    }

    @Bean
    public CommonAccessInterceptor commonAccessInterceptor() {
        return new CommonAccessInterceptor();
    }
}
