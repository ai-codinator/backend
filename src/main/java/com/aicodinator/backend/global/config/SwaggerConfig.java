package com.aicodinator.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
            .components(new Components().addSecuritySchemes("Authorization",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
            .title("AI를 활용한 개인 맞춤형 지역 이주 정착 지원 서비스 API")
            .description("KDT 해커톤 AI-C")
            .version("1.0.0");
    }
}

