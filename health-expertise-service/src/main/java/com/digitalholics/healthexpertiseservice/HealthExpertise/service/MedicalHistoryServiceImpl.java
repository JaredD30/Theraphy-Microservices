package com.digitalholics.healthexpertiseservice.HealthExpertise.service;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Consultation;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Patient;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.MedicalHistory;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.ConsultationRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.PatientRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.PhysiotherapistRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.UserRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.MedicalHistoryRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.MedicalHistoryService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.CreateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.MedicalHistory.UpdateMedicalHistoryResource;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.healthexpertiseservice.Shared.JwtValidation.JwtValidator;
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


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class MedicalHistoryServiceImpl implements MedicalHistoryService {

    private static final String ENTITY = "MedicalHistory";

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final PatientRepository patientRepository;
    private final PhysiotherapistRepository physiotherapistRepository;
    private final ConsultationRepository consultationRepository;
    private final JwtValidator jwtValidator;
    private final Validator validator;



    public MedicalHistoryServiceImpl(MedicalHistoryRepository medicalHistoryRepository, PatientRepository patientRepository, PhysiotherapistRepository physiotherapistRepository, ConsultationRepository consultationRepository, JwtValidator jwtValidator, Validator validator) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.patientRepository = patientRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.consultationRepository = consultationRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }

    @Override
    public List<MedicalHistory> getAll(String jwt) {
        User user = jwtValidator.validateJwtAndGetUser(jwt, "ADMIN");

        return medicalHistoryRepository.findAll();
    }

    @Override
    public Page<MedicalHistory> getAll(Pageable pageable) {
        return medicalHistoryRepository.findAll(pageable);
    }

    @Override
    public MedicalHistory getById(String jwt, Integer medicalHistoryId) {

        User user = jwtValidator.validateJwtAndGetUserNoRol(jwt);

        if(Objects.equals(String.valueOf(user.getRole()), "PATIENT")){
            Optional<MedicalHistory> medicalHistoryOptional = medicalHistoryRepository.findById(medicalHistoryId);
            MedicalHistory medicalHistory = medicalHistoryOptional.orElseThrow(() -> new NotFoundException("Not found medicalHistory with ID: " + medicalHistoryId));

            if(Objects.equals(String.valueOf(user.getUsername()), medicalHistory.getPatient().getUser().getUsername())){

                return medicalHistory;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }


        if(Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")){
            Optional<MedicalHistory> medicalHistoryOptional = medicalHistoryRepository.findById(medicalHistoryId);
            MedicalHistory medicalHistory = medicalHistoryOptional.orElseThrow(() -> new NotFoundException("Not found medicalHistory with ID: " + medicalHistoryId));

            Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));
            Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

            List<Consultation> myConsultations = consultationRepository.findByPhysiotherapistId(physiotherapist.getId());


            boolean isMyPatient = false;

            for (Consultation consultation : myConsultations) {
                if (Objects.equals(consultation.getPatient().getUser().getUsername(), medicalHistory.getPatient().getUser().getUsername())) {

                    isMyPatient = true;
                    break;
                }
            }

            if (isMyPatient) {

                return medicalHistory;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");

        }

        return medicalHistoryRepository.findById(medicalHistoryId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, medicalHistoryId));
    }

    @Override
    public MedicalHistory getByPatientId(String jwt, Integer patientId) {

        User user = jwtValidator.validateJwtAndGetUserNoRol(jwt);


        if(Objects.equals(String.valueOf(user.getRole()), "PATIENT")){
            MedicalHistory medicalHistory = medicalHistoryRepository.findByPatientId(patientId);
            if(medicalHistory == null)
                throw new ResourceValidationException(ENTITY,
                        "Not found Medical History for this patient");

            if(Objects.equals(String.valueOf(user.getUsername()), medicalHistory.getPatient().getUser().getUsername())){

                return medicalHistory;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }

        if(Objects.equals(String.valueOf(user.getRole()), "PHYSIOTHERAPIST")){
            MedicalHistory medicalHistory = medicalHistoryRepository.findByPatientId(patientId);
            if(medicalHistory == null)
                throw new ResourceValidationException(ENTITY,
                        "Not found Medical History for this patient");


            Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));
            Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

            List<Consultation> myConsultations = consultationRepository.findByPhysiotherapistId(physiotherapist.getId());


            boolean isMyPatient = false;

            for (Consultation consultation : myConsultations) {
                if (Objects.equals(consultation.getPatient().getUser().getUsername(), medicalHistory.getPatient().getUser().getUsername())) {

                    isMyPatient = true;
                    break;
                }
            }

            if (isMyPatient) {

                return medicalHistory;
            }

            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }


       MedicalHistory medicalHistory = medicalHistoryRepository.findByPatientId(patientId);

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


        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Optional<Patient> patientOptional = Optional.ofNullable(patientRepository.findPatientsByUserUsername(user.getUsername()));
        Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

        MedicalHistory medicalHistoryWithPatient = medicalHistoryRepository.findByPatientId(patient.getId());

        if(medicalHistoryWithPatient != null)
            throw new ResourceValidationException(ENTITY,
                    "A Medical History for this patient already exits");

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatient(patient);
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

        MedicalHistory medicalHistory = getById(jwt, medicalHistoryId);

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
        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        return medicalHistoryRepository.findById(medicalHistoryId).map(medicalHistory -> {

            if(Objects.equals(user.getUsername(), medicalHistory.getPatient().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){
                medicalHistoryRepository.delete(medicalHistory);
                return ResponseEntity.ok().build();
            }
            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,medicalHistoryId));
    }

}
