package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.CertificationService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.CertificationMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.UpdateCertificationResource;
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
@RequestMapping(value = "/api/v1/health-expertise/certifications", produces = "application/json")
@Tag(name = "Certifications", description = "Certifications operations: listing, retrieval, creation, update, and deletion")
public class CertificationsController {
    private final CertificationService certificationService;
    private final CertificationMapper mapper;

    public CertificationsController(CertificationService certificationService, CertificationMapper mapper) {
        this.certificationService = certificationService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all certifications", description = "Returns certifications' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<CertificationResource> getAllCertifications(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(certificationService.getAll(), pageable);
    }

    @Operation(summary = "Get certification by id", description = "Returns certification with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{certificationId}")
    public CertificationResource getCertificationById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Certification Id", required = true, examples = @ExampleObject(name = "certificationId", value = "1")) @PathVariable Integer certificationId
    ) {
        return mapper.toResource(certificationService.getById(certificationId));
    }

    @Operation(summary = "Get certification by physiotherapist id", description = "Returns certification with a provided physiotherapist id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byPhysiotherapistId/{physiotherapistId}")
    public Page<CertificationResource> getCertificationsByPhysiotherapistId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Physiotherapist Id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer physiotherapistId, Pageable pageable
    ) {
        return mapper.modelListPage(certificationService.getByPhysiotherapistId(physiotherapistId), pageable);
    }

    @Operation(summary = "Create certification", description = "Register a certification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<CertificationResource> createCertification(
            //@Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            //@RequestBody CreateCertificationResource resource) {
        //return new ResponseEntity<>(mapper.toResource(certificationService.create(jwt, resource)), HttpStatus.CREATED);
            @RequestBody CreateCertificationResource resource,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }
        return new ResponseEntity<>(mapper.toResource(certificationService.create(authorizationHeader,(resource))), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a certification partially", description = "Updates a certification partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{certificationId}")
    public ResponseEntity<CertificationResource> patchCertification(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Certification Id", required = true, examples = @ExampleObject(name = "certificationId", value = "1")) @PathVariable Integer certificationId,
            @RequestBody UpdateCertificationResource request
    ) {
        return new ResponseEntity<>(mapper.toResource(certificationService.updateTitleSchoolYear(jwt, certificationId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a certification", description = "Delete a certification with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{certificationId}")
    public ResponseEntity<?> deleteCertification(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Certification Id", required = true, examples = @ExampleObject(name = "certificationId", value = "1")) @PathVariable Integer certificationId) {
        return certificationService.delete(jwt, certificationId);
    }

}
