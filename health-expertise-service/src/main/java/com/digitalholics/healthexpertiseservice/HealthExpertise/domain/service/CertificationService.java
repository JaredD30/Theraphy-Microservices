package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.CreateCertificationResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Certification.UpdateCertificationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CertificationService {
    List<Certification> getAll();
    Page<Certification> getAll(Pageable pageable);
    Certification getById(Integer certificationId);

    List<Certification> getByPhysiotherapistId(Integer physiotherapistId);

    Certification create(String jwt, CreateCertificationResource certification);
    Certification update(String jwt, Integer certificationId, UpdateCertificationResource request);

    Certification updateTitleSchoolYear(String jwt, Integer certificationId, UpdateCertificationResource request);

    ResponseEntity<?> delete(String jwt, Integer certificationId);
}
