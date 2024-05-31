package com.digitalholics.therapyservice.Therapy.service;

import com.digitalholics.therapyservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.therapyservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.therapyservice.Shared.Exception.UnauthorizedException;
import com.digitalholics.therapyservice.Shared.configuration.ExternalConfiguration;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.User;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Therapy;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Treatment;
import com.digitalholics.therapyservice.Therapy.domain.persistence.TherapyRepository;
import com.digitalholics.therapyservice.Therapy.domain.persistence.TreatmentRepository;
import com.digitalholics.therapyservice.Therapy.domain.service.TreatmentService;
import com.digitalholics.therapyservice.Therapy.mapping.TreatmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.TreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.UpdateTreatmentResource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TreatmentServiceImpl implements TreatmentService {



    private static final String ENTITY = "Treatment";

    private final TreatmentRepository treatmentRepository;
    private final TherapyRepository therapyRepository;
    private final Validator validator;

    private final ExternalConfiguration externalConfiguration;

    private final TreatmentMapper mapper;


    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, TherapyRepository therapyRepository, Validator validator, ExternalConfiguration externalConfiguration, TreatmentMapper mapper) {
        this.treatmentRepository = treatmentRepository;
        this.therapyRepository = therapyRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }

    @Override
    public List<Treatment> getAll() {
        return treatmentRepository.findAll();
    }

    @Override
    public Page<Treatment> getAll(Pageable pageable) {
        return treatmentRepository.findAll(pageable);
    }

    @Override
    public Page<TreatmentResource> getAllResources(String jwt, Pageable pageable) {
        Page<TreatmentResource> treatments =
                mapper.modelListPage(getAll(), pageable);
        treatments.forEach(treatment -> {
            treatment.getTherapy().setPatient(externalConfiguration.getPatientByID(jwt, treatment.getTherapy().getPatient().getId()));
            treatment.getTherapy().setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, treatment.getTherapy().getPhysiotherapist().getId()));
        });
        return treatments;
    }

    @Override
    public Treatment getById(Integer treatmentId) {
        return treatmentRepository.findById(treatmentId).orElseThrow(() -> new ResourceNotFoundException(ENTITY, treatmentId));
    }

    @Override
    public TreatmentResource getResourceById(String jwt, Integer treatmentId) {
        TreatmentResource treatment = mapper.toResource(getById(treatmentId));
        treatment.getTherapy().setPatient(externalConfiguration.getPatientByID(jwt, treatment.getTherapy().getPatient().getId()));
        treatment.getTherapy().setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, treatment.getTherapy().getPhysiotherapist().getId()));
        return treatment;
    }

//    @Override
//    public Treatment create(Treatment treatment) {
//        Set<ConstraintViolation<Treatment>> violations = validator.validate(treatment);
//
//        if(!violations.isEmpty())
//            throw new ResourceValidationException(ENTITY, violations);
//
//        return treatmentRepository.save(treatment);
//    }
//

    @Override
    public Treatment create(String jwt, CreateTreatmentResource treatmentResource) {
        Set<ConstraintViolation<CreateTreatmentResource>> violations = validator.validate(treatmentResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = externalConfiguration.getUser(jwt);

        Optional<Therapy> therapyOptional = therapyRepository.findById(treatmentResource.getTherapyId());

        Therapy therapy = therapyOptional.orElseThrow(() -> new NotFoundException("Therapy not found with ID: " + treatmentResource.getTherapyId()));

        Treatment treatment = new Treatment();

        if (externalConfiguration.getPhysiotherapistById(jwt, therapy.getPhysiotherapistId()).getUser().getUsername().equals(user.getUsername())){
            treatment.setTherapy(therapy);
            treatment.setDay(treatmentResource.getDay());
            treatment.setDescription(treatmentResource.getDescription());
            treatment.setTitle(treatmentResource.getTitle());
            treatment.setDuration(treatmentResource.getDuration());
            treatment.setViewed(treatmentResource.getViewed());
            treatment.setVideoUrl(treatmentResource.getVideoUrl());
            return treatmentRepository.save(treatment);
        }else {
            throw new UnauthorizedException("You do not have permission to create an treatment for this therapy.");
        }

    }


    @Override
    public List<Treatment> getTreatmentByTherapyId(Integer therapyId) {
        return treatmentRepository.findTreatmentByTherapyId(therapyId);
    }

    @Override
    public Page<TreatmentResource> getResourcesByTherapyId(String jwt, Pageable pageable, Integer theraphyId) {
        Page<TreatmentResource> treatments =
                mapper.modelListPage(getTreatmentByTherapyId(theraphyId), pageable);
        treatments.forEach(treatment -> {
            treatment.getTherapy().setPatient(externalConfiguration.getPatientByID(jwt, treatment.getTherapy().getPatient().getId()));
            treatment.getTherapy().setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, treatment.getTherapy().getPhysiotherapist().getId()));
        });
        return treatments;
    }


    @Override
    public Treatment getTreatmentByDateAndTherapyId(Integer therapyId, String date) {
        return treatmentRepository.findTreatmentByDateAndTherapyId(therapyId, date);
    }


    @Override
    public TreatmentResource getResourceByDateAndTherapyId(String jwt, Integer therapyId, String date) {
        TreatmentResource treatment = mapper.toResource(getTreatmentByDateAndTherapyId(therapyId, date));
        treatment.getTherapy().setPatient(externalConfiguration.getPatientByID(jwt, treatment.getTherapy().getPatient().getId()));
        treatment.getTherapy().setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, treatment.getTherapy().getPhysiotherapist().getId()));
        return treatment;
    }
    @Override
    public Treatment update(Integer treatmentId, UpdateTreatmentResource request) {
        Treatment treatment = getById(treatmentId);
        if (request.getTitle() != null) {
            treatment.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            treatment.setDescription(request.getDescription());
        }
        if (request.getVideoUrl() != null) {
            treatment.setVideoUrl(request.getVideoUrl());
        }
        if (request.getDuration() != null) {
            treatment.setDuration(request.getDuration());
        }
        if (request.getDay() != null) {
            treatment.setDay(request.getDay());
        }
        if (request.getViewed() != null) {
            treatment.setViewed(request.getViewed());
        }



        return treatmentRepository.save(treatment);
    }

    @Override
    public ResponseEntity<?> delete(Integer treatmentId) {
        return treatmentRepository.findById(treatmentId)
                .map(treatment -> {
                    treatmentRepository.delete(treatment);
                    return ResponseEntity.ok().build();
                }).orElseThrow(()-> new ResourceNotFoundException(ENTITY, treatmentId));
    }
}









