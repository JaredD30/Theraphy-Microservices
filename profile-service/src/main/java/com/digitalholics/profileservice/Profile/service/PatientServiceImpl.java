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

@Service
public class PatientServiceImpl implements PatientService {

;    private static final String ENTITY = "Patient";

    private final PatientRepository patientRepository;

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;


    //private final Validator validator;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        //this.validator = validator;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Patient> getAll() {
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

        User user = validateJwtAndGetUser(jwt);

        return patientRepository.findPatientsByUserUsername(user.getUsername());
    }

    @Override
    public Patient create(String jwt, CreatePatientResource patientResource) {

        User user = validateJwtAndGetUser(jwt);

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
        User user = validateJwtAndGetUser(jwt);

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

        User user = validateJwtAndGetUser(jwt);

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
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita los primeros 7 caracteres ("Bearer ")
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        String validationEndpointUrl = "http://security-service/api/v1/security/auth/validate-jwt";
        ResponseEntity<String> responseEntity = restTemplate.exchange(validationEndpointUrl, HttpMethod.GET, requestEntity, String.class);

        Optional<User> userOptional = userRepository.findByUsername(responseEntity.getBody());

        User user = userOptional.orElseThrow(() -> new NotFoundException("User not found for username: " + responseEntity.getBody()));

        if(Objects.equals(String.valueOf(user.getRole()), "PATIENT")
                || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){
            return user;
        }

        throw new ResourceValidationException("JWT",
                "Invalid rol.");
    }

}

