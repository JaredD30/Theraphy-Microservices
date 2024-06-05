package com.digitalholics.healthexpertiseservice.HealthExpertise.service;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Consultation;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.MedicalHistory;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.MedicalHistoryRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.MedicalHistoryService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.mapping.MedicalHistoryMapper;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.MedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.UpdateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.healthexpertiseservice.Shared.configuration.ExternalConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    private static final String ENTITY = "MedicalHistory";
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;

    private final MedicalHistoryMapper mapper;

    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository, Validator validator, ExternalConfiguration externalConfiguration, MedicalHistoryMapper mapper) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }

    @Override
    public List<MedicalHistory> getAll(String jwt) {
        return medicalHistoryRepository.findAll();
    }

    @Override
    public Page<MedicalHistory> getAll(Pageable pageable) {
        return medicalHistoryRepository.findAll(pageable);
    }

    @Override
    public MedicalHistory getById(Integer medicalHistoryId) {
        return medicalHistoryRepository.findById(medicalHistoryId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, medicalHistoryId));
    }
    @Override
    public MedicalHistory getByPatientId(Integer patientId) {

       MedicalHistory medicalHistory = medicalHistoryRepository.findByPatientId(patientId);

        if(medicalHistory == null)
            throw new ResourceValidationException(ENTITY,
                    "Not found Medical History for this patient");

        return medicalHistory;
    }

    @Override
    public MedicalHistoryResource getResourceByPatientId(String jwt, Integer patientId) {

        MedicalHistoryResource medicalHistory = mapper.toResource(getByPatientId(patientId));

        medicalHistory.setPatient(externalConfiguration.getPatientByID(jwt, medicalHistory.getPatient().getId()));

        if(medicalHistory == null)
            throw new ResourceValidationException(ENTITY,
                    "Not found Medical History for this patient");

        return medicalHistory;
    }



    @Override
    public MedicalHistory create(String jwt, CreateMedicalHistoryResource medicalHistoryResource) {

        Set<ConstraintViolation<CreateMedicalHistoryResource>> violations = validator.validate(medicalHistoryResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = externalConfiguration.getUser(jwt);
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatientId(medicalHistoryResource.getPatientId());
        medicalHistory.setGender(medicalHistoryResource.getGender());
        medicalHistory.setSize(medicalHistoryResource.getSize());
        medicalHistory.setWeight(medicalHistoryResource.getWeight());
        medicalHistory.setBirthplace(medicalHistoryResource.getBirthplace());
        medicalHistory.setHereditaryHistory(medicalHistoryResource.getHereditaryHistory());
        medicalHistory.setNonPathologicalHistory(medicalHistoryResource.getNonPathologicalHistory());
        medicalHistory.setPathologicalHistory(medicalHistoryResource.getPathologicalHistory());


        return medicalHistoryRepository.save(medicalHistory);
    }

    @Override
    public MedicalHistory update(String jwt, Integer medicalHistoryId, UpdateMedicalHistoryResource request) {

        MedicalHistory medicalHistory = getById( medicalHistoryId);

        if(medicalHistory == null)
            throw new ResourceValidationException(ENTITY,
                    "Not found Medical History with ID:"+ medicalHistoryId);

        if (request.getGender() != null) {
            medicalHistory.setGender(request.getGender());
        }
        if (request.getSize() != null) {
            medicalHistory.setSize(request.getSize());
        }
        if (request.getWeight() != null) {
            medicalHistory.setWeight(request.getWeight());
        }
        if (request.getBirthplace() != null) {
            medicalHistory.setBirthplace(request.getBirthplace());
        }
        if (request.getHereditaryHistory() != null) {
            medicalHistory.setHereditaryHistory(request.getHereditaryHistory());
        }
        if (request.getNonPathologicalHistory() != null) {
            medicalHistory.setNonPathologicalHistory(request.getNonPathologicalHistory());
        }
        if (request.getPathologicalHistory() != null) {
            medicalHistory.setPathologicalHistory(request.getPathologicalHistory());
        }

        return medicalHistoryRepository.save(medicalHistory);

    }

    @Override
    public ResponseEntity<?> delete(String jwt, Integer medicalHistoryId) {

        return medicalHistoryRepository.findById(medicalHistoryId).map(medicalHistory -> {
                medicalHistoryRepository.delete(medicalHistory);
                return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,medicalHistoryId));
    }

}
