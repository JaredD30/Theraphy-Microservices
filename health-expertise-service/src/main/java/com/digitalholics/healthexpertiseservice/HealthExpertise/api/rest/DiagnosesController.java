package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.DiagnosisService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.DiagnosisMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.CreateDiagnosisResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.DiagnosisResource;
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
@RequestMapping(value = "/api/v1/health-expertise/diagnoses", produces = "application/json")
@Tag(name = "Diagnoses", description = "Diagnoses operations: retrieval and deletion")
public class DiagnosesController {

    private final DiagnosisService diagnosisService;

    private final DiagnosisMapper mapper;

    public DiagnosesController(DiagnosisService diagnosisService, DiagnosisMapper mapper) {
        this.diagnosisService = diagnosisService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get last diagnoses", description = "Returns the last diagnoses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public DiagnosisResource getLast(
            @Parameter(hidden = true)
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }

        return mapper.toResource(diagnosisService.getLast(authorizationHeader));
    }

    @Operation(summary = "Get diagnoses by patient id", description = "Returns diagnoses with a provide patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("byPatientId/{patientId}")
    public Page<DiagnosisResource> getDiagnosisByPatientId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId, Pageable pageable
    ) {
        return diagnosisService.getResourceByPatientId(jwt,pageable,patientId);
    }

    @Operation(summary = "Create diagnosis", description = "Register a diagnosis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<DiagnosisResource> createDiagnosis(
            //@Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            //@RequestBody CreateCertificationResource resource) {
            //return new ResponseEntity<>(mapper.toResource(certificationService.create(jwt, resource)), HttpStatus.CREATED);
            @RequestBody CreateDiagnosisResource resource,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }
        return new ResponseEntity<>(mapper.toResource(diagnosisService.create(authorizationHeader,(resource))), HttpStatus.CREATED);
    }
}
