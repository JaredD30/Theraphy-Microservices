package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
    Consultation findByTopic(String topic);

    Consultation findConsultationByPhysiotherapistId(Integer physiotherapistId);

    List<Consultation> findByPatientId(Integer patientId);

    List<Consultation> findByPhysiotherapistId(Integer physiotherapistId);


}
