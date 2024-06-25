package com.digitalholics.socialservice.Shared.configuration;

import com.digitalholics.socialservice.Social.domain.model.entity.External.Patient;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Physiotherapist;
import com.digitalholics.socialservice.Social.domain.model.entity.External.User;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    public Physiotherapist getPhysiotherapistById(String jwt, Integer id){
        String physiotherapistServiceUrl = "http://profile-service/api/v1/profile/physiotherapists/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Physiotherapist> response = restTemplate.exchange(physiotherapistServiceUrl, HttpMethod.GET, entity, Physiotherapist.class);
        return response.getBody();
    }

    public Physiotherapist updatePhysiotherapistRating(String jwt, Integer physiotherapistId, Double rating) {
        String physiotherapistServiceUrl = "http://profile-service/api/v1/profile/physiotherapists/" + physiotherapistId + "/rating";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Double> entity = new HttpEntity<>(rating, headers);
        ResponseEntity<Physiotherapist> response = restTemplate.exchange(physiotherapistServiceUrl, HttpMethod.POST, entity, Physiotherapist.class);
        return response.getBody();
    }


    public Patient getPatientByID(String jwt, Integer id){
        String patientServiceUrl = "http://profile-service/api/v1/profile/patients/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Patient> response = restTemplate.exchange(patientServiceUrl, HttpMethod.GET, entity, Patient.class);
        return response.getBody();
    }

    public Patient getPatientByUserId (String jwt, Integer userId){
        String patientServiceUrl = "http://profile-service/api/v1/profile/patients/byUserId/"+userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Patient> response = restTemplate.exchange(patientServiceUrl, HttpMethod.GET, entity, Patient.class);
        return response.getBody();
    }

    public User getUserById(Integer id) {
        String userServiceUrl = "http://security-service/api/v1/security/auth/user/"+id;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<User> response = restTemplate.exchange(userServiceUrl, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }

}
