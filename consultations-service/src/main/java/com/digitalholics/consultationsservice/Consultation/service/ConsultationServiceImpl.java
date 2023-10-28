package com.digitalholics.consultationsservice.Consultation.service;


import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Consultation;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Patient;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.Physiotherapist;
import com.digitalholics.consultationsservice.Consultation.domain.model.entity.User.User;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.ConsultationRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.PatientRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.PhysiotherapistRepository;
import com.digitalholics.consultationsservice.Consultation.domain.persistence.UserRepository;
import com.digitalholics.consultationsservice.Consultation.domain.service.ConsultationService;

import com.digitalholics.consultationsservice.Consultation.resource.CreateConsultationResource;
import com.digitalholics.consultationsservice.Consultation.resource.UpdateConsultationResource;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.consultationsservice.Shared.Exception.ResourceValidationException;


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

@Service
public class ConsultationServiceImpl implements ConsultationService {

    private static final String ENTITY = "Consultation";

    private final ConsultationRepository consultationRepository;
    private final PhysiotherapistRepository physiotherapistRepository;
    private final PatientRepository patientRepository;

    private final UserRepository userRepository;

    //private final DiagnosisRepository diagnosisRepository;

    private final RestTemplate restTemplate;


    public ConsultationServiceImpl(ConsultationRepository consultationRepository, PhysiotherapistRepository physiotherapistRepository, PatientRepository patientRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.consultationRepository = consultationRepository;
        this.physiotherapistRepository = physiotherapistRepository;

        //this.diagnosisRepository = diagnosisRepository;

        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
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
    public Consultation create(String jwt, CreateConsultationResource consultationResource) {


        User user = validateJwtAndGetUser(jwt, "PATIENT");

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

        User user = validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        Consultation consultation = getById(consultationId);

        if(Objects.equals(user.getUsername(), consultation.getPhysiotherapist().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){
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

        throw new ResourceValidationException("JWT",
                "Invalid rol.");

    }


    @Override
    public ResponseEntity<?> delete(String jwt, Integer consultationId) {
        User user = validateJwtAndGetUser(jwt, "PATIENT");

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

        User user = validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        Consultation consultation = getById(consultationId);

        if(Objects.equals(user.getUsername(), consultation.getPhysiotherapist().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){

            if (diagnosis != null) {
                consultation.setDiagnosis(diagnosis);
            }
            if (!consultation.getDone()) {
                consultation.setDone(true);
            }

        /*Diagnosis diagnosisResource = new Diagnosis();
        diagnosisResource.setDiagnosis(diagnosis);
        diagnosisResource.setPatient(consultation.getPatient());
        diagnosisResource.setPhysiotherapist(consultation.getPhysiotherapist());
        diagnosisResource.setDate(String.valueOf(LocalDate.now()));

        diagnosisRepository.save(diagnosisResource);
        */

            return consultationRepository.save(consultation);

        }

        throw new ResourceValidationException("JWT",
                "Invalid access.");
    }

    public User validateJwtAndGetUser(String jwt, String admittedRole) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita los primeros 7 caracteres ("Bearer ")
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwt);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        String validationEndpointUrl = "http://security-service/api/v1/security/auth/validate-jwt";
        ResponseEntity<String> responseEntity = restTemplate.exchange(validationEndpointUrl, HttpMethod.GET, requestEntity, String.class);

        Optional<User> userOptional = userRepository.findByUsername(responseEntity.getBody());

        User user = userOptional.orElseThrow(() -> new NotFoundException("User not found for username: " + responseEntity.getBody()));

        if(Objects.equals(String.valueOf(user.getRole()), admittedRole)
                || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){
            return user;
        }

        throw new ResourceValidationException("JWT",
                "Invalid rol.");
    }
}
