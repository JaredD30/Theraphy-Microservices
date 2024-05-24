package com.digitalholics.consultationsservice.Consultation.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Physiotherapist;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.User;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.ConsultationRepository;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;

import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import com.digitalholics.consultationsservice.Shared.EmailService.MailSenderService;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.consultationsservice.Shared.configuration.ExternalConfiguration;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private static final String ENTITY = "Consultation";

    private final ConsultationRepository consultationRepository;
    private final Validator validator;
    private final MailSenderService mailService;
    private final ExternalConfiguration externalConfiguration;


    public ConsultationServiceImpl(ConsultationRepository consultationRepository, Validator validator, MailSenderService mailSenderService, ExternalConfiguration externalConfiguration) {
        this.consultationRepository = consultationRepository;
        this.validator = validator;
        this.mailService = mailSenderService;
        this.externalConfiguration = externalConfiguration;
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

        User user = externalConfiguration.getUser(jwt);

        System.out.printf(String.valueOf(user));

        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PATIENT")) {

            Patient patient = externalConfiguration.getPatientByUserId(jwt,user.getId());
            System.out.println("GetPatientByUser Id" + patient);
            Physiotherapist physiotherapist =  externalConfiguration.getPhysiotherapistById(jwt, consultationResource.getPhysiotherapistId());
            System.out.println("GetPhysioByConsultation" + physiotherapist);
            User userPhysiotherapist = externalConfiguration.getUserById(physiotherapist.getUser().getId());
            System.out.println("Este fisio: "+ physiotherapist + " Pertenese a este usuario "+ userPhysiotherapist);

            System.out.println(patient.getId());
            Consultation consultation = new Consultation();
            consultation.setPatientId(patient.getId());
            consultation.setPhysiotherapistId(consultationResource.getPhysiotherapistId());
            consultation.setDone(consultationResource.getDone());
            consultation.setTopic(consultationResource.getTopic());
            consultation.setDiagnosis(consultationResource.getDiagnosis());
            consultation.setDate(consultationResource.getDate());
            consultation.setHour(consultationResource.getHour());
            consultation.setPlace(consultationResource.getPlace());

            //Send email to the user
            String body = mailService.buildHtmlEmail(user.getFirstname(),consultationResource.getTopic(),consultationResource.getDate(),userPhysiotherapist.getFirstname());
            try {
                mailService.sendNewMail(user.getUsername(),"Confirmacion de Consulta",body);
            }catch (MessagingException e){
                e.printStackTrace();
            }

            return consultationRepository.save(consultation);
        } else {
            throw new ResourceValidationException(ENTITY,
                    "Consultation not crate, because you are not a patient.");
        }


        //Aumento de la cantidad de consultas de un fisio

        //Aumento de la cantidad de consultas de un paciente

        //Aumento de cantidad de pacientes de un fisio

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
