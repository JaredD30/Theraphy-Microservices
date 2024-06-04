package com.digitalholics.therapyservice.Therapy.domain.service;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.Appointment;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.AppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.CreateAppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.UpdateAppointmentResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AppointmentService {

    List<Appointment> getAll();
    Page<Appointment> getAll(Pageable pageable);

    Page<AppointmentResource> getAllResources(String jwt, Pageable pageable);

    Appointment getById(Integer appointmentId);

    AppointmentResource getResourceById(String jwt, Integer appointmentId);

    List<Appointment> getAppointmentByTherapyId(Integer therapyId);

    Page<AppointmentResource> getResourcesByTherapyId(String jwt, Pageable pageable, Integer theraphyId);

    Appointment create(String jwt, CreateAppointmentResource appointment);

    Appointment update(String jwt, Integer appointmentId, UpdateAppointmentResource request);

    ResponseEntity<?> delete(Integer appointmentId);


    List<Appointment> getAppointmentsByTherapyByPatientId(Integer patientId);

    Page<AppointmentResource> getResourcesByPatientId(String jwt, Pageable pageable, Integer patientId);

    List<Appointment> getAppointmentsByTherapyByPhysiotherapistId(Integer physiotherapistId);

    Page<AppointmentResource> getResourcesByPhysiotherapistId(String jwt, Pageable pageable, Integer physiotherapistId);

    Appointment getAppointmentByDateAndTherapyId(Integer therapyId, String date);

    AppointmentResource getResourceByDateAndTherapyId(String jwt, Integer therapyId, String date);

    //Appointment updateDiagnosis(Integer appointmentId, String diagnosis);

}
