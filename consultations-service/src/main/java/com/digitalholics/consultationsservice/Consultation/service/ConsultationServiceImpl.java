package com.digitalholics.consultationsservice.Consultation.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.ConsultationRepository;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;

import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private static final String ENTITY = "Consultation";

    private final ConsultationRepository consultationRepository;
    //private final PhysiotherapistRepository physiotherapistRepository;
    //private final PatientRepository patientRepository;
    ///private final DiagnosisRepository diagnosisRepository;
    //private final Validator validator;

    public ConsultationServiceImpl(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
        //this.physiotherapistRepository = physiotherapistRepository;
        //this.patientRepository = patientRepository;
        //this.diagnosisRepository = diagnosisRepository;
        //this.validator = validator;
    }


    @Override
    public List<Consultation> getAll() {
        return consultationRepository.findAll();
    }

    @Override
    public Page<Consultation> getAll(Pageable pageable) {
        return consultationRepository.findAll(pageable);
    }

    @Override
    public Consultation getById(Integer consultationId) {
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
    public Consultation create(CreateConsultationResource consultationResource) {
        //Set<ConstraintViolation<CreateConsultationResource>> violations = validator.validate(consultationResource);

        //if (!violations.isEmpty())
            //throw new ResourceValidationException(ENTITY, violations);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        //Optional<Patient> patientOptional = Optional.ofNullable(patientRepository.findPatientsByUserUsername(username));
        //Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + username));


        //Optional<Physiotherapist> physiotherapistOptional = physiotherapistRepository.findById(consultationResource.getPhysiotherapistId());
        //Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found physiotherapist with ID: " + consultationResource.getPhysiotherapistId()));

        Consultation consultation = new Consultation();
        consultation.setPatientId(consultationResource.getPatientId());
        consultation.setPhysiotherapistId(consultationResource.getPhysiotherapistId());
        consultation.setDone(consultationResource.getDone());
        consultation.setTopic(consultationResource.getTopic());
        consultation.setDiagnosis(consultationResource.getDiagnosis());
        consultation.setDate(consultationResource.getDate());
        consultation.setHour(consultationResource.getHour());
        consultation.setPlace(consultationResource.getPlace());


        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation update(Integer consultationId, UpdateConsultationResource request) {
        Consultation consultation = getById(consultationId);

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
    public ResponseEntity<?> delete(Integer consultationId) {
        return consultationRepository.findById(consultationId).map(consultation -> {
            consultationRepository.delete(consultation);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,consultationId));
    }

    @Override
    public Consultation updateDiagnosis(Integer consultationId, String diagnosis) {
        Consultation consultation = getById(consultationId);

        if (diagnosis != null) {
            consultation.setDiagnosis(diagnosis);
        }
        if (!consultation.getDone()) {
            consultation.setDone(true);
        }

        return consultationRepository.save(consultation);
    }
}
