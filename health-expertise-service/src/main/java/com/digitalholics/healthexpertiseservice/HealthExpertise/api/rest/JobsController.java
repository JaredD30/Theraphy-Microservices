package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.JobService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.JobMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.CreateJobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.JobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.UpdateJobResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/health-expertise/jobs", produces = "application/json")
public class JobsController {
    private final JobService jobService;

    private final JobMapper mapper;


    public JobsController(JobService jobService, JobMapper mapper) {
        this.jobService = jobService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<JobResource> getAllJobs(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(jobService.getAll(), pageable);
    }

    @GetMapping("{jobId}")
    public JobResource getJobById(@RequestHeader("Authorization") String jwt, @PathVariable Integer jobId) {
        return mapper.toResource(jobService.getById(jobId));
    }


    @GetMapping("byPhysiotherapistId/{physiotherapistId}")
    public Page<JobResource> getJobsByPhysiotherapistId(@RequestHeader("Authorization") String jwt, @PathVariable Integer physiotherapistId, Pageable pageable) {
        return mapper.modelListPage(jobService.getByPhysiotherapistId(physiotherapistId), pageable);
    }

    @PostMapping
    public ResponseEntity<JobResource> createJob(@RequestHeader("Authorization") String jwt, @RequestBody CreateJobResource resource) {
        return new ResponseEntity<>(mapper.toResource(jobService.create(jwt, resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{jobId}")
    public ResponseEntity<JobResource> patchJob(@RequestHeader("Authorization") String jwt, @PathVariable Integer jobId,
                                                          @RequestBody UpdateJobResource request) {
        return new ResponseEntity<>(mapper.toResource(jobService.update(jwt, jobId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{jobId}")
    public ResponseEntity<?> deleteJob(@RequestHeader("Authorization") String jwt, @PathVariable Integer jobId) {
        return jobService.delete(jwt, jobId);
    }
}
