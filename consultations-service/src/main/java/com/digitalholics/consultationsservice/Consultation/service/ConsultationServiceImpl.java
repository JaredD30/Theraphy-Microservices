package com.digitalholics.consultationsservice.Consultation.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Diagnosis;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Physiotherapist;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.User;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.ConsultationRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.External.DiagnosisRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.External.PatientRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.External.PhysiotherapistRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.External.UserRepository;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;

import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceValidationException;


import com.digitalholics.consultationsservice.Shared.JwtValidation.JwtValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private static final String ENTITY = "Consultation";

    private final ConsultationRepository consultationRepository;
    private final PhysiotherapistRepository physiotherapistRepository;
    private final PatientRepository patientRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final JwtValidator jwtValidator;
    private final Validator validator;


    public ConsultationServiceImpl(ConsultationRepository consultationRepository, PhysiotherapistRepository physiotherapistRepository, PatientRepository patientRepository, DiagnosisRepository diagnosisRepository,  JwtValidator jwtValidator, Validator validator) {
        this.consultationRepository = consultationRepository;
        this.physiotherapistRepository = physiotherapistRepository;

        this.diagnosisRepository = diagnosisRepository;

        this.patientRepository = patientRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }


    @Override
    public List<Consultation> getAll(String jwt) {

        User user = jwtValidator.validateJwtAndGetUser(jwt, "ADMIN");

        return consultationRepository.findAll();
    }

    @Override
    public Page<Consultation> getAll(Pageable pageable) {
        return consultationRepository.findAll(pageable);
    }

    @Override
    public Consultation getById(String jwt, Integer consultationId) {

        User user = jwtValidator.validateJwtAndGetUserNoRol(jwt);

        if(Objects.equals(String.valueOf(user.getRole()), "PATIENT")){
            Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);
            Consultation consultation = consultationOptional.orElseThrow(() -> new NotFoundException("Not found consultation with ID: " + consultationId));

            if(Objects.equals(String.valueOf(user.getUsername()), consultation.getPatient().getUser().getUsername())){

                return consultation;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }

        if(Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")){
            Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);
            Consultation consultationNeeded = consultationOptional.orElseThrow(() -> new NotFoundException("Not found consultation with ID: " + consultationId));

            Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));
            Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

            List<Consultation> myConsultations = consultationRepository.findByPhysiotherapistId(physiotherapist.getId());


            boolean isMyPatient = false;

            for (Consultation consultation : myConsultations) {
                if (Objects.equals(consultation.getPatient().getUser().getUsername(), consultationNeeded.getPatient().getUser().getUsername())) {

                    isMyPatient = true;
                    break;
                }
            }

            if (isMyPatient) {

                return consultationNeeded;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");

        }

        return consultationRepository.findById(consultationId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, consultationId));
    }


    @Override
    public List<Consultation> getByPhysiotherapistId(Integer physiotherapistId) {
        List<Consultation> consultations = consultationRepository.findByPhysiotherapistId(physiotherapistId);

        if(consultations.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Consultations for this physiotherapist");

        return consultations;
    }

    @Override
    public Consultation getConsultationByPhysiotherapistId(Integer physiotherapistId) {
        return consultationRepository.findConsultationByPhysiotherapistId(physiotherapistId);
    }

    @Override
    public List<Consultation> getByPatientId(Integer patientId) {
        List<Consultation> consultations = consultationRepository.findByPatientId(patientId);

        if(consultations.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Consultations for this patient");

        return consultations;
    }




    @Override
    public Consultation create(String jwt, CreateConsultationResource consultationResource) {

        Set<ConstraintViolation<CreateConsultationResource>> violations = validator.validate(consultationResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Optional<Patient> patientOptional = Optional.ofNullable(patientRepository.findPatientsByUserUsername(user.getUsername()));
        Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));


        Optional<Physiotherapist> physiotherapistOptional = physiotherapistRepository.findById(consultationResource.getPhysiotherapistId());
        Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found physiotherapist with ID: " + consultationResource.getPhysiotherapistId()));

        Consultation consultation = new Consultation();
        consultation.setPatient(patient);
        consultation.setPhysiotherapist(physiotherapist);
        consultation.setDone(consultationResource.getDone());
        consultation.setTopic(consultationResource.getTopic());
        consultation.setDiagnosis(consultationResource.getDiagnosis());
        consultation.setDate(consultationResource.getDate());
        consultation.setHour(consultationResource.getHour());
        consultation.setPlace(consultationResource.getPlace());

        Integer consultationsQuantityPhysiotherapist = physiotherapist.getConsultationQuantity();
        physiotherapist.setConsultationQuantity(consultationsQuantityPhysiotherapist+1);
        physiotherapistRepository.save(physiotherapist);

        Integer consultationsQuantityPatient = patient.getAppointmentQuantity();
        patient.setAppointmentQuantity(consultationsQuantityPatient+1);
        patientRepository.save(patient);

        List<Consultation> consultations = consultationRepository.findByPhysiotherapistId(physiotherapist.getId());

        for (Consultation existingConsultation : consultations) {
            if (existingConsultation.getPatient().getId().equals(patient.getId())) {

                return consultationRepository.save(consultation);
            }
        }

        Integer patientQuantity = physiotherapist.getPatientQuantity();
        physiotherapist.setPatientQuantity(patientQuantity+1);
        physiotherapistRepository.save(physiotherapist);

        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation update(String jwt, Integer consultationId, UpdateConsultationResource request) {

        Consultation consultation = getById(jwt, consultationId);

            if (request.getDone() != null) {
                consultation.setDone(request.getDone());
            }
            if (request.getTopic() != null) {
                consultation.setTopic(request.getTopic());
            }
            if (request.getDiagnosis() != null) {
                consultation.setDiagnosis(request.getDiagnosis());
            }
            if (request.getDate() != null) {
                consultation.setDate(request.getDate());
            }
            if (request.getHour() != null) {
                consultation.setHour(request.getHour());
            }
            if (request.getPlace() != null) {
                consultation.setPlace(request.getPlace());
            }

            return consultationRepository.save(consultation);

    }


    @Override
    public ResponseEntity<?> delete(String jwt, Integer consultationId) {
        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        return consultationRepository.findById(consultationId).map(consultation -> {
            if(Objects.equals(user.getUsername(), consultation.getPatient().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){

                consultationRepository.delete(consultation);
                return ResponseEntity.ok().build();
            }
            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,consultationId));
    }

    @Override
    public Consultation updateDiagnosis(String jwt, Integer consultationId, String diagnosis) {


        Consultation consultation = getById(jwt, consultationId);

            if (diagnosis != null) {
                consultation.setDiagnosis(diagnosis);
            }
            if (!consultation.getDone()) {
                consultation.setDone(true);
            }

        Diagnosis diagnosisResource = new Diagnosis();
        diagnosisResource.setDiagnosis(diagnosis);
        diagnosisResource.setPatient(consultation.getPatient());
        diagnosisResource.setPhysiotherapist(consultation.getPhysiotherapist());
        diagnosisResource.setDate(String.valueOf(LocalDate.now()));

        diagnosisRepository.save(diagnosisResource);


            return consultationRepository.save(consultation);
    }
}
