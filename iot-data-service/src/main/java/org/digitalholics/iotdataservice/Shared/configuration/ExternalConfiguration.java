package org.digitalholics.iotdataservice.Shared.configuration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.digitalholics.iotdataservice.IoTData.domain.model.entity.External.Therapy;
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

    public Therapy getTheraphyByID(String jwt, Integer id){
        String therapyServiceUrl = "http://therapy-service/api/v1/therapy/therapies/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Therapy> response = restTemplate.exchange(therapyServiceUrl, HttpMethod.GET, entity, Therapy.class);
        return response.getBody();
    }

}
