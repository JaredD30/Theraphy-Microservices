package com.digitalholics.healthexpertiseservice.HealthExpertise.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Diagnosis;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Patient;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.DiagnosisRepository;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.DiagnosisService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.DiagnosisMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.CreateDiagnosisResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Diagnosis.DiagnosisResource;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.healthexpertiseservice.Shared.configuration.ExternalConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class DiagnosisServiceImpl implements DiagnosisService {
    private static final String ENTITY = "Diagnosis";
    private final DiagnosisRepository diagnosisRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;

    private final DiagnosisMapper mapper;


    public DiagnosisServiceImpl(DiagnosisRepository diagnosisRepository, Validator validator, ExternalConfiguration externalConfiguration, DiagnosisMapper mapper) {
        this.diagnosisRepository = diagnosisRepository;
        this.externalConfiguration = externalConfiguration;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public Diagnosis getLast(String jwt) {

        //User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");
        User user = externalConfiguration.getUser(jwt);

        Patient patient = externalConfiguration.getPatientByUserId(jwt,user.getId());
        List<Diagnosis> allDiagnoses = diagnosisRepository.findByPatientId(patient.getId());

        if (allDiagnoses.isEmpty()) {
            throw new ResourceNotFoundException("Diagnoses not found.");
        }

        allDiagnoses.sort(Comparator.comparing(Diagnosis::getId).reversed());

        return allDiagnoses.get(0);
    }


    @Override
    public List<Diagnosis> getByPatientId(Integer patientId) {

        List<Diagnosis> diagnosis = diagnosisRepository.findByPatientId(patientId);

        if(diagnosis.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Diagnosis for this patient");

        return diagnosis;
    }

    @Override
    public Page<DiagnosisResource> getResourceByPatientId(String jwt, Pageable pageable, Integer patientId) {
        Page<DiagnosisResource> diagnoses =
                mapper.modelListPage(getByPatientId(patientId), pageable);


        diagnoses.forEach(diagnosisResource -> {
            diagnosisResource.setPatient(externalConfiguration.getPatientByID(jwt, diagnosisResource.getPatient().getId()));
            diagnosisResource.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, diagnosisResource.getPhysiotherapist().getId()));
        });

        return diagnoses;
    }


    @Override
    public Diagnosis create(String jwt, CreateDiagnosisResource diagnosisResource) {

        Set<ConstraintViolation<CreateDiagnosisResource>> violations = validator.validate(diagnosisResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = externalConfiguration.getUser(jwt);

        Physiotherapist physiotherapist =  externalConfiguration.getPhysiotherapistByUserId(jwt, user.getId());
        Patient patient = externalConfiguration.getPatientByID(jwt, diagnosisResource.getPatientId());
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPhysiotherapistId(physiotherapist.getId());
        diagnosis.setPatientId(patient.getId());
        diagnosis.setDiagnosis(diagnosisResource.getDiagnosis());
        diagnosis.setDate(diagnosisResource.getDate());

        return diagnosisRepository.save(diagnosis);
    }

    @Override
    public ResponseEntity<?> delete(Integer diagnosisId) {
        return diagnosisRepository.findById(diagnosisId).map(diagnosis -> {
            diagnosisRepository.delete(diagnosis);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,diagnosisId));
    }

}
