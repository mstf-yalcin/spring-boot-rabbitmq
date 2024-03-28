package com.rabbitmq.producer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo("RabbitMQ-Test"));
    }

    private Info getApiInfo(String appName) {
        return new Info().title(appName).description(appName).contact(getContact()).version("1.0");
    }

    private Contact getContact() {
        return new Contact().name("name").email("email").url("url");
    }


}


