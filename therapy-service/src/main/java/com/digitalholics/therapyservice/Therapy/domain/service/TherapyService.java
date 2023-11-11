package com.digitalholics.therapyservice.Therapy.domain.service;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.Therapy;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.UpdateTherapyResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TherapyService {

    List<Therapy> getAll();

    Page<Therapy> getAll(Pageable pageable);
    List<Therapy> getTherapyByPatientId(Integer patientId);

    Therapy getActiveTherapyByPatientId(String jwt);

    Therapy getById(Integer therapyId);

    Therapy create(String jwt, CreateTherapyResource therapy);
    Therapy update(Integer therapyId, UpdateTherapyResource request);

    ResponseEntity<?> delete(Integer therapyId);

}
