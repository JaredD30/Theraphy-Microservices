package com.digitalholics.therapyservice.Therapy.service;

import com.digitalholics.therapyservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.therapyservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.therapyservice.Shared.configuration.ExternalConfiguration;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Consultation;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Patient;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Physiotherapist;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.User;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Therapy;

import com.digitalholics.therapyservice.Therapy.domain.persistence.TherapyRepository;
import com.digitalholics.therapyservice.Therapy.domain.service.TherapyService;
import com.digitalholics.therapyservice.Therapy.mapping.TherapyMapper;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.UpdateTherapyResource;
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
public class TherapyServiceImpl implements TherapyService {


    private static final String ENTITY = "Therapy";

    private final TherapyRepository therapyRepository;

    private final Validator validator;

    private final ExternalConfiguration externalConfiguration;


    private final TherapyMapper mapper;


    public TherapyServiceImpl(TherapyRepository therapyRepository, Validator validator, ExternalConfiguration externalConfiguration, TherapyMapper mapper) {
        this.therapyRepository = therapyRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }



    @Override
    public List<Therapy> getAll() {
        return therapyRepository.findAll();
    }

    @Override
    public Page<TherapyResource> getAllResources(String jwt, Pageable pageable) {
        Page<TherapyResource> theraphies =
        mapper.modelListPage(getAll(), pageable);
        theraphies.forEach(therapyResource -> {
            therapyResource.setPatient(externalConfiguration.getPatientByID(jwt, therapyResource.getPatient().getId()));
            therapyResource.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, therapyResource.getPhysiotherapist().getId()));
        });

        return theraphies;
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
    public Page<TherapyResource> getResourceByPatientId(String jwt, Pageable pageable, Integer patientId) {
        Page<TherapyResource> theraphies =
                mapper.modelListPage(getTherapyByPatientId(patientId), pageable);
        theraphies.forEach(therapyResource -> {
            therapyResource.setPatient(externalConfiguration.getPatientByID(jwt, therapyResource.getPatient().getId()));
            therapyResource.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, therapyResource.getPhysiotherapist().getId()));
        });

        return theraphies;
    }



    @Override
    public Therapy getActiveTherapyByPatientId(String jwt) {

        User user = externalConfiguration.getUser(jwt);

        Optional<Patient> patientOptional = Optional.ofNullable(externalConfiguration.getPatientByUserId(jwt, user.getId()));
        Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("User not found patient for id: " + user.getId()));

        return therapyRepository.findActiveTherapyByPatientId(patient.getId());

    }

    @Override
    public TherapyResource getResourceActiveByPatientId(String jwt) {
        TherapyResource therapy = mapper.toResource(getActiveTherapyByPatientId(jwt));
        therapy.setPatient(externalConfiguration.getPatientByID(jwt, therapy.getPatient().getId()));
        therapy.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, therapy.getPhysiotherapist().getId()));
        return therapy;
    }



    @Override
    public Therapy getById(Integer therapyId) {
        return therapyRepository.findById(therapyId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITY, therapyId));
    }

    @Override
    public TherapyResource getResourceById(String jwt, Integer therapyId) {
        TherapyResource therapy = mapper.toResource(getById(therapyId));
        therapy.setPatient(externalConfiguration.getPatientByID(jwt, therapy.getPatient().getId()));
        therapy.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, therapy.getPhysiotherapist().getId()));
        return therapy;
    }

    @Override
    public Therapy create(String jwt, CreateTherapyResource therapyResource) {
       Set<ConstraintViolation<CreateTherapyResource>> violations = validator.validate(therapyResource);

       if(!violations.isEmpty())
           throw new ResourceValidationException(ENTITY, violations);

        User user = externalConfiguration.getUser(jwt);

        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")) {

            Physiotherapist physiotherapist = externalConfiguration.getPhysiotherapistByUserId(jwt,user.getId());
            List<Consultation> consultation = externalConfiguration.getConsultationByPhysiotherapyId(jwt,physiotherapist.getId());
            Boolean bolConsult = false;
            for (Consultation existingConsultation : consultation) {
                if (existingConsultation.getPatientId().getId().equals(therapyResource.getPatientId()) && existingConsultation.getDone()) {
                    bolConsult = true;
                }
            }

            if (bolConsult) {
                Therapy therapy = new Therapy();
                therapy.setTherapyName(therapyResource.getTherapyName());
                therapy.setDescription(therapyResource.getDescription());
                therapy.setAppointmentQuantity(therapyResource.getAppointmentQuantity());
                therapy.setStartAt(therapyResource.getStartAt());
                therapy.setFinishAt(therapyResource.getFinishAt());
                therapy.setFinished(therapyResource.getFinished());
                therapy.setPatientId(therapyResource.getPatientId());
                therapy.setPhysiotherapistId(physiotherapist.getId());

                return therapyRepository.save(therapy);
            } else {
                throw new ResourceValidationException(ENTITY,
                        "Therapy not created, because You haven't done a consultation with physiotherapist yet.");
            }

        } else {
            throw new ResourceValidationException(ENTITY,
                    "Therapy not created, because you are not a physiotherapist.");
        }

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
        if (request.getFinished() != null) {
            therapy.setFinished(request.getFinished());
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

    @Override
    public Therapy getTherapyByPhysiotherapistIdAndPatientId(Integer physiotherapistId, Integer patientId) {
        return therapyRepository.findTherapyByPhysiotherapistIdAndPatientId(physiotherapistId, patientId);
    }

    @Override
    public TherapyResource getResourceByPhysiotherapistIdAndPatientId(String jwt, Integer physiotherapistId, Integer patientId) {
        TherapyResource therapy = mapper.toResource(getTherapyByPhysiotherapistIdAndPatientId(physiotherapistId, patientId));
        therapy.setPatient(externalConfiguration.getPatientByID(jwt, therapy.getPatient().getId()));
        therapy.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, therapy.getPhysiotherapist().getId()));
        return therapy;
    }

}
