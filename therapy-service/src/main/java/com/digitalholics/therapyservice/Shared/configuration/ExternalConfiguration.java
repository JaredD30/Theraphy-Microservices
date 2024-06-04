package com.digitalholics.therapyservice.Shared.configuration;


import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Consultation;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Patient;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Physiotherapist;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExternalConfiguration {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExternalConfiguration(RestTemplate restTemplate, ObjectMapper objectMapper) {

        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
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

    public Physiotherapist getPhysiotherapistByUserId (String jwt, Integer userId){
        String patientServiceUrl = "http://profile-service/api/v1/profile/physiotherapists/byUserId/"+userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Physiotherapist> response = restTemplate.exchange(patientServiceUrl, HttpMethod.GET, entity, Physiotherapist.class);
        return response.getBody();
    }

    public List<Consultation> getConsultationByPhysiotherapyId(String jwt, Integer physiotherapistId) {
        String physiotherapistServiceUrl = "http://consultations-service/api/v1/consultations/byPhysiotherapistId/" + physiotherapistId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                physiotherapistServiceUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        try {
            // Log the response body for debugging purposes
            System.out.println("Response JSON: " + response.getBody());

            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode contentNode = rootNode.path("content");

            // Convert the content node to a list of Consultation
            return objectMapper.readValue(contentNode.toString(), new TypeReference<List<Consultation>>() {});
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            throw new RuntimeException("Failed to convert JSON response to List<Consultation>", e);
        }
    }

    public User getUserById(Integer id) {
        String userServiceUrl = "http://security-service/api/v1/security/auth/user/"+id;
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<User> response = restTemplate.exchange(userServiceUrl, HttpMethod.GET, entity, User.class);
        return response.getBody();
    }

}
