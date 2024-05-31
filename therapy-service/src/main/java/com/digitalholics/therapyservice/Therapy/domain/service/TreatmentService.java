package com.digitalholics.therapyservice.Therapy.domain.service;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.Treatment;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.TreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.UpdateTreatmentResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TreatmentService {

    List<Treatment> getAll();

    Page<Treatment> getAll(Pageable pageable);

    Page<TreatmentResource> getAllResources(String jwt, Pageable pageable);

    Treatment getById(Integer treatmentId);

    TreatmentResource getResourceById(String jwt, Integer treatmentId);

    Treatment create(String jwt, CreateTreatmentResource treatmentResource);

    List<Treatment> getTreatmentByTherapyId(Integer therapyId);

    Page<TreatmentResource> getResourcesByTherapyId(String jwt, Pageable pageable, Integer theraphyId);

    Treatment getTreatmentByDateAndTherapyId(Integer therapyId, String date);

    TreatmentResource getResourceByDateAndTherapyId(String jwt, Integer therapyId, String date);

    Treatment update(Integer treatmentId, UpdateTreatmentResource request);

    ResponseEntity<?> delete(Integer treatmentId);

}
