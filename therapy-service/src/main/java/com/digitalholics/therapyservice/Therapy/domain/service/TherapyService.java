package com.digitalholics.therapyservice.Therapy.domain.service;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.Therapy;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.UpdateTherapyResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TherapyService {

    List<Therapy> getAll();

    Page<TherapyResource> getAllResources(String jwt, Pageable pageable);

    Page<Therapy> getAll(Pageable pageable);
    List<Therapy> getTherapyByPatientId(Integer patientId);

    Page<TherapyResource> getResourceByPatientId(String jwt, Pageable pageable, Integer patientId);

    Therapy getActiveTherapyByPatientId(String jwt);

    TherapyResource getResourceActiveByPatientId(String jwt);

    Therapy getById(Integer therapyId);

    TherapyResource getResourceById(String jwt, Integer therapyId);

    Therapy create(String jwt, CreateTherapyResource therapy);
    Therapy update(Integer therapyId, UpdateTherapyResource request);

    ResponseEntity<?> delete(Integer therapyId);

    Therapy getTherapyByPhysiotherapistIdAndPatientId(Integer physiotherapistId, Integer patientId);

    TherapyResource getResourceByPhysiotherapistIdAndPatientId(String jwt, Integer physiotherapistId, Integer patientId);
}
