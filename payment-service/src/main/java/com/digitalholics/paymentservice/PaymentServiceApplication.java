package com.digitalholics.paymentservice;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Theraphy - Payment Service")
                        .version("1.0")
                        .description("Theraphy Payment Service, made with java and springboot, applying domain-driven architecture approach.")
                        .termsOfService("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/")
                        .license(new License()
                                .name("Apache 2.0 License")
                                .url("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/"))
                        .contact(new Contact()
                                .url("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/")
                                .name("Theraphy,.studio")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                        )
                ).addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));

    }
}
