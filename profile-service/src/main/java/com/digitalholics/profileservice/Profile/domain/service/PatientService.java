package com.digitalholics.profileservice.Profile.domain.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.PatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface PatientService {
    Page<Patient> getAll(Pageable pageable);
    Patient getById( Integer patientId);

    PatientResource getLoggedInPatient(String jwt);

    Patient getByDni(String dni);
    Patient getByUserId( Integer userId);
    PatientResource getResourceById(Integer id);
    Patient create(CreatePatientResource patient,String jwt);
    Patient update(Integer patientId, UpdatePatientResource request);
    ResponseEntity<?> delete(Integer patientId);

    Patient updatePatientAppointmentQuantity(String jwt, Integer patientId, Integer appointmentQuantity);
}
