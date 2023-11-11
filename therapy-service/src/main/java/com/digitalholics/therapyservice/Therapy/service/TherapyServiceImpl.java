package com.digitalholics.therapyservice.Therapy.service;

import com.digitalholics.therapyservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.therapyservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.therapyservice.Shared.JwtValidation.JwtValidator;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Patient;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Physiotherapist;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.User;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Therapy;
import com.digitalholics.therapyservice.Therapy.domain.persistence.External.PatientRepository;
import com.digitalholics.therapyservice.Therapy.domain.persistence.External.PhysiotherapistRepository;
import com.digitalholics.therapyservice.Therapy.domain.persistence.External.UserRepository;
import com.digitalholics.therapyservice.Therapy.domain.persistence.TherapyRepository;
import com.digitalholics.therapyservice.Therapy.domain.service.TherapyService;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.UpdateTherapyResource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TherapyServiceImpl implements TherapyService {


    private static final String ENTITY = "Therapy";

    private final TherapyRepository therapyRepository;

    private final PatientRepository patientRepository;

    private final PhysiotherapistRepository physiotherapistRepository;

    private final UserRepository userRepository;

    private final JwtValidator jwtValidator;
    private final Validator validator;

    public TherapyServiceImpl(TherapyRepository therapyRepository, PatientRepository patientRepository, PhysiotherapistRepository physiotherapistRepository, UserRepository userRepository, JwtValidator jwtValidator, Validator validator) {
        this.therapyRepository = therapyRepository;
        this.patientRepository = patientRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.userRepository = userRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }



    @Override
    public List<Therapy> getAll() {
        return therapyRepository.findAll();
    }

    @Override
    public Page<Therapy> getAll(Pageable pageable) {
        return therapyRepository.findAll(pageable);
    }

    @Override
    public List<Therapy> getTherapyByPatientId(Integer patientId) {
        return therapyRepository.findTherapyByPatientId(patientId);
    }

    @Override
    public Therapy getActiveTherapyByPatientId(String jwt) {

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Optional<Patient> patientOptional = patientRepository.findByUserId(user.getId());
        Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("User not found patient for id: " + user.getId()));

        return therapyRepository.findActiveTherapyByPatientId(patient.getId());

    }

    @Override
    public Therapy getById(Integer therapyId) {
        return therapyRepository.findById(therapyId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, therapyId));
    }

    @Override
    public Therapy create(String jwt, CreateTherapyResource therapyResource) {
       Set<ConstraintViolation<CreateTherapyResource>> violations = validator.validate(therapyResource);

       if(!violations.isEmpty())
           throw new ResourceValidationException(ENTITY, violations);

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        Optional<Patient> patientOptional = patientRepository.findById(therapyResource.getPatientId());
        Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));

        Patient patient = patientOptional.orElseThrow(()-> new NotFoundException("This patient not found with ID: "+ therapyResource.getPatientId()));
        Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(()->new NotFoundException("This physiotherapist not found with ID: "+ user.getUsername()));

        Therapy therapy = new Therapy();
        therapy.setTherapyName(therapyResource.getTherapyName());
        therapy.setDescription(therapyResource.getDescription());
        therapy.setAppointmentQuantity(therapyResource.getAppointmentQuantity());
        therapy.setStartAt(therapyResource.getStartAt());
        therapy.setFinishAt(therapyResource.getFinishAt());
        therapy.setFinished(therapyResource.getFinished());
        therapy.setPatient(patient);
        therapy.setPhysiotherapist(physiotherapist);



       return therapyRepository.save(therapy);
    }

    @Override
    public Therapy update(Integer therapyId, UpdateTherapyResource request) {
        Therapy therapy = getById(therapyId);

        if (request.getTherapyName() != null) {
            therapy.setTherapyName(request.getTherapyName());
        }
        if (request.getAppointmentQuantity() != null) {
            therapy.setAppointmentQuantity(request.getAppointmentQuantity());
        }
        if (request.getStartAt() != null) {
            therapy.setStartAt(request.getStartAt());
        }
        if (request.getFinishAt() != null) {
            therapy.setFinishAt(request.getFinishAt());
        }


        return therapyRepository.save(therapy);
    }

    @Override
    public ResponseEntity<?> delete(Integer therapyId) {
        return therapyRepository.findById(therapyId)
                .map(therapy -> {
                    therapyRepository.delete(therapy);
                    return ResponseEntity.ok().build();
                }).orElseThrow(()-> new ResourceNotFoundException(ENTITY, therapyId));
    }
}
