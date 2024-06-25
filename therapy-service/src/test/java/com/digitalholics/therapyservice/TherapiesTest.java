package com.digitalholics.therapyservice;

import com.digitalholics.therapyservice.Therapy.api.rest.TherapiesController;
import com.digitalholics.therapyservice.Therapy.domain.service.TherapyService;
import com.digitalholics.therapyservice.Therapy.mapping.TherapyMapper;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
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

public class TherapiesTest {

    @Mock
    private TherapyService therapyService;

    @Mock
    private TherapyMapper mapper;

    @InjectMocks
    private TherapiesController therapiesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTherapy() {
        CreateTherapyResource createTherapyResource = new CreateTherapyResource();
        TherapyResource therapyResource = new TherapyResource();
        String authorizationHeader = "Bearer token";

        //when(therapyService.create(anyString(), any(CreateTherapyResource.class))).thenReturn(therapyResource);
        when(mapper.toResource(any())).thenReturn(therapyResource);

        ResponseEntity<TherapyResource> response = therapiesController.createTherapy(createTherapyResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(therapyResource, response.getBody());
    }

    @Test
    void testGetActiveTherapyByPatientId() {
        TherapyResource therapyResource = new TherapyResource();
        String jwt = "Bearer token";

        when(therapyService.getResourceActiveByPatientId(anyString())).thenReturn(therapyResource);

        TherapyResource response = therapiesController.getActiveTherapyByPatientId(jwt);

        assertEquals(therapyResource, response);
    }

    @Test
    void testGetTherapyByPatientId() {
        Page<TherapyResource> therapyPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(therapyService.getResourceByPatientId(anyString(), any(Pageable.class), anyInt())).thenReturn(therapyPage);

        Page<TherapyResource> response = therapiesController.getTherapyByPatientId(jwt, 1, pageable);

        assertEquals(therapyPage, response);
    }


    @Test
    void testGetTherapyByPhysiotherapistIdAndPatientId() {
        TherapyResource therapyResource = new TherapyResource();
        String jwt = "Bearer token";

        when(therapyService.getResourceByPhysiotherapistIdAndPatientId(anyString(), anyInt(), anyInt())).thenReturn(therapyResource);

        TherapyResource response = therapiesController.getTherapyByPhysiotherapistIdAndPatientId(jwt, 1, 1);

        assertEquals(therapyResource, response);
    }
}
