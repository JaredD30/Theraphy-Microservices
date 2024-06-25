package com.digitalholics.healthexpertiseservice;

import com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest.DiagnosesController;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.DiagnosisService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.DiagnosisMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.CreateDiagnosisResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.DiagnosisResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiagnosesTest {

    @Mock
    private DiagnosisService diagnosisService;

    @Mock
    private DiagnosisMapper mapper;

    @InjectMocks
    private DiagnosesController diagnosesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateDiagnosis() {
        CreateDiagnosisResource createDiagnosisResource = new CreateDiagnosisResource();
        DiagnosisResource diagnosisResource = new DiagnosisResource();
        String authorizationHeader = "Bearer token";

        //when(diagnosisService.create(anyString(), any(CreateDiagnosisResource.class))).thenReturn(diagnosisResource);
        when(mapper.toResource(any())).thenReturn(diagnosisResource);

        ResponseEntity<DiagnosisResource> response = diagnosesController.createDiagnosis(createDiagnosisResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(diagnosisResource, response.getBody());
    }

    @Test
    void testGetDiagnosisByPatientId() {
        Page<DiagnosisResource> diagnosisPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(diagnosisService.getResourceByPatientId(anyString(), any(Pageable.class), anyInt())).thenReturn(diagnosisPage);

        Page<DiagnosisResource> response = diagnosesController.getDiagnosisByPatientId(jwt, 1, pageable);

        assertEquals(diagnosisPage, response);
    }

    @Test
    void testGetLast() {
        DiagnosisResource diagnosisResource = new DiagnosisResource();
        String authorizationHeader = "Bearer token";

        //when(diagnosisService.getLast(anyString())).thenReturn(diagnosisResource);
        when(mapper.toResource(any())).thenReturn(diagnosisResource);

        DiagnosisResource response = diagnosesController.getLast(authorizationHeader);

        assertEquals(diagnosisResource, response);
    }

}
