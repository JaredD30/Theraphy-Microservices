package com.digitalholics.consultationsservice.Consultation.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.Physiotherapist;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.External.User;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.ConsultationRepository;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;

import com.digitalholics.consultationsservice.Consultation.mapping.ConsultationMapper;
import com.digitalholics.consultationsservice.Consultation.resource.ConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import com.digitalholics.consultationsservice.Shared.EmailService.MailSenderService;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.consultationsservice.Shared.configuration.ExternalConfiguration;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;


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
    private final ConsultationMapper mapper;


    public ConsultationServiceImpl(ConsultationRepository consultationRepository, Validator validator, MailSenderService mailSenderService, ExternalConfiguration externalConfiguration, ConsultationMapper mapper) {
        this.consultationRepository = consultationRepository;
        this.validator = validator;
        this.mailService = mailSenderService;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
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
    public ConsultationResource getResourceById(String jwt, Integer therapyId) {
        ConsultationResource consultationResource = mapper.toResource(getById(therapyId));
        consultationResource.setPatientId(externalConfiguration.getPatientByID(jwt, consultationResource.getPatientId().getId()));
        consultationResource.setPhysiotherapistId(externalConfiguration.getPhysiotherapistById(jwt, consultationResource.getPhysiotherapistId().getId()));
        return consultationResource;
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
    public Page<ConsultationResource> getResourceByPatientId(String jwt, Integer patientId, Pageable pageable) {
        Page<ConsultationResource> consultation =
                mapper.modelListPage(getByPatientId(patientId), pageable);
        consultation.forEach(consultationResource -> {
            consultationResource.setPatientId(externalConfiguration.getPatientByID(jwt, consultationResource.getPatientId().getId()));
            consultationResource.setPhysiotherapistId(externalConfiguration.getPhysiotherapistById(jwt, consultationResource.getPhysiotherapistId().getId()));
        });

        return consultation;
    }


    @Override
    public Consultation getConsultationByPhysiotherapistId(Integer physiotherapistId) {
        return consultationRepository.findConsultationByPhysiotherapistId(physiotherapistId);
    }

    @Override
    public Page<ConsultationResource> getResourceByPhysiotherapistId(String jwt, Integer physiotherapistId, Pageable pageable) {
        Page<ConsultationResource> consultation =
                mapper.modelListPage(getByPhysiotherapistId(physiotherapistId), pageable);
        consultation.forEach(consultationResource -> {
            consultationResource.setPatientId(externalConfiguration.getPatientByID(jwt, consultationResource.getPatientId().getId()));
            consultationResource.setPhysiotherapistId(externalConfiguration.getPhysiotherapistById(jwt, consultationResource.getPhysiotherapistId().getId()));
        });

        return consultation;
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

        Integer consultationQ = 0;
        Integer patientQ = 0;

        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PATIENT")) {

            Patient patient = externalConfiguration.getPatientByUserId(jwt,user.getId());
            System.out.println("GetPatientByUser Id" + patient);
            Physiotherapist physiotherapist =  externalConfiguration.getPhysiotherapistById(jwt, consultationResource.getPhysiotherapistId());
            System.out.println("GetPhysioByConsultation" + physiotherapist);
            User userPhysiotherapist = externalConfiguration.getUserById(physiotherapist.getUser().getId());
            System.out.println("Este fisio: "+ physiotherapist + " Pertenese a este usuario "+ userPhysiotherapist);

            System.out.println(patient.getId());

            List<Consultation> consultations = consultationRepository.findByPhysiotherapistId(physiotherapist.getId());

            Consultation consultation = new Consultation();
            Boolean bolConsult = true;
            for (Consultation existingConsultation : consultations) {
                if (existingConsultation.getPatientId().equals(patient.getId()) && !existingConsultation.getDone()) {
                    bolConsult = false;
                }
            }

            if (bolConsult) {
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
                    //Aumento de la cantidad de consultas de un fisio
                    consultationQ += 1;
                    externalConfiguration.updatePhysiotherapistConsultationQuantity(jwt,consultationResource.getPhysiotherapistId(), consultationQ);
                    //Aumento de cantidad de pacientes de un fisio
                    patientQ += 1;
                    externalConfiguration.updatePhysiotherapistPatientsQuantity(jwt,consultationResource.getPhysiotherapistId(), patientQ);
                    externalConfiguration.updatePatientsAppointmentQuantity(jwt,patient.getId(),1);

                }catch (MessagingException e){
                    e.printStackTrace();
                }
                return consultationRepository.save(consultation);

            }else {
                throw new ResourceValidationException(ENTITY,
                        "Consultation not crate, You have already a pending consultation.");
            }
        }else {
            throw new ResourceValidationException(ENTITY,
                    "Consultation not crate, because you are not a patient.");
        }
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
