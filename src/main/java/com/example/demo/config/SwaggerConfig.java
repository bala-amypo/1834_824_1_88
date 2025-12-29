package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // 🔐 JWT Bearer Security Scheme
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                // 📌 API Info
                .info(new Info()
                        .title("Apartment Facility Booking API")
                        .version("1.0")
                        .description("API documentation for Apartment Facility Booking System"))

                // 🌐 VERY IMPORTANT SERVER (POWER LINK)
                .servers(List.of(
                        new Server()
                                .url("https://9038.32procr.amypo.ai")
                                .description("Production Server")
                ))

                // 🔒 Apply JWT globally
                .addSecurityItem(
                        new SecurityRequirement().addList("bearerAuth")
                )

                // 🔑 Register JWT scheme
                .schemaRequirement("bearerAuth", bearerAuth);
    }
}
