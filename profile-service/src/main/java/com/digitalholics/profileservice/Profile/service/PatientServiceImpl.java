package com.digitalholics.profileservice.Profile.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.persistence.PatientRepository;
import com.digitalholics.profileservice.Profile.domain.persistence.External.UserRepository;
import com.digitalholics.profileservice.Profile.domain.service.PatientService;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import com.digitalholics.profileservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.profileservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.profileservice.Shared.JwtValidation.JwtValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class PatientServiceImpl implements PatientService {

;    private static final String ENTITY = "Patient";
    private final PatientRepository patientRepository;
    private final JwtValidator jwtValidator;
    private final Validator validator;



    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, JwtValidator jwtValidator, Validator validator) {
        this.patientRepository = patientRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }

    @Override
    public List<Patient> getAll(String jwt) {
        User user = jwtValidator.validateJwtAndGetUser(jwt, "ADMIN");

        return patientRepository.findAll();
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
    public Patient getByDni(String dni) {
        return patientRepository.findPatientByDni(dni);
    }

    @Override
    public Patient getByUserId(Integer userId) {
        return patientRepository.findByUserId(userId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, userId));
    }

    @Override
    public Patient getLoggedInPatient(String jwt) {

        User user = jwtValidator.validateJwtAndGetUser(jwt,"PATIENT");

        return patientRepository.findPatientsByUserUsername(user.getUsername());
    }

    @Override
    public Patient create(String jwt, CreatePatientResource patientResource) {

        Set<ConstraintViolation<CreatePatientResource>> violations = validator.validate(patientResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Patient patientWithDni = patientRepository.findPatientByDni(patientResource.getDni());

        if(patientWithDni != null)
            throw new ResourceValidationException(ENTITY,
                    "A patient with the same Dni first name already exists.");

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setAge(patientResource.getAge());
        patient.setDni(patientResource.getDni());
        patient.setLocation(patientResource.getLocation());
        patient.setBirthdayDate(patientResource.getBirthdayDate());
        patient.setPhotoUrl(patientResource.getPhotoUrl());
        patient.setAppointmentQuantity(0);

        return patientRepository.save(patient);

    }

    @Override
    public Patient update(String jwt, Integer patientId, UpdatePatientResource request) {
        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Patient patient = getById(patientId);

        if(Objects.equals(user.getUsername(), patient.getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){

            patient.setDni(request.getDni() != null ? request.getDni() : patient.getDni());
            patient.setAge(request.getAge() != null ? request.getAge() : patient.getAge());
            patient.setPhotoUrl(request.getPhotoUrl() != null ? request.getPhotoUrl() : patient.getPhotoUrl());
            patient.setAppointmentQuantity(request.getAppointmentQuantity() != null ? request.getAppointmentQuantity() : patient.getAppointmentQuantity());
            patient.setLocation(request.getLocation() != null ? request.getLocation() : patient.getLocation());
            patient.setBirthdayDate(request.getBirthdayDate() != null ? request.getBirthdayDate() : patient.getBirthdayDate());

            return patientRepository.save(patient);
        }

        throw new ResourceValidationException("JWT",
                "Invalid access.");
    }

    @Override
    public ResponseEntity<?> delete(String jwt, Integer patientId) {

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        return patientRepository.findById(patientId).map(patient -> {
            if(Objects.equals(user.getUsername(), patient.getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){

                patientRepository.delete(patient);
                return ResponseEntity.ok().build();
            }
            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,patientId));

    }

    public User validateJwtAndGetUser(String jwt) {
        return jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");
    }

}

