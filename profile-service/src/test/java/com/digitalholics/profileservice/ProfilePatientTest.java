package com.digitalholics.profileservice;

import com.digitalholics.profileservice.Profile.api.rest.PatientsController;
import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.service.PatientService;
import com.digitalholics.profileservice.Profile.mapping.PatientMapper;
import com.digitalholics.profileservice.Profile.resource.Patient.CreatePatientResource;
import com.digitalholics.profileservice.Profile.resource.Patient.PatientResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ProfilePatientTest {

    @Mock
    private PatientService patientService;

    @Mock
    private PatientMapper mapper;

    @InjectMocks
    private PatientsController patientsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatient() {
        CreatePatientResource createPatientResource = new CreatePatientResource();
        Patient patient = new Patient();
        PatientResource patientResource = new PatientResource();
        String authorizationHeader = "Bearer token";

        when(patientService.create(any(CreatePatientResource.class), any(String.class))).thenReturn(patient);
        when(mapper.toResource(any(Patient.class))).thenReturn(patientResource);

        ResponseEntity<PatientResource> response = patientsController.createPatient(createPatientResource, authorizationHeader);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patientResource, response.getBody());
    }

    @Test
    void testGetPatientLogget() {
        String authorizationHeader = "Bearer token";
        PatientResource patientResource = new PatientResource();

        when(patientService.getLoggedInPatient(any(String.class))).thenReturn(patientResource);

        PatientResource response = patientsController.getPatientLogget(authorizationHeader);

        assertEquals(patientResource, response);
    }

    @Test
    void testGetPatientByUserId() {
        Patient patient = new Patient();
        PatientResource patientResource = new PatientResource();

        when(patientService.getByUserId(anyInt())).thenReturn(patient);
        when(mapper.toResource(any(Patient.class))).thenReturn(patientResource);

        PatientResource response = patientsController.getPatientByUserId(1);

        assertEquals(patientResource, response);
    }

    @Test
    void testGetPatientById() {
        Patient patient = new Patient();
        PatientResource patientResource = new PatientResource();

        when(patientService.getResourceById(anyInt())).thenReturn(patientResource);

        PatientResource response = patientsController.getPatientById(1);

        assertEquals(patientResource, response);
    }
}
