package com.digitalholics.profileservice.Shared;

import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalConfiguration {

    private final RestTemplate restTemplate;

    public ExternalConfiguration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUser(String jwt) {
        String userServiceUrl = "http://security-service/api/v1/security/auth/get-user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User> response = restTemplate.exchange(userServiceUrl, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }
}
