package com.digitalholics.consultationsservice.Consultation.domain.persistence.External;

import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {

    List<Diagnosis> findByPatientId(Integer patientId);


}
