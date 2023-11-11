package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Diagnosis;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiagnosisService {

    Diagnosis getLast(String jwt);
    List<Diagnosis> getByPatientId(String jwt, Integer patientId);
    Diagnosis create(Diagnosis diagnosis);
    ResponseEntity<?> delete(Integer patientId);
}
