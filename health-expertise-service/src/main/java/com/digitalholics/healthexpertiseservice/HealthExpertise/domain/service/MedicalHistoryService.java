package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.MedicalHistory;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.MedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.UpdateMedicalHistoryResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MedicalHistoryService {

    List<MedicalHistory> getAll(String jwt);
    Page<MedicalHistory> getAll(Pageable pageable);
    MedicalHistory getById( Integer medicalHistoryId);
    MedicalHistory getByPatientId(Integer patientId);
    MedicalHistoryResource getResourceByPatientId(String jwt, Integer patientId);
    MedicalHistory create(String jwt, CreateMedicalHistoryResource medicalHistory);
    MedicalHistory update(String jwt, Integer medicalHistoryId, UpdateMedicalHistoryResource request);
    ResponseEntity<?> delete(String jwt, Integer medicalHistoryId);
}
