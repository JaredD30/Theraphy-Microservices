package com.digitalholics.consultationsservice.Consultation.domain.persistence;

import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    Consultation findConsultationById(Integer consultationId);
    Consultation findByTopic(String topic);

    Consultation findConsultationByPhysiotherapistId(Integer physiotherapistId);

    List<Consultation> findByPatientId(Integer patientId);

    List<Consultation> findByPhysiotherapistId(Integer physiotherapistId);



}
