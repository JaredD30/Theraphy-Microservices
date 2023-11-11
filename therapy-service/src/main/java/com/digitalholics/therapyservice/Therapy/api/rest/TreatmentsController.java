package com.digitalholics.therapyservice.Therapy.api.rest;


import com.digitalholics.therapyservice.Therapy.domain.service.TreatmentService;
import com.digitalholics.therapyservice.Therapy.mapping.TreatmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.TreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.UpdateTreatmentResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/therapy/treatments", produces = "application/json")

public class TreatmentsController {
    private final TreatmentService treatmentService;

    private final TreatmentMapper mapper;

    public TreatmentsController(TreatmentService treatmentService, TreatmentMapper mapper) {
        this.treatmentService = treatmentService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<TreatmentResource> getAllTreatments(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(treatmentService.getAll(), pageable);
    }

    @GetMapping("{treatmentId}")
    public TreatmentResource getTreatmentById(@RequestHeader("Authorization") String jwt, @PathVariable Integer treatmentId) {
        return mapper.toResource(treatmentService.getById(treatmentId));
    }


    @GetMapping("byTherapyId/{therapyId}")
    public Page<TreatmentResource> getTreatmentByTherapyId(@RequestHeader("Authorization") String jwt, @PathVariable Integer therapyId, Pageable pageable){
        return mapper.modelListPage(treatmentService.getTreatmentByTherapyId(therapyId), pageable);
    }

    @GetMapping("byDate/{date}/TherapyId/{therapyId}")
    public  TreatmentResource getTreatmentByDateAndTherapyId(@RequestHeader("Authorization") String jwt, @PathVariable String date, @PathVariable Integer therapyId) {
        return mapper.toResource(treatmentService.getTreatmentByDateAndTherapyId(therapyId, date));
    }

    @PostMapping
    public ResponseEntity<TreatmentResource> createTreatment(@RequestHeader("Authorization") String jwt, @RequestBody CreateTreatmentResource resource) {
        return new ResponseEntity<>(mapper.toResource(treatmentService.create(jwt, resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{treatmentId}")
    public ResponseEntity<TreatmentResource> patchTreatment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer treatmentId,
            @RequestBody UpdateTreatmentResource request) {

        return new  ResponseEntity<>(mapper.toResource(treatmentService.update(treatmentId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{treatmentId}")
    public ResponseEntity<?> deleteTreatment(@RequestHeader("Authorization") String jwt, @PathVariable Integer treatmentId) {
        return treatmentService.delete(treatmentId);
    }



}
