package com.digitalholics.healthexpertiseservice;

import com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest.JobsController;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.JobService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.JobMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.CreateJobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.JobResource;
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

public class JobsTest {

    @Mock
    private JobService jobService;

    @Mock
    private JobMapper mapper;

    @InjectMocks
    private JobsController jobsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateJob() {
        CreateJobResource createJobResource = new CreateJobResource();
        JobResource jobResource = new JobResource();
        String authorizationHeader = "Bearer token";

        //when(jobService.create(anyString(), any(CreateJobResource.class))).thenReturn(jobResource);
        when(mapper.toResource(any())).thenReturn(jobResource);

        ResponseEntity<JobResource> response = jobsController.createJob(createJobResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(jobResource, response.getBody());
    }

    @Test
    void testGetJobsByPhysiotherapistId() {
        Page<JobResource> jobPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        //when(jobService.getByPhysiotherapistId(anyInt())).thenReturn(jobPage);
        when(mapper.modelListPage(any(), any(Pageable.class))).thenReturn(jobPage);

        Page<JobResource> response = jobsController.getJobsByPhysiotherapistId(jwt, 1, pageable);

        assertEquals(jobPage, response);
    }

}
