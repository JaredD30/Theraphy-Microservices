package com.digitalholics.profileservice;

import com.digitalholics.profileservice.Profile.api.rest.PhysiotherapistController;
import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import com.digitalholics.profileservice.Profile.domain.service.PhysiotherapistService;
import com.digitalholics.profileservice.Profile.mapping.PhysiotherapistMapper;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.CreatePhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.PhysiotherapistResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ProfilePhysiotherapistTest {
    @Mock
    private PhysiotherapistService physiotherapistService;

    @Mock
    private PhysiotherapistMapper mapper;

    @InjectMocks
    private PhysiotherapistController physiotherapistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePhysiotherapist() {
        CreatePhysiotherapistResource createPhysiotherapistResource = new CreatePhysiotherapistResource();
        Physiotherapist physiotherapist = new Physiotherapist();
        PhysiotherapistResource physiotherapistResource = new PhysiotherapistResource();
        String authorizationHeader = "Bearer token";

        when(physiotherapistService.create(any(CreatePhysiotherapistResource.class), anyString())).thenReturn(physiotherapist);
        when(mapper.toResource(any(Physiotherapist.class))).thenReturn(physiotherapistResource);

        ResponseEntity<PhysiotherapistResource> response = physiotherapistController.createPhysiotherapist(createPhysiotherapistResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(physiotherapistResource, response.getBody());
    }

    @Test
    void testGetPhysiotherapistByUserId() {
        Physiotherapist physiotherapist = new Physiotherapist();
        PhysiotherapistResource physiotherapistResource = new PhysiotherapistResource();

        when(physiotherapistService.getByUserId(anyInt())).thenReturn(physiotherapist);
        when(mapper.toResource(any(Physiotherapist.class))).thenReturn(physiotherapistResource);

        PhysiotherapistResource response = physiotherapistController.getPhysiotherapistByUserId("Bearer token", 1);

        assertEquals(physiotherapistResource, response);
    }

    @Test
    void testGetPatientLogget() {
        String authorizationHeader = "Bearer token";
        PhysiotherapistResource physiotherapistResource = new PhysiotherapistResource();

        when(physiotherapistService.getLoggedInPhysiotherapist(any(String.class))).thenReturn(physiotherapistResource);

        PhysiotherapistResource response = physiotherapistController.getPatientLogget(authorizationHeader);

        assertEquals(physiotherapistResource, response);
    }

}
