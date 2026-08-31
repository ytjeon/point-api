package com.example.pointapi.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("PG outbound-gateway api")
                .version("1.0")
                .description("PG outbound-gateway api doc 화면입니다."));
    }

    @Bean
    public GroupedOpenApi api(){
        String[] paths = {"/api/**"};
        String[] packagesToScan = {"kr.co.iaurora.pg.outbound.notice"
                , "kr.co.iaurora.pg.outbound.icf"
                ,"kr.co.iaurora.pg.outbound.ssh"
                ,"kr.co.iaurora.pg.outbound.banking"
        };
        return GroupedOpenApi.builder().group("springdoc-openapi")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }
}
