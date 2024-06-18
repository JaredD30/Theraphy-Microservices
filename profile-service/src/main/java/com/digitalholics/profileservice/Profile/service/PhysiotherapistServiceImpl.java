package com.digitalholics.profileservice.Profile.service;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import com.digitalholics.profileservice.Profile.domain.persistence.PatientRepository;
import com.digitalholics.profileservice.Profile.domain.persistence.PhysiotherapistRepository;
import com.digitalholics.profileservice.Profile.domain.service.PhysiotherapistService;
import com.digitalholics.profileservice.Profile.mapping.PhysiotherapistMapper;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.CreatePhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.PhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.UpdatePhysiotherapistResource;
import com.digitalholics.profileservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.profileservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.profileservice.Shared.configuration.ExternalConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class PhysiotherapistServiceImpl implements PhysiotherapistService {
    private static final String ENTITY = "Physiotherapist";

    private final PhysiotherapistRepository physiotherapistRepository;
    private final PatientRepository patientRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;
    private final PhysiotherapistMapper mapper;


    public PhysiotherapistServiceImpl(PhysiotherapistRepository physiotherapistRepository, PatientRepository patientRepository, Validator validator, ExternalConfiguration externalConfiguration, PhysiotherapistMapper mapper) {
        this.physiotherapistRepository = physiotherapistRepository;
        this.patientRepository = patientRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }

    @Override
    public List<Physiotherapist> getAll() {

        return physiotherapistRepository.findAll();
    }

    @Override
    public Page<Physiotherapist> getAll(Pageable pageable) {
        return physiotherapistRepository.findAll(pageable);
    }

    @Override
    public Page<PhysiotherapistResource> getAllPhysiotherapist(String jwt, Pageable pageable) {
        Page<PhysiotherapistResource> physiotherapist =
                mapper.modelListPage(getAll(), pageable);
        physiotherapist.forEach(physiotherapistResource -> {
            physiotherapistResource.setUser(externalConfiguration.getUserById(physiotherapistResource.getUser().getId()));
        });

        return physiotherapist;
    }

    @Override
    public Physiotherapist getById(Integer patientId) {
        return physiotherapistRepository.findById(patientId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, patientId));    }

    @Override
    public PhysiotherapistResource getLoggedInPhysiotherapist(String jwt) {
        User user = externalConfiguration.getUser(jwt);
        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")) {
            Optional<Physiotherapist> physiotherapistOptional = physiotherapistRepository.findByUserId(user.getId());
            Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new ResourceNotFoundException("Not found a physiotherapist autenticated."));
            PhysiotherapistResource physiotherapistResource  = mapper.toResource(physiotherapist);
            physiotherapistResource.setUser(user);
            return physiotherapistResource;
        }
        throw new ResourceNotFoundException("Not found a physiotherapist autenticated.");
    }

    @Override
    public PhysiotherapistResource getResourceById(Integer patientId) {
        Physiotherapist physiotherapist = getById(patientId);
        PhysiotherapistResource physiotherapistResource = mapper.toResource(physiotherapist);
        User user = externalConfiguration.getUserById(physiotherapist.getUserId());
        physiotherapistResource.setUser(user);
        return physiotherapistResource;
    }

    @Override
    public Physiotherapist getByUserId(Integer userId) {
        return physiotherapistRepository.findByUserId(userId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, userId));    }


    @Override
    public Physiotherapist create(CreatePhysiotherapistResource physiotherapistResource, String jwt) {

        Set<ConstraintViolation<CreatePhysiotherapistResource>> violations = validator.validate(physiotherapistResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Physiotherapist physiotherapistWithDni = physiotherapistRepository.findPhysiotherapistByDni(physiotherapistResource.getDni());
        Patient patientWithDni = patientRepository.findPatientByDni(physiotherapistResource.getDni());

        if (physiotherapistWithDni != null || patientWithDni != null)
            throw new ResourceValidationException(ENTITY,
                    "A physiotherapist with the same Dni first name already exists.");

        User user = externalConfiguration.getUser(jwt);

        System.out.printf(String.valueOf(user));

        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")) {
            Physiotherapist physiotherapist = new Physiotherapist();
            physiotherapist.setUserId(user.getId());
            physiotherapist.setAge(physiotherapistResource.getAge());
            physiotherapist.setDni(physiotherapistResource.getDni());
            physiotherapist.setLocation(physiotherapistResource.getLocation());
            physiotherapist.setBirthdayDate(physiotherapistResource.getBirthdayDate());
            physiotherapist.setPhotoUrl(physiotherapistResource.getPhotoUrl());
            physiotherapist.setConsultationQuantity(0);
            physiotherapist.setSpecialization(physiotherapistResource.getSpecialization());
            physiotherapist.setYearsExperience(physiotherapistResource.getYearsExperience());
            physiotherapist.setRating(0.0);
            physiotherapist.setPatientQuantity(0);
            physiotherapist.setFees(physiotherapistResource.getFees());

            return physiotherapistRepository.save(physiotherapist);
        }else {
            throw new ResourceValidationException(ENTITY,
                    "Physiotherapist not crate, because you are not a Physiotherapist.");
        }
    }

    @Override
    public Physiotherapist update(Integer physiotherapistId, UpdatePhysiotherapistResource request) {
        Physiotherapist physiotherapist = getById(physiotherapistId);
        physiotherapist.setDni(request.getDni() != null ? request.getDni() : physiotherapist.getDni());
        physiotherapist.setAge(request.getAge() != null ? request.getAge() : physiotherapist.getAge());
        physiotherapist.setPhotoUrl(request.getPhotoUrl() != null ? request.getPhotoUrl() : physiotherapist.getPhotoUrl());
        physiotherapist.setPatientQuantity(request.getPatientQuantity() != null ? request.getPatientQuantity() : physiotherapist.getPatientQuantity());
        physiotherapist.setLocation(request.getLocation() != null ? request.getLocation() : physiotherapist.getLocation());
        physiotherapist.setBirthdayDate(request.getBirthdayDate() != null ? request.getBirthdayDate() : physiotherapist.getBirthdayDate());
        physiotherapist.setRating(request.getRating() != null ? request.getRating() : physiotherapist.getRating());
        physiotherapist.setSpecialization(request.getSpecialization() != null ? request.getSpecialization() : physiotherapist.getSpecialization());
        physiotherapist.setYearsExperience(request.getYearsExperience() != null ? request.getYearsExperience() : physiotherapist.getYearsExperience());
        physiotherapist.setConsultationQuantity(request.getConsultationQuantity() != null ? request.getConsultationQuantity() : physiotherapist.getConsultationQuantity());
        physiotherapist.setFees(request.getFees() != null ? request.getFees() : physiotherapist.getFees());
        return physiotherapistRepository.save(physiotherapist);
    }

    @Override
    public ResponseEntity<?> delete(Integer physiotherapistId) {
        Physiotherapist physiotherapist = physiotherapistRepository.findPhysiotherapistById(physiotherapistId);
        physiotherapistRepository.delete(physiotherapist);
        return ResponseEntity.ok().build();
    }

    @Override
    public Physiotherapist updatePhysiotherapistRating(String jwt, Integer physiotherapistID, Double rating) {
        Physiotherapist physiotherapist = physiotherapistRepository.findPhysiotherapistById(physiotherapistID);
        physiotherapist.setRating(rating);
        return physiotherapistRepository.save(physiotherapist);
    }

    @Override
    public Physiotherapist updatePhysiotherapistConsultationQuantity(String jwt, Integer physiotherapistID, Integer consultation) {
        Physiotherapist physiotherapist = physiotherapistRepository.findPhysiotherapistById(physiotherapistID);
        physiotherapist.setConsultationQuantity(physiotherapist.getConsultationQuantity() + consultation);
        return physiotherapistRepository.save(physiotherapist);
    }

    @Override
    public Physiotherapist updatePhysiotherapistPatientQuantity(String jwt, Integer physiotherapistID, Integer patientQuantity) {
        Physiotherapist physiotherapist = physiotherapistRepository.findPhysiotherapistById(physiotherapistID);
        physiotherapist.setPatientQuantity(physiotherapist.getPatientQuantity() + patientQuantity);
        return physiotherapistRepository.save(physiotherapist);
    }

}
