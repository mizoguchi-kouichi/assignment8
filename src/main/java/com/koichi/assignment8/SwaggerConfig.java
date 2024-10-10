package com.koichi.assignment8;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学生リスト管理API")
                        .version("1.0")
                        .description("中学校および高校の学生データを管理する機能を提供しています。"))
                .servers(List.of(
                        new Server().url("http://localhost:8080/").description("ローカル開発環境")
                ));
    }
}
