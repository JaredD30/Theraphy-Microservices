package com.digitalholics.healthexpertiseservice;

import com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest.CertificationsController;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.CertificationService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.CertificationMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
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

public class CertificationTest {

    @Mock
    private CertificationService certificationService;

    @Mock
    private CertificationMapper mapper;

    @InjectMocks
    private CertificationsController certificationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateCertification() {
        CreateCertificationResource createCertificationResource = new CreateCertificationResource();
        CertificationResource certificationResource = new CertificationResource();
        String authorizationHeader = "Bearer token";

        //when(certificationService.create(anyString(), any(CreateCertificationResource.class))).thenReturn(certificationResource);
        when(mapper.toResource(any())).thenReturn(certificationResource);

        ResponseEntity<CertificationResource> response = certificationsController.createCertification(createCertificationResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(certificationResource, response.getBody());
    }

    @Test
    void testGetCertificationsByPhysiotherapistId() {
        Page<CertificationResource> certificationPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        //when(certificationService.getByPhysiotherapistId(anyInt())).thenReturn(certificationPage);
        when(mapper.modelListPage(any(), any(Pageable.class))).thenReturn(certificationPage);

        Page<CertificationResource> response = certificationsController.getCertificationsByPhysiotherapistId(jwt, 1, pageable);

        assertEquals(certificationPage, response);
    }

}
