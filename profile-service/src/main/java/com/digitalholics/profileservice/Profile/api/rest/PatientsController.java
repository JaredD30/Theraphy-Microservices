package com.digitalholics.profileservice.Profile.api.rest;



import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.service.PatientService;
import com.digitalholics.profileservice.Profile.mapping.PatientMapper;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.PatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
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

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/profile/patients", produces = "application/json")
@Tag(name = "Patients", description = "Patients operations: profile, listing, retrieval, validation, registration, update, and deletion")
public class PatientsController {

    private final PatientService patientService;

    private final PatientMapper mapper;

    public PatientsController(PatientService patientService, PatientMapper mapper) {
        this.patientService = patientService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get patient by id", description = "Returns patient with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{patientId}")
    public PatientResource getPatientById(
            @PathVariable Integer patientId
    ) {
        return patientService.getResourceById(patientId);
    }

    @GetMapping("/PatientLogget")
    public PatientResource getPatientLogget(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Quita "Bearer " del token
        }
        return patientService.getLoggedInPatient(authorizationHeader);
    }

    @Operation(summary = "Get patient by user id", description = "Returns patient with a provide user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byUserId/{userId}")
    public PatientResource getPatientByUserId(
            @Parameter(description = "User Id", required = true, examples = @ExampleObject(name = "userId", value = "1")) @PathVariable Integer userId
    ) {
        return mapper.toResource(patientService.getByUserId(userId));
    }
    @Operation(summary = "Registration patient", description = "Register a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("registration-patient")
    public ResponseEntity<PatientResource> createPatient(
            @RequestBody CreatePatientResource resource,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Quita "Bearer " del token
        }
        return new ResponseEntity<>(mapper.toResource(patientService.create(resource, authorizationHeader)), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a patient partially", description = "Updates a patient with a provided patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{patientId}")
    public ResponseEntity<PatientResource> patchPatient(
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId,
            @RequestBody UpdatePatientResource request) {

        return new  ResponseEntity<>(mapper.toResource(patientService.update(patientId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a patient", description = "Delete a patient with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{patientId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId) {
        return patientService.delete(patientId);
    }

    @PostMapping("/{patientId}/appointmentQuantity")
    public Patient updatePhysiotherapistPatientsQuantity(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer patientId,
            @RequestBody Integer appointmentQuantity) {
        return patientService.updatePatientAppointmentQuantity(jwt, patientId, appointmentQuantity);
    }

}