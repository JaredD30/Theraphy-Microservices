package com.digitalholics.securityservice.Security.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("usersMappingConfiguration")
public class MappingConfiguration {
    @Bean
    public UserMapper userMapper() { return new UserMapper();}
}
