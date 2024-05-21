package com.digitalholics.consultationsservice.Consultation.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.User;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.ConsultationRepository;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;

import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import com.digitalholics.consultationsservice.Shared.EmailService.MailSenderService;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceValidationException;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private static final String ENTITY = "Consultation";

    private final ConsultationRepository consultationRepository;
    private final Validator validator;
    private final MailSenderService mailService;


    public ConsultationServiceImpl(ConsultationRepository consultationRepository, Validator validator, MailSenderService mailSenderService) {
        this.consultationRepository = consultationRepository;
        this.validator = validator;
        this.mailService = mailSenderService;
    }

    @Override
    public List<Consultation> getAll(String jwt) {
        return consultationRepository.findAll();
    }

    @Override
    public Page<Consultation> getAll(Pageable pageable) {
        return consultationRepository.findAll(pageable);
    }

    @Override
    public Consultation getById(Integer consultationId) {
        return consultationRepository.findConsultationById(consultationId);
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
    public Consultation create(CreateConsultationResource consultationResource, String jwt) {

        Set<ConstraintViolation<CreateConsultationResource>> violations = validator.validate(consultationResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Consultation consultation = new Consultation();
        consultation.setPatientId(consultationResource.getPatientId());
        consultation.setPhysiotherapistId(consultationResource.getPhysiotherapistId());
        consultation.setDone(consultationResource.getDone());
        consultation.setTopic(consultationResource.getTopic());
        consultation.setDiagnosis(consultationResource.getDiagnosis());
        consultation.setDate(consultationResource.getDate());
        consultation.setHour(consultationResource.getHour());
        consultation.setPlace(consultationResource.getPlace());

        //Aumento de la cantidad de consultas de un fisio

        //Aumento de la cantidad de consultas de un paciente

        //Aumento de cantidad de pacientes de un fisio

        //Send email to the user

        User user = mailService.getUser(jwt);

        String body = mailService.buildHtmlEmail(user.getFirstname(),consultationResource.getTopic(),consultationResource.getDate(),"1");
        try {
            mailService.sendNewMail(user.getUsername(),"Confirmacion de Consulta",body);
        }catch (MessagingException e){
            e.printStackTrace();
        }

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
        Consultation consultation = consultationRepository.findConsultationById(consultationId);
        consultationRepository.delete(consultation);
        return ResponseEntity.ok().build();
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
