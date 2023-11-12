package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.MedicalHistoryService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.MedicalHistoryMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.MedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.UpdateMedicalHistoryResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/health-expertise/medical-histories", produces = "application/json")
public class MedicalHistoriesController {

    private final MedicalHistoryService medicalHistoryService;
    private final MedicalHistoryMapper mapper;

    public MedicalHistoriesController(MedicalHistoryService medicalHistoryService, MedicalHistoryMapper mapper) {
        this.medicalHistoryService = medicalHistoryService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<MedicalHistoryResource> getAllMedicalHistories(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(medicalHistoryService.getAll(jwt), pageable);
    }

    @GetMapping("{medicalHistoryId}")
    public MedicalHistoryResource getMedicalHistoryById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Medical History Id", required = true, examples = @ExampleObject(name = "medicalHistoryId", value = "1")) @PathVariable Integer medicalHistoryId) {
        return mapper.toResource(medicalHistoryService.getById(jwt, medicalHistoryId));
    }

    @GetMapping("byPatientId/{patientId}")
    public MedicalHistoryResource getMedicalHistoryByPatientId(@RequestHeader("Authorization") String jwt, @PathVariable Integer patientId, Pageable pageable) {
        return mapper.toResource(medicalHistoryService.getByPatientId(jwt, patientId));
    }

    @PostMapping
    public ResponseEntity<MedicalHistoryResource> createMedicalHistory(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @RequestBody CreateMedicalHistoryResource resource) {
        return new ResponseEntity<>(mapper.toResource(medicalHistoryService.create(jwt, resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{medicalHistoryId}")
    public ResponseEntity<MedicalHistoryResource> patchMedicalHistory(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Medical History Id", required = true, examples = @ExampleObject(name = "medicalHistoryId", value = "1")) @PathVariable Integer medicalHistoryId,
            @RequestBody UpdateMedicalHistoryResource request
    ) {
        return new  ResponseEntity<>(mapper.toResource(medicalHistoryService.update(jwt, medicalHistoryId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{medicalHistoryId}")
    public ResponseEntity<?> deleteMedicalHistory(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Medical History Id", required = true, examples = @ExampleObject(name = "medicalHistoryId", value = "1")) @PathVariable Integer medicalHistoryId) {
        return medicalHistoryService.delete(jwt, medicalHistoryId);
    }
}
