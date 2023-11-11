package com.digitalholics.therapyservice.Therapy.domain.service;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.Treatment;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.UpdateTreatmentResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TreatmentService {

    List<Treatment> getAll();

    Page<Treatment> getAll(Pageable pageable);

    Treatment getById(Integer treatmentId);

    Treatment create(String jwt, CreateTreatmentResource treatmentResource);

    List<Treatment> getTreatmentByTherapyId(Integer therapyId);

    Treatment getTreatmentByDateAndTherapyId(Integer therapyId, String date);

    Treatment update(Integer treatmentId, UpdateTreatmentResource request);

    ResponseEntity<?> delete(Integer treatmentId);

}
