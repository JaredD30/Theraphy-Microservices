package com.digitalholics.profileservice.Profile.domain.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import com.digitalholics.profileservice.Profile.resource.Patient.PatientResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.CreatePhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.PhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.UpdatePhysiotherapistResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.sql.RowSet;
import java.util.List;

public interface PhysiotherapistService {
    List<Physiotherapist> getAll();
    Page<Physiotherapist> getAll(Pageable pageable);

    Page<PhysiotherapistResource> getAllPhysiotherapist(String jwt, Pageable pageable);

    Physiotherapist getById(Integer patientId);

    PhysiotherapistResource getLoggedInPhysiotherapist(String jwt);

    PhysiotherapistResource getResourceById(Integer patientId);
    Physiotherapist getByUserId(Integer userId);
    Physiotherapist create(CreatePhysiotherapistResource physiotherapist, String jwt);
    Physiotherapist update(Integer physiotherapistId, UpdatePhysiotherapistResource request);
    ResponseEntity<?> delete(Integer physiotherapistId);

    Physiotherapist updatePhysiotherapistRating(String jwt, Integer physiotherapistId, Double rating);

    Physiotherapist updatePhysiotherapistConsultationQuantity(String jwt, Integer physiotherapistID, Integer consultation);

    Physiotherapist updatePhysiotherapistPatientQuantity(String jwt, Integer physiotherapistID, Integer patientQuantity);
}
