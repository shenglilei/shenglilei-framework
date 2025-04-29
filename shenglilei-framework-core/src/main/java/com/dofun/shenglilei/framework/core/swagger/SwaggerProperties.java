package com.dofun.shenglilei.framework.core.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {

    private String basePackage;

    private ApiInfo apiInfo;

    private List<GlobalOperationParameter> globalOperationParameters;

    @Data
    public static class ApiInfo {
        private String title;
        private String name;
        private String description;
        private String email;
        private String version;
    }

    @Data
    public static class GlobalOperationParameter {
        private String name;
        private String description;
        private String parameterType;
        private String modelRef;
        private Boolean required;
    }

}
