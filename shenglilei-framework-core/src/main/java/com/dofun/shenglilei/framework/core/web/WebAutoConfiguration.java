package com.dofun.shenglilei.framework.core.web;


import com.dofun.shenglilei.framework.common.utils.JacksonUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/11
 * Time:10:05
 */
@Slf4j
@Configuration
public class WebAutoConfiguration implements WebMvcConfigurer {

    @PostConstruct
    public void init() {
        log.info("WebAutoConfiguration is ready to inject");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        //解决18位Id过长，导致javascript无法解析为integer的问题
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        List<MediaType> mediaTypeList = jackson2HttpMessageConverter.getSupportedMediaTypes();
        mediaTypeList.forEach(mediaType -> log.info("before supportedMediaTypes:{}", mediaType.toString()));
        List<MediaType> newMediaTypeList = new ArrayList<>(mediaTypeList);
        newMediaTypeList.add(MediaType.TEXT_EVENT_STREAM);
        jackson2HttpMessageConverter.setSupportedMediaTypes(newMediaTypeList);
        mediaTypeList = jackson2HttpMessageConverter.getSupportedMediaTypes();
        mediaTypeList.forEach(mediaType -> log.info("after supportedMediaTypes:{}", mediaType.toString()));
        converters.add(0, jackson2HttpMessageConverter);
    }
}