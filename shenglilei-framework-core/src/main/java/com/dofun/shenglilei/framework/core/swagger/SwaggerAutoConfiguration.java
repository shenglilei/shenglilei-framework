package com.dofun.shenglilei.framework.core.swagger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * swagger API文档自动生成工具配置
 */
@SuppressWarnings("deprecation")
@Slf4j
@EnableSwagger2
@Configuration
@Import(SwaggerProperties.class)
public class SwaggerAutoConfiguration {
    @Value("${server.port}")
    private int port;

    @Value("${server.servlet.context-path}")
    private String baseUrl;

    @Autowired(required = false)
    private SwaggerProperties properties;

    @SneakyThrows
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getGlobalParameters());
        String ip = InetAddress.getLocalHost().getHostAddress();
        String builder = "http://" + ip +
                ":" +
                port +
                baseUrl;
        log.info("Swagger2 ready to inject");
        log.warn("Swagger使用地址:" + builder + "doc.html");
        return docket;
    }

    private List<springfox.documentation.service.Parameter> getGlobalParameters() {
        List<springfox.documentation.service.Parameter> globalParamList = new ArrayList<>();
        List<SwaggerProperties.GlobalOperationParameter> list = properties.getGlobalOperationParameters();
        if (list != null) {
            springfox.documentation.builders.ParameterBuilder parameterBuilder = new springfox.documentation.builders.ParameterBuilder();
            list.forEach(l -> {
                springfox.documentation.service.Parameter tokenParam = parameterBuilder.name(l.getName()).description(l.getDescription())
                        .modelRef(new springfox.documentation.schema.ModelRef(l.getModelRef())).parameterType(l.getParameterType())
                        .required(l.getRequired()).build();
                globalParamList.add(tokenParam);
            });
        }
        return globalParamList;
    }

    private ApiInfo apiInfo() {
        if (properties.getApiInfo() == null) {
            SwaggerProperties.ApiInfo apiInfo = new SwaggerProperties.ApiInfo();
            apiInfo.setTitle("REST API接口在线文档");
            apiInfo.setVersion("1.0");
            apiInfo.setDescription("");
            apiInfo.setEmail("");
            properties.setApiInfo(apiInfo);
        }
        Contact contact = new Contact(properties.getApiInfo().getName(), "", properties.getApiInfo().getEmail());
        return new ApiInfoBuilder()
                .title(properties.getApiInfo().getTitle())
                .description(properties.getApiInfo().getDescription())
                .contact(contact)
                .version(properties.getApiInfo().getVersion())
                .build();
    }
}
