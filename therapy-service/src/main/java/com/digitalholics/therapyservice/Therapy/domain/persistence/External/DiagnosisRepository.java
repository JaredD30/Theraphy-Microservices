package com.digitalholics.therapyservice.Therapy.domain.persistence.External;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Integer> {

    List<Diagnosis> findByPatientId(Integer patientId);


}
