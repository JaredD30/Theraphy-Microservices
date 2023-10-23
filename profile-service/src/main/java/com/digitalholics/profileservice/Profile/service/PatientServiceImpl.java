package com.digitalholics.profileservice.Profile.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.model.entity.User.User;
import com.digitalholics.profileservice.Profile.domain.persistence.PatientRepository;
import com.digitalholics.profileservice.Profile.domain.service.PatientService;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.UpdatePatientResource;
import com.digitalholics.profileservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.profileservice.Shared.Exception.ResourceValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PatientServiceImpl implements PatientService {

    private static final String ENTITY = "Patient";

    private final PatientRepository patientRepository;


    //private final Validator validator;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
        //this.validator = validator;
    }

    @Override
    public List<Patient> getAll() {
        return patientRepository.findAll();
    }

    @Override
    public Page<Patient> getAll(Pageable pageable) {
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
    public Patient getLoggedInPatient() {
        /*Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            return patientRepository.findPatientsByUserUsername(username);
        }*/
        throw new ResourceNotFoundException("No se encontró un paciente autenticado.");
    }

    @Override
    public Patient create(CreatePatientResource patientResource) {
       // Set<ConstraintViolation<CreatePatientResource>> violations = validator.validate(patientResource);

        //if (!violations.isEmpty())
            //throw new ResourceValidationException(ENTITY, violations);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //Optional<User> userOptional = userRepository.findByUsername(username);

        //User user = userOptional.orElseThrow(() -> new NotFoundException("User not found for username: " + username));



        Patient patientWithDni = patientRepository.findPatientByDni(patientResource.getDni());

        if(patientWithDni != null)
            throw new ResourceValidationException(ENTITY,
                    "A patient with the same Dni first name already exists.");

        Patient patient = new Patient();
        patient.setUserId(patientResource.getUserId());
        patient.setAge(patientResource.getAge());
        patient.setDni(patientResource.getDni());
        patient.setLocation(patientResource.getLocation());
        patient.setBirthdayDate(patientResource.getBirthdayDate());
        patient.setPhotoUrl(patientResource.getPhotoUrl());
        patient.setAppointmentQuantity(0);

        return patientRepository.save(patient);

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
        return patientRepository.findById(patientId).map(patient -> {
            patientRepository.delete(patient);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,patientId));
    }


}