package com.digitalholics.therapyservice.Therapy.api.rest;


import com.digitalholics.therapyservice.Therapy.domain.service.AppointmentService;
import com.digitalholics.therapyservice.Therapy.mapping.AppointmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.AppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.CreateAppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.UpdateAppointmentResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/therapy/appointments", produces = "application/json")
public class AppointmentsController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper mapper;

    public AppointmentsController(AppointmentService appointmentService, AppointmentMapper mapper) {
        this.appointmentService = appointmentService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<AppointmentResource> getAllAppointments(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(appointmentService.getAll(), pageable);
    }

    @GetMapping("{appointmentId}")
    public AppointmentResource getAppointmentById(@RequestHeader("Authorization") String jwt, @PathVariable Integer appointmentId) {
        return mapper.toResource(appointmentService.getById(appointmentId));
    }

    @GetMapping("byTherapyId/{therapyId}")
    public Page<AppointmentResource> getAppointmentByTherapyId(@RequestHeader("Authorization") String jwt, @PathVariable Integer therapyId, Pageable pageable) {
        return mapper.modelListPage(appointmentService.getAppointmentByTherapyId(therapyId), pageable);
    }


    @GetMapping("appointment/therapy-patient/{patientId}")
    public Page<AppointmentResource> getAppointmentsByTherapyByPatientId(@RequestHeader("Authorization") String jwt, @PathVariable Integer patientId, Pageable pageable) {
        return mapper.modelListPage(appointmentService.getAppointmentsByTherapyByPatientId(patientId),pageable);
    }

    @GetMapping("appointment/therapy-physiotherapist/{physiotherapistId}")
    public Page<AppointmentResource> getAppointmentsByTherapyByPhysiotherapistId(@RequestHeader("Authorization") String jwt, @PathVariable Integer physiotherapistId, Pageable pageable) {
        return mapper.modelListPage(appointmentService.getAppointmentsByTherapyByPhysiotherapistId(physiotherapistId), pageable);
    }

    @GetMapping("byDate/{date}/TherapyId/{therapyId}")
    public AppointmentResource getAppointmentByDateAndTherapyId(@RequestHeader("Authorization") String jwt, @PathVariable String date, @PathVariable Integer therapyId) {
        return mapper.toResource(appointmentService.getAppointmentByDateAndTherapyId(therapyId, date));
    }

    @PostMapping
    public ResponseEntity<AppointmentResource> createAppointment(@RequestHeader("Authorization") String jwt, @RequestBody CreateAppointmentResource resource) {
        return new ResponseEntity<>(mapper.toResource(appointmentService.create(jwt,(resource))), HttpStatus.CREATED);
    }

    @PatchMapping("{appointmentId}")
    public ResponseEntity<AppointmentResource> patchAppointment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer appointmentId,
            @RequestBody UpdateAppointmentResource request) {

        return new  ResponseEntity<>(mapper.toResource(appointmentService.update(appointmentId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{appointmentId}")
    public ResponseEntity<?> deleteAppointment(@RequestHeader("Authorization") String jwt, @PathVariable Integer appointmentId) {
        return appointmentService.delete(appointmentId);
    }

    @PatchMapping("updateDiagnosis/{appointmentId}")
    public ResponseEntity<AppointmentResource> updateConsultationDiagnosis(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer appointmentId,
            @RequestBody String diagnosis) {

        return new  ResponseEntity<>(mapper.toResource(appointmentService.updateDiagnosis(appointmentId,diagnosis)), HttpStatus.CREATED);
    }

}
