package com.tui.proof.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Pilotes Order System")
                .description("API to control the orders of Pilotes")
                .contact(new Contact()
                        .name("Martin Bolatti")
                        .email("martinbolatti@gmail.com"))
                .license(new License()
                        .name("Apache License Version 2.0")
                        .url("https://example.io/EXAMPLE"))
                .version("1.0");
    }

}