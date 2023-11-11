package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory,Integer> {

    MedicalHistory findByPatientId(Integer patientId);
}
