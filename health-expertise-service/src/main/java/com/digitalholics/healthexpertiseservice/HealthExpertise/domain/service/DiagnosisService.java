package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Diagnosis;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.CreateDiagnosisResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.DiagnosisResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiagnosisService {

    Diagnosis getLast(String jwt);
    List<Diagnosis> getByPatientId(Integer patientId);
    Page<DiagnosisResource> getResourceByPatientId(String jwt, Pageable pageable, Integer patientId);

    Diagnosis create(String jwt, CreateDiagnosisResource diagnosis);
    ResponseEntity<?> delete(Integer patientId);
}
