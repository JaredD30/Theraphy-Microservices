package com.digitalholics.consultationsservice.Consultation.domain.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.resource.ConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ConsultationService {

    List<Consultation> getAll(String jwt);
    Page<Consultation> getAll(Pageable pageable);
    Consultation getById(Integer consultationId);


    Page<ConsultationResource> getResourceByPhysiotherapistId(String jwt, Integer physiotherapistId, Pageable pageable);

    List<Consultation> getByPatientId(Integer patientId);

    ConsultationResource getResourceById(String jwt, Integer patientId);

    List<Consultation> getByPhysiotherapistId(Integer physiotherapistId);

    Page<ConsultationResource> getResourceByPatientId(String jwt, Integer patientId, Pageable pageable);

    Consultation getConsultationByPhysiotherapistId(Integer physiotherapistId);

    Consultation create(CreateConsultationResource consultation, String jwt);
    Consultation update(Integer consultationId, UpdateConsultationResource request);
    ResponseEntity<?> delete(Integer ConsultationId);

    Consultation updateDiagnosis(Integer consultationId, String diagnosis);

}
