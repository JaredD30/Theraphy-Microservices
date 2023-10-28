package com.digitalholics.profileservice.Profile.domain.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.ExternalEntities.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.sql.RowSet;
import java.util.List;

public interface PatientService {
    List<Patient> getAll();
    Page<Patient> getAll(Pageable pageable);
    Patient getById( Integer patientId);
    Patient getByDni(String dni);
    Patient getByUserId( Integer userId);

    Patient getLoggedInPatient(String jwt);

    Patient create( String jwt, CreatePatientResource patient);
    Patient update(String jwt, Integer patientId, UpdatePatientResource request);
    ResponseEntity<?> delete(String jwt, Integer patientId);
    User validateJwtAndGetUser(String jwt);
}
