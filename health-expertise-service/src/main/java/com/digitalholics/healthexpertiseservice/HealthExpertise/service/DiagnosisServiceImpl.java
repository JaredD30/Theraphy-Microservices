package com.digitalholics.healthexpertiseservice.HealthExpertise.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Diagnosis;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Consultation;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Patient;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.DiagnosisRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.ConsultationRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.PatientRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.PhysiotherapistRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.UserRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.DiagnosisService;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.healthexpertiseservice.Shared.JwtValidation.JwtValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {
    private static final String ENTITY = "Diagnosis";

    private final DiagnosisRepository diagnosisRepository;

    private final PhysiotherapistRepository physiotherapistRepository;
    private final PatientRepository patientRepository;
    private final ConsultationRepository consultationRepository;
    private final JwtValidator jwtValidator;
    private final Validator validator;


    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository, PhysiotherapistRepository physiotherapistRepository, PatientRepository patientRepository, ConsultationRepository consultationRepository, JwtValidator jwtValidator, Validator validator) {
        this.diagnosisRepository = diagnosisRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.patientRepository = patientRepository;
        this.consultationRepository = consultationRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }

    @Override
    public Diagnosis getLast(String jwt) {

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Optional<Patient> patientOptional = Optional.ofNullable(patientRepository.findPatientsByUserUsername(user.getUsername()));
        Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));


        List<Diagnosis> allDiagnoses = diagnosisRepository.findByPatientId(patient.getId());

        if (allDiagnoses.isEmpty()) {
            throw new ResourceNotFoundException("Diagnoses not found.");
        }

        allDiagnoses.sort(Comparator.comparing(Diagnosis::getId).reversed());

        return allDiagnoses.get(0);
    }

    @Override
    public List<Diagnosis> getByPatientId(String jwt, Integer patientId) {

        User user = jwtValidator.validateJwtAndGetUserNoRol(jwt);

        if(Objects.equals(String.valueOf(user.getRole()), "PATIENT")){
            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with ID: " + patientId));

            if(Objects.equals(String.valueOf(user.getUsername()), patient.getUser().getUsername())){
                List<Diagnosis> diagnosis = diagnosisRepository.findByPatientId(patientId);

                if(diagnosis.isEmpty())
                    throw new ResourceValidationException(ENTITY,
                            "Not found Diagnosis for this patient");

                return diagnosis;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");

        }

        if(Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")){
            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with ID: " + patientId));

            Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));
            Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

            List<Consultation> myConsultations = consultationRepository.findByPhysiotherapistId(physiotherapist.getId());


            boolean isMyPatient = false;

            for (Consultation consultation : myConsultations) {
                if (Objects.equals(consultation.getPatient().getUser().getUsername(), patient.getUser().getUsername())) {

                        isMyPatient = true;
                        break;
                }
            }

            if (isMyPatient) {
                List<Diagnosis> diagnosis = diagnosisRepository.findByPatientId(patientId);

                if(diagnosis.isEmpty())
                    throw new ResourceValidationException(ENTITY,
                            "Not found Diagnosis for this patient");

                return diagnosis;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }

        List<Diagnosis> diagnosis = diagnosisRepository.findByPatientId(patientId);

        if(diagnosis.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Diagnosis for this patient");

        return diagnosis;
    }

    @Override
    public Diagnosis create(Diagnosis diagnosisResource) {

        Set<ConstraintViolation<Diagnosis>> violations = validator.validate(diagnosisResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        return diagnosisRepository.save(diagnosisResource);
    }

    @Override
    public ResponseEntity<?> delete(Integer diagnosisId) {
        return diagnosisRepository.findById(diagnosisId).map(diagnosis -> {
            diagnosisRepository.delete(diagnosis);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,diagnosisId));
    }

}
