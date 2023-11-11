package com.digitalholics.therapyservice.Therapy.service;

import com.digitalholics.therapyservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.therapyservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.therapyservice.Shared.Exception.UnauthorizedException;
import com.digitalholics.therapyservice.Shared.JwtValidation.JwtValidator;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Appointment;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Diagnosis;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.User;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.Therapy;
import com.digitalholics.therapyservice.Therapy.domain.persistence.AppointmentRepository;
import com.digitalholics.therapyservice.Therapy.domain.persistence.External.DiagnosisRepository;
import com.digitalholics.therapyservice.Therapy.domain.persistence.TherapyRepository;
import com.digitalholics.therapyservice.Therapy.domain.service.AppointmentService;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.CreateAppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.UpdateAppointmentResource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
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
public class AppointmentServiceImpl implements AppointmentService {

    private static final String ENTITY = "Appointment";

    private final AppointmentRepository appointmentRepository;
    private final TherapyRepository therapyRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final JwtValidator jwtValidator;
    private final Validator validator;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, TherapyRepository therapyRepository, DiagnosisRepository diagnosisRepository, JwtValidator jwtValidator, Validator validator) {
        this.appointmentRepository = appointmentRepository;
        this.therapyRepository = therapyRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Page<Appointment> getAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public Appointment getById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException(ENTITY, appointmentId));
    }

    @Override
    public List<Appointment> getAppointmentByTherapyId(Integer therapyId) {
        return appointmentRepository.findAppointmentByTherapyId(therapyId);
    }

    @Override
    public Appointment create(String jwt, CreateAppointmentResource appointmentResource) {
        Set<ConstraintViolation<CreateAppointmentResource>> violations = validator.validate(appointmentResource);

        if(!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        Appointment appointmentWithTopic = appointmentRepository.findByTopic(appointmentResource.getTopic());

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        if(appointmentWithTopic != null)
            throw new ResourceValidationException(ENTITY,
                    "A Appointment with the same topic already exists.");

        Optional<Therapy> therapyOptional = therapyRepository.findById(appointmentResource.getTherapyId());

        Therapy therapy = therapyOptional.orElseThrow(()-> new NotFoundException("This therapy not found with ID: "+ appointmentResource.getTherapyId()));
        Appointment appointment = new Appointment();

        if (therapy.getPatient().getUser().getUsername().equals(user.getUsername()) || therapy.getPhysiotherapist().getUser().getUsername().equals(user.getUsername())){
            appointment.setDone(appointmentResource.getDone());
            appointment.setTopic(appointmentResource.getTopic());
            appointment.setDiagnosis(appointmentResource.getDiagnosis());
            appointment.setDate(appointmentResource.getDate());
            appointment.setHour(appointmentResource.getHour());
            appointment.setPlace(appointmentResource.getPlace());
            appointment.setTherapy(therapy);
            return appointmentRepository.save(appointment);
        }else {
            throw new UnauthorizedException("You do not have permission to create an appointment for this therapy.");
        }
    }

    @Override
    public Appointment update(Integer appointmentId, UpdateAppointmentResource request) {
        Appointment appointment = getById(appointmentId);

        if (request.getDone() != null) {
            appointment.setDone(request.getDone());
        }
        if (request.getTopic() != null) {
            appointment.setTopic(request.getTopic());
        }
        if (request.getDiagnosis() != null) {
            appointment.setDiagnosis(request.getDiagnosis());
        }
        if (request.getDate() != null) {
            appointment.setDate(request.getDate());
        }
        if (request.getHour() != null) {
            appointment.setHour(request.getHour());
        }
        if (request.getPlace() != null) {
            appointment.setPlace(request.getPlace());
        }



        return appointmentRepository.save(appointment);
    }

    @Override
    public ResponseEntity<?> delete(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId).map( appointment -> {
            appointmentRepository.delete(appointment);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,appointmentId));
    }

    @Override
    public List<Appointment> getAppointmentsByTherapyByPatientId(Integer patientId) {
        return appointmentRepository.findAppointmentsByTherapyByPatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsByTherapyByPhysiotherapistId(Integer physiotherapistId) {
        return appointmentRepository.findAppointmentsByTherapyByPhysiotherapistId(physiotherapistId);
    }

    @Override
    public Appointment getAppointmentByDateAndTherapyId(Integer therapyId, String date) {
        return appointmentRepository.findAppointmentByDateAndTherapyId(therapyId, date);
    }

    @Override
    public Appointment updateDiagnosis(Integer appointmentId, String diagnosis) {
        Appointment appointment = getById(appointmentId);

        if (diagnosis != null) {
            appointment.setDiagnosis(diagnosis);
        }
        if (!appointment.getDone()) {
            appointment.setDone(true);
        }

        Diagnosis diagnosisResource = new Diagnosis();
        diagnosisResource.setDiagnosis(diagnosis);
        diagnosisResource.setPatient(appointment.getTherapy().getPatient());
        diagnosisResource.setPhysiotherapist(appointment.getTherapy().getPhysiotherapist());
        diagnosisResource.setDate(String.valueOf(LocalDate.now()));

        diagnosisRepository.save(diagnosisResource);

        return appointmentRepository.save(appointment);
    }
}
