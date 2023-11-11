package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.DiagnosisService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.DiagnosisMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.DiagnosisResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/health-expertise/diagnoses", produces = "application/json")
public class DiagnosesController {

    private final DiagnosisService diagnosisService;

    private final DiagnosisMapper mapper;

    public DiagnosesController(DiagnosisService diagnosisService, DiagnosisMapper mapper) {
        this.diagnosisService = diagnosisService;
        this.mapper = mapper;
    }

    @GetMapping
    public DiagnosisResource getLast(@RequestHeader("Authorization") String jwt) {
        return mapper.toResource(diagnosisService.getLast(jwt));
    }

    @GetMapping("byPatientId/{patientId}")
    public Page<DiagnosisResource> getDiagnosisByPatientId(@RequestHeader("Authorization") String jwt, @PathVariable Integer patientId, Pageable pageable) {
        return mapper.modelListPage(diagnosisService.getByPatientId(jwt, patientId), pageable);
    }
}
