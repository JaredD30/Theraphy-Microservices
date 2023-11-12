package com.digitalholics.profileservice.Profile.api.rest;



import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.service.PatientService;
import com.digitalholics.profileservice.Profile.mapping.PatientMapper;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.PatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/profile/patients", produces = "application/json")
public class PatientsController {
    private final PatientService patientService;

    private final PatientMapper mapper;

    public PatientsController(PatientService patientService, PatientMapper mapper) {
        this.patientService = patientService;
        this.mapper = mapper;
    }

    @GetMapping("/profile")
    public PatientResource getLoggedInPatientProfile(@RequestHeader("Authorization") String jwt) {
        return mapper.toResource(patientService.getLoggedInPatient(jwt));
    }


    @GetMapping
    public Page<PatientResource> getAllPatients(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(patientService.getAll(jwt), pageable);
    }

    @Operation(summary = "Get patient by id", description = "Returns patient with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{patientId}")
    public PatientResource getPatientById(@RequestHeader("Authorization") String jwt, @PathVariable Integer patientId) {
        return mapper.toResource(patientService.getById( patientId));
    }

    @GetMapping("byUserId/{userId}")
    public PatientResource getPatientByUserId(@RequestHeader("Authorization") String jwt, @PathVariable Integer userId) {
        return mapper.toResource(patientService.getByUserId(userId));
    }


    @GetMapping("/validate-jwt")
    public ResponseEntity<String> validateJwtAndReturnUsername(@RequestHeader("Authorization") String jwt) {

        String username = patientService.validateJwtAndGetUser(jwt).getUsername();

        if (username != null) {
            return new ResponseEntity<>(username, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Registration patient", description = "Register a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("registration-patient")
    public ResponseEntity<PatientResource> createPatient(@RequestHeader("Authorization") String jwt, @RequestBody CreatePatientResource resource) {
        return new ResponseEntity<>(mapper.toResource(patientService.create(jwt,resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{patientId}")
    public ResponseEntity<PatientResource> patchPatient(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId,
            @RequestBody UpdatePatientResource request) {

        return new  ResponseEntity<>(mapper.toResource(patientService.update(jwt, patientId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{patientId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId) {
        return patientService.delete(jwt, patientId);
    }
}