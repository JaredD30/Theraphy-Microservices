package com.digitalholics.therapyservice.Therapy.api.rest;


import com.digitalholics.therapyservice.Therapy.domain.service.TherapyService;
import com.digitalholics.therapyservice.Therapy.mapping.TherapyMapper;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.UpdateTherapyResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/therapy/therapies", produces = "application/json")

public class TherapiesController {

    private final TherapyService therapyService;

    private final TherapyMapper mapper;

    public TherapiesController(TherapyService therapyService, TherapyMapper mapper) {
        this.therapyService = therapyService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<TherapyResource> getAllTherapies(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(therapyService.getAll(), pageable);
    }

    @GetMapping("{therapyId}")
    public TherapyResource getTherapyById(@RequestHeader("Authorization") String jwt, @PathVariable Integer therapyId) {
        return mapper.toResource(therapyService.getById(therapyId));
    }


    @GetMapping("byPatientId/{patientId}")
    public Page<TherapyResource> getTherapyByPatientId(@RequestHeader("Authorization") String jwt, @PathVariable Integer patientId, Pageable pageable) {
        return mapper.modelListPage(therapyService.getTherapyByPatientId(patientId), pageable);
    }

    @GetMapping("activeTherapyByPatientId")
    public TherapyResource getActiveTherapyByPatientId(@RequestHeader("Authorization") String jwt) {
        return mapper.toResource(therapyService.getActiveTherapyByPatientId(jwt));
    }

    @PostMapping
    public ResponseEntity<TherapyResource> createTherapy(@RequestHeader("Authorization") String jwt, @RequestBody CreateTherapyResource resource) {
        return new ResponseEntity<>(mapper.toResource(therapyService.create(jwt, (resource))), HttpStatus.CREATED);
    }

    @PatchMapping("{therapyId}")
    public ResponseEntity<TherapyResource> patchTherapy(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer therapyId,
            @RequestBody UpdateTherapyResource request) {

        return new  ResponseEntity<>(mapper.toResource(therapyService.update(therapyId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{therapyId}")
    public ResponseEntity<?> deleteTherapy(@RequestHeader("Authorization") String jwt, @PathVariable Integer therapyId) {
        return therapyService.delete(therapyId);
    }


}
