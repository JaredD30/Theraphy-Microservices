package com.digitalholics.profileservice.Profile.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.persistence.PatientRepository;
import com.digitalholics.profileservice.Profile.domain.service.PatientService;
import com.digitalholics.profileservice.Profile.mapping.PatientMapper;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.PatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import com.digitalholics.profileservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.profileservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.profileservice.Shared.configuration.ExternalConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class PatientServiceImpl implements PatientService {

    private static final String ENTITY = "Patient";
    private final PatientRepository patientRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;
    private final PatientMapper mapper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, Validator validator, ExternalConfiguration externalConfiguration, PatientMapper mapper) {
        this.patientRepository = patientRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }

    @Override
    public Page<Patient> getAll( Pageable pageable) {
            return patientRepository.findAll(pageable);
    }

    @Override
    public Patient getById(Integer patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, patientId));
    }

    @Override
    public PatientResource getResourceById(Integer patientId) {
        Patient patient = getById(patientId);
        PatientResource patientResource = mapper.toResource(patient);
        User user = externalConfiguration.getUserById(patient.getUserId());
        patientResource.setUser(user);
        return patientResource;
    }

    @Override
    public PatientResource getLoggedInPatient(String jwt) {
        User user = externalConfiguration.getUser(jwt);
        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PATIENT")) {
            Optional<Patient> patientOptional = patientRepository.findByUserId(user.getId());
            Patient patient = patientOptional.orElseThrow(() -> new ResourceNotFoundException("No se encontró un paciente autenticado."));
            PatientResource patientResource  = mapper.toResource(patient);
            patientResource.setUser(user);
            return patientResource;
        }
        throw new ResourceNotFoundException("No se encontró un paciente autenticado.");
    }

    @Override
    public Patient getByDni(String dni) {
        return patientRepository.findPatientByDni(dni);
    }

    @Override
    public Patient getByUserId(Integer userId) {
        return patientRepository.findByUserId(userId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, userId));
    }

    @Override
    public Patient create(CreatePatientResource patientResource, String jwt) {

        Set<ConstraintViolation<CreatePatientResource>> violations = validator.validate(patientResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Patient patientWithDni = patientRepository.findPatientByDni(patientResource.getDni());

        if(patientWithDni != null)
            throw new ResourceValidationException(ENTITY,
                    "A patient with the same Dni first name already exists.");

        User user = externalConfiguration.getUser(jwt);

        System.out.printf(String.valueOf(user));

        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PATIENT")) {
            Patient patient = new Patient();
            patient.setAge(patientResource.getAge());
            patient.setDni(patientResource.getDni());
            patient.setLocation(patientResource.getLocation());
            patient.setBirthdayDate(patientResource.getBirthdayDate());
            patient.setPhotoUrl(patientResource.getPhotoUrl());
            patient.setAppointmentQuantity(0);
            patient.setUserId(user.getId());

            return patientRepository.save(patient);
        } else {
            throw new ResourceValidationException(ENTITY,
                    "Patient not crate, because you are not a patient.");
        }
    }

    @Override
    public Patient update(Integer patientId, UpdatePatientResource request) {
        Patient patient = getById(patientId);
        patient.setDni(request.getDni() != null ? request.getDni() : patient.getDni());
        patient.setAge(request.getAge() != null ? request.getAge() : patient.getAge());
        patient.setPhotoUrl(request.getPhotoUrl() != null ? request.getPhotoUrl() : patient.getPhotoUrl());
        patient.setAppointmentQuantity(request.getAppointmentQuantity() != null ? request.getAppointmentQuantity() : patient.getAppointmentQuantity());
        patient.setLocation(request.getLocation() != null ? request.getLocation() : patient.getLocation());
        patient.setBirthdayDate(request.getBirthdayDate() != null ? request.getBirthdayDate() : patient.getBirthdayDate());
        return patientRepository.save(patient);
    }

    @Override
    public ResponseEntity<?> delete(Integer patientId) {
        Patient patient = patientRepository.findPatientById(patientId);
        patientRepository.delete(patient);
        return ResponseEntity.ok().build();
    }

    @Override
    public Patient updatePatientAppointmentQuantity(String jwt, Integer patientId, Integer appointmentQuantity) {
        Patient patient = patientRepository.findPatientById(patientId);
        patient.setAppointmentQuantity(patient.getAppointmentQuantity() + appointmentQuantity);
        return patientRepository.save(patient);
    }

}

