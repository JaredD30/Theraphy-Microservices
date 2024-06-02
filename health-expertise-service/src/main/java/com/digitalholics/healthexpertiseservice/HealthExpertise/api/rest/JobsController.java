package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.JobService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.JobMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.CreateJobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.JobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.UpdateJobResource;

import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/health-expertise/jobs", produces = "application/json")
@Tag(name = "Jobs", description = "Jobs operations: listing, retrieval, validation, creation, update, and deletion")
public class JobsController {
    private final JobService jobService;

    private final JobMapper mapper;


    public JobsController(JobService jobService, JobMapper mapper) {
        this.jobService = jobService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all jobs", description = "Returns jobs' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<JobResource> getAllJobs(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(jobService.getAll(), pageable);
    }

    @Operation(summary = "Get job by id", description = "Returns job with a provide id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{jobId}")
    public JobResource getJobById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Job id", required = true, examples = @ExampleObject(name = "jobId", value = "1")) @PathVariable Integer jobId) {
        return mapper.toResource(jobService.getById(jobId));
    }

    @Operation(summary = "Get job by physiotherapist id", description = "Returns job with a provide physiotherapist id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byPhysiotherapistId/{physiotherapistId}")
    public Page<JobResource> getJobsByPhysiotherapistId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Physiotherapist Id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer physiotherapistId, Pageable pageable
    ) {
        return mapper.modelListPage(jobService.getByPhysiotherapistId(physiotherapistId), pageable);
    }

    @Operation(summary = "Create job", description = "Register a job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<JobResource> createJob(
            //@RequestHeader("Authorization") String jwt, @RequestBody CreateJobResource resource
            @RequestBody CreateJobResource resource,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        //return new ResponseEntity<>(mapper.toResource(jobService.create(jwt, resource)), HttpStatus.CREATED);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }
        return new ResponseEntity<>(mapper.toResource(jobService.create(authorizationHeader,(resource))), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a job partially", description = "Updates a job partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{jobId}")
    public ResponseEntity<JobResource> patchJob(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer jobId,
            @RequestBody UpdateJobResource request
    ) {
        return new ResponseEntity<>(mapper.toResource(jobService.updatePositionOrganization(jwt, jobId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a job", description = "Delete a job with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{jobId}")
    public ResponseEntity<?> deleteJob(@RequestHeader("Authorization") String jwt, @PathVariable Integer jobId) {
        return jobService.delete(jwt, jobId);
    }
}
