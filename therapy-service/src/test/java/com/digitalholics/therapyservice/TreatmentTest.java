package com.digitalholics.therapyservice;

import com.digitalholics.therapyservice.Therapy.api.rest.TreatmentsController;
import com.digitalholics.therapyservice.Therapy.domain.service.TreatmentService;
import com.digitalholics.therapyservice.Therapy.mapping.TreatmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.TreatmentResource;
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

public class TreatmentTest {

    @Mock
    private TreatmentService treatmentService;

    @Mock
    private TreatmentMapper mapper;

    @InjectMocks
    private TreatmentsController treatmentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateTreatment() {
        CreateTreatmentResource createTreatmentResource = new CreateTreatmentResource();
        TreatmentResource treatmentResource = new TreatmentResource();
        String authorizationHeader = "Bearer token";

        //when(treatmentService.create(anyString(), any(CreateTreatmentResource.class))).thenReturn(treatmentResource);
        when(mapper.toResource(any())).thenReturn(treatmentResource);

        ResponseEntity<TreatmentResource> response = treatmentsController.createTreatment(authorizationHeader, createTreatmentResource);

        assertEquals(HttpStatus.CREATED, ((ResponseEntity<?>) response).getStatusCode());
        assertEquals(treatmentResource, response.getBody());
    }

    @Test
    void testGetTreatmentByDateAndTherapyId() {
        TreatmentResource treatmentResource = new TreatmentResource();
        String jwt = "Bearer token";

        when(treatmentService.getResourceByDateAndTherapyId(anyString(), anyInt(), anyString())).thenReturn(treatmentResource);

        TreatmentResource response = treatmentsController.getTreatmentByDateAndTherapyId(jwt, "2022-10-25", 1);

        assertEquals(treatmentResource, response);
    }

    @Test
    void testGetTreatmentByTherapyId() {
        Page<TreatmentResource> treatmentPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(treatmentService.getResourcesByTherapyId(anyString(), any(Pageable.class), anyInt())).thenReturn(treatmentPage);

        Page<TreatmentResource> response = treatmentsController.getTreatmentByTherapyId(jwt, 1, pageable);

        assertEquals(treatmentPage, response);
    }

}
