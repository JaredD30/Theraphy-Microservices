package com.digitalholics.consultationsservice.Consultation.domain.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConsultationService {

    List<Consultation> getAll(String jwt);
    Page<Consultation> getAll(Pageable pageable);
    Consultation getById(String jwt, Integer consultationId);
    List<Consultation> getByPatientId(Integer patientId);
    List<Consultation> getByPhysiotherapistId(Integer physiotherapistId);

    Consultation getConsultationByPhysiotherapistId(Integer physiotherapistId);

    Consultation create(String jwt, CreateConsultationResource consultation);
    Consultation update(String jwt, Integer consultationId, UpdateConsultationResource request);
    ResponseEntity<?> delete(String jwt, Integer ConsultationId);

    Consultation updateDiagnosis(String jwt, Integer consultationId, String diagnosis);

}
