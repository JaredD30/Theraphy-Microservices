package com.digitalholics.healthexpertiseservice.HealthExpertise.api.rest;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.CertificationService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.CertificationMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.UpdateCertificationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/health-expertise/certifications", produces = "application/json")
public class CertificationsController {
    private final CertificationService certificationService;

    private final CertificationMapper mapper;


    public CertificationsController(CertificationService certificationService, CertificationMapper mapper) {
        this.certificationService = certificationService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<CertificationResource> getAllCertifications(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(certificationService.getAll(), pageable);
    }

    @GetMapping("{certificationId}")
    public CertificationResource getCertificationById(@RequestHeader("Authorization") String jwt, @PathVariable Integer certificationId) {
        return mapper.toResource(certificationService.getById(certificationId));
    }

    @GetMapping("byPhysiotherapistId/{physiotherapistId}")
    public Page<CertificationResource> getCertificationsByPhysiotherapistId(@RequestHeader("Authorization") String jwt, @PathVariable Integer physiotherapistId, Pageable pageable) {
        return mapper.modelListPage(certificationService.getByPhysiotherapistId(physiotherapistId), pageable);
    }

    @PostMapping
    public ResponseEntity<CertificationResource> createCertification(@RequestHeader("Authorization") String jwt, @RequestBody CreateCertificationResource resource) {
        return new ResponseEntity<>(mapper.toResource(certificationService.create(jwt, resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{certificationId}")
    public ResponseEntity<CertificationResource> patchCertification(@RequestHeader("Authorization") String jwt, @PathVariable Integer certificationId,
                                                                    @RequestBody UpdateCertificationResource request) {

        return new ResponseEntity<>(mapper.toResource(certificationService.update(jwt, certificationId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{certificationId}")
    public ResponseEntity<?> deleteCertification(@RequestHeader("Authorization") String jwt, @PathVariable Integer certificationId) {
        return certificationService.delete(jwt, certificationId);
    }

}
