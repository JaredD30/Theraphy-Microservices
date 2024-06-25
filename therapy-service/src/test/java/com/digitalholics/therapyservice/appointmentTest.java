package com.digitalholics.therapyservice;

import com.digitalholics.therapyservice.Therapy.api.rest.AppointmentsController;
import com.digitalholics.therapyservice.Therapy.domain.service.AppointmentService;
import com.digitalholics.therapyservice.Therapy.mapping.AppointmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.AppointmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Appointment.CreateAppointmentResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class appointmentTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AppointmentMapper mapper;

    @InjectMocks
    private AppointmentsController appointmentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAppointment() {
        CreateAppointmentResource createAppointmentResource = new CreateAppointmentResource();
        AppointmentResource appointmentResource = new AppointmentResource();
        String authorizationHeader = "Bearer token";

        //when(appointmentService.create(anyString(), any(CreateAppointmentResource.class))).thenReturn(appointmentResource);
        when(mapper.toResource(any())).thenReturn(appointmentResource);

        ResponseEntity<AppointmentResource> response = appointmentsController.createAppointment(authorizationHeader, createAppointmentResource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(appointmentResource, response.getBody());
    }

    @Test
    void testGetAppointmentByDateAndTherapyId() {
        AppointmentResource appointmentResource = new AppointmentResource();
        String jwt = "Bearer token";

        when(appointmentService.getResourceByDateAndTherapyId(anyString(), anyInt(), anyString())).thenReturn(appointmentResource);

        AppointmentResource response = appointmentsController.getAppointmentByDateAndTherapyId(jwt, "2024-06-05", 1);

        assertEquals(appointmentResource, response);
    }

    @Test
    void testGetAppointmentsByTherapyByPhysiotherapistId() {
        Page<AppointmentResource> appointmentPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(appointmentService.getResourcesByPhysiotherapistId(anyString(), any(Pageable.class), anyInt())).thenReturn(appointmentPage);

        Page<AppointmentResource> response = appointmentsController.getAppointmentsByTherapyByPhysiotherapistId(jwt, 1, pageable);

        assertEquals(appointmentPage, response);
    }

    @Test
    void testGetAppointmentsByTherapyByPatientId() {
        Page<AppointmentResource> appointmentPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(appointmentService.getResourcesByPatientId(anyString(), any(Pageable.class), anyInt())).thenReturn(appointmentPage);

        Page<AppointmentResource> response = appointmentsController.getAppointmentsByTherapyByPatientId(jwt, 1, pageable);

        assertEquals(appointmentPage, response);
    }

    @Test
    void testGetAppointmentByTherapyId() {
        Page<AppointmentResource> appointmentPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(appointmentService.getResourcesByTherapyId(anyString(), any(Pageable.class), anyInt())).thenReturn(appointmentPage);

        Page<AppointmentResource> response = appointmentsController.getAppointmentByTherapyId(jwt, 1, pageable);

        assertEquals(appointmentPage, response);
    }

    @Test
    void testGetAppointmentById() {
        AppointmentResource appointmentResource = new AppointmentResource();
        String jwt = "Bearer token";

        when(appointmentService.getResourceById(anyString(), anyInt())).thenReturn(appointmentResource);

        AppointmentResource response = appointmentsController.getAppointmentById(jwt, 1);

        assertEquals(appointmentResource, response);
    }
}
