package com.digitalholics.consultationsservice;

import com.digitalholics.consultationsservice.Consultation.api.rest.ConsultationsController;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;
import com.digitalholics.consultationsservice.Consultation.mapping.ConsultationMapper;
import com.digitalholics.consultationsservice.Consultation.resource.ConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
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

public class ConsultationTest {

    @Mock
    private ConsultationService consultationService;

    @Mock
    private ConsultationMapper mapper;

    @InjectMocks
    private ConsultationsController consultationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateConsultation() {
        // Arrange
        CreateConsultationResource createConsultationResource = new CreateConsultationResource();
        ConsultationResource consultationResource = new ConsultationResource();
        String authorizationHeader = "Bearer token";

       // when(consultationService.create(any(CreateConsultationResource.class), anyString())).thenReturn(consultationResource);
        when(mapper.toResource(any())).thenReturn(consultationResource);

        // Act
        ResponseEntity<ConsultationResource> response = consultationsController.createConsultation(createConsultationResource, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(consultationResource, response.getBody());
    }

    @Test
    void testGetConsultationByPhysiotherapistId() {
        ConsultationResource consultationResource = new ConsultationResource();

       // when(consultationService.getConsultationByPhysiotherapistId(anyInt())).thenReturn(consultationResource);
        when(mapper.toResource(any())).thenReturn(consultationResource);

        ConsultationResource response = consultationsController.getConsultationByPhysiotherapistId(1);

        assertEquals(consultationResource, response);
    }

    @Test
    void testGetConsultationsByPhysiotherapistId() {
        Page<ConsultationResource> consultationPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(consultationService.getResourceByPhysiotherapistId(anyString(), anyInt(), any(Pageable.class))).thenReturn(consultationPage);

        Page<ConsultationResource> response = consultationsController.getConsultationsByPhysiotherapistId(1, pageable, jwt);

        assertEquals(consultationPage, response);
    }

    @Test
    void testGetConsultationsByPatientId() {
        Page<ConsultationResource> consultationPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(consultationService.getResourceByPatientId(anyString(), anyInt(), any(Pageable.class))).thenReturn(consultationPage);

        Page<ConsultationResource> response = consultationsController.getConsultationsByPatientId(1, pageable, jwt);

        assertEquals(consultationPage, response);
    }

}
