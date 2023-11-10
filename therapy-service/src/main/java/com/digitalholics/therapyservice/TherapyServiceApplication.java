package com.digitalholics.therapyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TherapyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TherapyServiceApplication.class, args);
    }

}
