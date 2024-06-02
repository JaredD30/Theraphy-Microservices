package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.MedicalHistoryService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.MedicalHistoryMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.MedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.UpdateMedicalHistoryResource;

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
@RequestMapping(value = "/api/v1/health-expertise/medical-histories", produces = "application/json")
@Tag(name = "Medical histories", description = "Medical histories operations: listing, retrieval, validation, creation, update, and deletion")

public class MedicalHistoriesController {

    private final MedicalHistoryService medicalHistoryService;
    private final MedicalHistoryMapper mapper;

    public MedicalHistoriesController(MedicalHistoryService medicalHistoryService, MedicalHistoryMapper mapper) {
        this.medicalHistoryService = medicalHistoryService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all medical histories", description = "Returns medical histories' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<MedicalHistoryResource> getAllMedicalHistories(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(medicalHistoryService.getAll(jwt), pageable);
    }

    @Operation(summary = "Get medical history by patient id", description = "Returns medical history with a provide patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byPatientId/{patientId}")
    public MedicalHistoryResource getMedicalHistoryByPatientId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId, Pageable pageable) {
        return medicalHistoryService.getResourceByPatientId(jwt, patientId);
    }

    @Operation(summary = "Create medical history", description = "Register a medical history")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<MedicalHistoryResource> createMedicalHistory(
          //  @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
        //    @RequestBody CreateMedicalHistoryResource resource) {
        //return new ResponseEntity<>(mapper.toResource(medicalHistoryService.create(jwt, resource)), HttpStatus.CREATED);
    @RequestBody CreateMedicalHistoryResource resource,
          @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7);
        }
        return new ResponseEntity<>(mapper.toResource(medicalHistoryService.create(authorizationHeader,(resource))), HttpStatus.CREATED);
 }

    @Operation(summary = "Update a medical history partially", description = "Updates a medical history partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{medicalHistoryId}")
    public ResponseEntity<MedicalHistoryResource> patchMedicalHistory(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Medical History Id", required = true, examples = @ExampleObject(name = "medicalHistoryId", value = "1")) @PathVariable Integer medicalHistoryId,
            @RequestBody UpdateMedicalHistoryResource request
    ) {
        return new  ResponseEntity<>(mapper.toResource(medicalHistoryService.update(jwt, medicalHistoryId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a medical history", description = "Delete a medical history with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{medicalHistoryId}")
    public ResponseEntity<?> deleteMedicalHistory(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Medical History Id", required = true, examples = @ExampleObject(name = "medicalHistoryId", value = "1")) @PathVariable Integer medicalHistoryId) {
        return medicalHistoryService.delete(jwt, medicalHistoryId);
    }
}
