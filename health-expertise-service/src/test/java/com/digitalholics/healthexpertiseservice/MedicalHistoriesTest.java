package com.digitalholics.healthexpertiseservice;

import com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest.MedicalHistoriesController;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.MedicalHistoryService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.MedicalHistoryMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.MedicalHistoryResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MedicalHistoriesTest {


    @Mock
    private MedicalHistoryService medicalHistoryService;

    @Mock
    private MedicalHistoryMapper mapper;

    @InjectMocks
    private MedicalHistoriesController medicalHistoriesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMedicalHistory() {
        CreateMedicalHistoryResource createMedicalHistoryResource = new CreateMedicalHistoryResource();
        MedicalHistoryResource medicalHistoryResource = new MedicalHistoryResource();
        String authorizationHeader = "Bearer token";

        //when(medicalHistoryService.create(anyString(), any(CreateMedicalHistoryResource.class))).thenReturn(medicalHistoryResource);
        when(mapper.toResource(any())).thenReturn(medicalHistoryResource);

        ResponseEntity<MedicalHistoryResource> response = medicalHistoriesController.createMedicalHistory(createMedicalHistoryResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(medicalHistoryResource, response.getBody());
    }


    @Test
    void testGetMedicalHistoryByPatientId() {
        MedicalHistoryResource medicalHistoryResource = new MedicalHistoryResource();
        String jwt = "Bearer token";

        when(medicalHistoryService.getResourceByPatientId(anyString(), anyInt())).thenReturn(medicalHistoryResource);
        when(mapper.toResource(any())).thenReturn(medicalHistoryResource);

        MedicalHistoryResource response = medicalHistoriesController.getMedicalHistoryByPatientId(jwt, 1, mock(Pageable.class));

        assertEquals(medicalHistoryResource, response);
    }

}
