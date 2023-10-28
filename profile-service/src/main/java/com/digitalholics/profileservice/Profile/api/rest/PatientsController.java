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
        return mapper.modelListPage(patientService.getAll(), pageable);
    }

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

    @PostMapping("registration-patient")
    public ResponseEntity<PatientResource> createPatient(@RequestHeader("Authorization") String jwt, @RequestBody CreatePatientResource resource) {
        return new ResponseEntity<>(mapper.toResource(patientService.create(jwt,resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{patientId}")
    public ResponseEntity<PatientResource> patchPatient(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer patientId,
            @RequestBody UpdatePatientResource request) {

        return new  ResponseEntity<>(mapper.toResource(patientService.update(jwt, patientId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{patientId}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String jwt, @PathVariable Integer patientId) {
        return patientService.delete(jwt, patientId);
    }
}