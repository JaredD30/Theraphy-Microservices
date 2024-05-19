package com.digitalholics.profileservice.Profile.domain.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientService {
    Page<Patient> getAll(Pageable pageable);
    Patient getById( Integer patientId);
    Patient getByDni(String dni);
    Patient getByUserId( Integer userId);

    Patient create(CreatePatientResource patient);
    Patient update(Integer patientId, UpdatePatientResource request);
    ResponseEntity<?> delete(Integer patientId);
}
