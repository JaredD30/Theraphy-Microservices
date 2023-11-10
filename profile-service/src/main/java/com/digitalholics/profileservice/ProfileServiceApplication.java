package com.digitalholics.profileservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
public class ProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileServiceApplication.class, args);
	}
	@Bean
	public OpenAPI customOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Theraphy - Profile Service")
						.version("1.0")
						.description("Theraphy Profile Service, made with java and springboot, applying domain-driven architecture approach.")
						.termsOfService("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/")
						.license(new License()
								.name("Apache 2.0 License")
								.url("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/"))
						.contact(new Contact()
								.url("https://digitalholics-fundamentosdesoftware.github.io/Theraphy-LandingPage/")
								.name("Theraphy,.studio")));

	}
}
