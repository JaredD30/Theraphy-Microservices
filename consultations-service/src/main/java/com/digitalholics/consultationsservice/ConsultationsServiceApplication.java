package com.digitalholics.consultationsservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ConsultationsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultationsServiceApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Theraphy - Consultations Service")
                        .version("1.0")
                        .description("Theraphy Consultations Service, made with java and springboot, applying domain-driven architecture approach.")
                        .termsOfService("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/")
                        .license(new License()
                                .name("Apache 2.0 License")
                                .url("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/"))
                        .contact(new Contact()
                                .url("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/")
                                .name("Theraphy,.studio")));

    }
}
