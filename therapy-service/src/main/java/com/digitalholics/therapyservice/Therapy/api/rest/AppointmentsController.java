package com.digitalholics.therapyservice.Therapy.api.rest;


import com.digitalholics.therapyservice.Therapy.domain.service.AppointmentService;
import com.digitalholics.therapyservice.Therapy.mapping.AppointmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.AppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.CreateAppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.UpdateAppointmentResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/therapy/appointments", produces = "application/json")
@Tag(name = "Appointments", description = "Appointments operations: listing, retrieval, creation, update, and deletion")
public class AppointmentsController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper mapper;

    public AppointmentsController(AppointmentService appointmentService, AppointmentMapper mapper) {
        this.appointmentService = appointmentService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all appointments", description = "Returns appointments' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<AppointmentResource> getAllAppointments(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        //return mapper.modelListPage(appointmentService.getAll(), pageable);
        return appointmentService.getAllResources(jwt, pageable);
    }

    @Operation(summary = "Get appointment by id", description = "Returns appointment with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{appointmentId}")
    public AppointmentResource getAppointmentById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Appointment Id", required = true, examples = @ExampleObject(name = "appointmentId", value = "1")) @PathVariable Integer appointmentId
    ) {
        //return mapper.toResource(appointmentService.getById(appointmentId));
        return appointmentService.getResourceById(jwt, appointmentId);
    }

    @Operation(summary = "Get appointment's list by therapy id", description = "Returns appointment's list with a provide therapy id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byTherapyId/{therapyId}")
    public Page<AppointmentResource> getAppointmentByTherapyId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "therapy's id", required = true, examples = @ExampleObject(name = "TherapyId", value = "1")) @PathVariable Integer therapyId, Pageable pageable) {
       // return mapper.modelListPage(appointmentService.getAppointmentByTherapyId(therapyId), pageable);
        return appointmentService.getResourcesByTherapyId(jwt, pageable, therapyId);
    }

    @Operation(summary = "Get appointment's list by therapy by patient id", description = "Returns appointment's list with a provided therapy by patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("appointment/therapy-patient/{patientId}")
    public Page<AppointmentResource> getAppointmentsByTherapyByPatientId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "patient's id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId, Pageable pageable) {
        //return mapper.modelListPage(appointmentService.getAppointmentsByTherapyByPatientId(patientId),pageable);
        return appointmentService.getResourcesByPatientId(jwt, pageable, patientId);
    }

    @Operation(summary = "Get appointment's list by therapy by physiotherapist id", description = "Returns appointment's list with a provide therapy by physiohterapist id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("appointment/therapy-physiotherapist/{physiotherapistId}")
    public Page<AppointmentResource> getAppointmentsByTherapyByPhysiotherapistId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "physiotherapist's id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer physiotherapistId, Pageable pageable) {
        //return mapper.modelListPage(appointmentService.getAppointmentsByTherapyByPhysiotherapistId(physiotherapistId), pageable);
        return  appointmentService.getResourcesByPhysiotherapistId(jwt, pageable, physiotherapistId);
    }

    @Operation(summary = "Get appointment by date and therapy id", description = "Returns appointment with a provide therapy id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byDate/{date}/TherapyId/{therapyId}")
    public AppointmentResource getAppointmentByDateAndTherapyId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "date", required = true, examples = @ExampleObject(name = "date", value = "1")) @PathVariable String date, @Parameter(description = "physiotherapist's id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer therapyId) {
        //return mapper.toResource(appointmentService.getAppointmentByDateAndTherapyId(therapyId, date));
        return appointmentService.getResourceByDateAndTherapyId(jwt, therapyId, date);
    }

    @Operation(summary = "Create appointment", description = "Register an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<AppointmentResource> createAppointment(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @RequestBody CreateAppointmentResource resource) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita "Bearer " del token
        }
        return new ResponseEntity<>(mapper.toResource(appointmentService.create(jwt,(resource))), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an appointment partially", description = "Updates an appointment partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{appointmentId}")
    public ResponseEntity<AppointmentResource> patchAppointment(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "appointment Id", required = true, examples = @ExampleObject(name = "appointmentId", value = "1"))
            @PathVariable Integer appointmentId,
            @RequestBody UpdateAppointmentResource request) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita "Bearer " del token
        }
        return new  ResponseEntity<>(mapper.toResource(appointmentService.update(jwt,appointmentId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an appointment", description = "Delete an appointment with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{appointmentId}")
    public ResponseEntity<?> deleteAppointment(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "Appointment Id", required = true, examples = @ExampleObject(name = "appointmentId", value = "1")) @PathVariable Integer appointmentId) {
        return appointmentService.delete(appointmentId);
    }

//    @Operation(summary = "Update appointment's diagnosis", description = "Updates an appointment's diagnosis")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully updated"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
//    })
//    @PatchMapping("updateDiagnosis/{appointmentId}")
//    public ResponseEntity<AppointmentResource> updateConsultationDiagnosis(
//            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
//            @Parameter(description = "Appointment Id", required = true, examples = @ExampleObject(name = "appointmentId", value = "1")) @PathVariable Integer appointmentId,
//            @RequestBody String diagnosis) {
//
//        return new  ResponseEntity<>(mapper.toResource(appointmentService.updateDiagnosis(appointmentId,diagnosis)), HttpStatus.CREATED);
//    }

}
