package com.parking.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Parking Auth Service",
                version = "0.0.1-SNAPSHOT",
                description = "Authentication APIs for user registration, role assignment, login, and JWT issuing."
        )
)
public class OpenApiConfig {
}