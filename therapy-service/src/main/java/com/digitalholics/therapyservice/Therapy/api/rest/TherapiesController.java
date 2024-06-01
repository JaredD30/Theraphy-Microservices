package com.digitalholics.therapyservice.Therapy.api.rest;


import com.digitalholics.therapyservice.Therapy.domain.service.TherapyService;
import com.digitalholics.therapyservice.Therapy.mapping.TherapyMapper;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.CreateTherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.TherapyResource;
import com.digitalholics.therapyservice.Therapy.resource.Therapy.UpdateTherapyResource;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/therapy/therapies", produces = "application/json")
@Tag(name = "Therapies", description = "Therapies operations: listing, retrieval, creation, update, and deletion")
public class TherapiesController {

    private final TherapyService therapyService;

    private final TherapyMapper mapper;

    public TherapiesController(TherapyService therapyService, TherapyMapper mapper) {
        this.therapyService = therapyService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all therapies", description = "Returns therapies' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<TherapyResource> getAllTherapies(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        //return mapper.modelListPage(therapyService.getAll(), pageable);
        return therapyService.getAllResources(jwt, pageable);
    }

    @Operation(summary = "Get therapy by id", description = "Returns therapy with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{therapyId}")
    public TherapyResource getTherapyById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Therapy Id", required = true, examples = @ExampleObject(name = "therapyId", value = "1")) @PathVariable Integer therapyId) {
        //return mapper.toResource(therapyService.getById(therapyId));
        return therapyService.getResourceById(jwt,therapyId);
    }

    @Operation(summary = "Get therapy by patient id", description = "Returns therapy with a provided patient id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byPatientId/{patientId}")
    public Page<TherapyResource> getTherapyByPatientId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Patient Id", required = true, examples = @ExampleObject(name = "patientId", value = "1")) @PathVariable Integer patientId, Pageable pageable) {
        //return mapper.modelListPage(therapyService.getTherapyByPatientId(patientId), pageable);
        return therapyService.getResourceByPatientId(jwt,pageable,patientId);
    }

    @Operation(summary = "Get active therapy by jwt", description = "Returns an active therapy by jwt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("activeTherapyByPatientId")
    public TherapyResource getActiveTherapyByPatientId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt) {
       // return mapper.toResource(therapyService.getActiveTherapyByPatientId(jwt));

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita "Bearer " del token
        }
        return therapyService.getResourceActiveByPatientId(jwt);
    }

    @Operation(summary = "Create therapy", description = "Register a therapy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<TherapyResource> createTherapy(
            @RequestBody CreateTherapyResource resource,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Quita "Bearer " del token
        }
        return new ResponseEntity<>(mapper.toResource(therapyService.create(authorizationHeader, (resource))), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a therapy partially", description = "Updates a therapy partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{therapyId}")
    public ResponseEntity<TherapyResource> patchTherapy(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Therapy Id", required = true, examples = @ExampleObject(name = "therapyId", value = "1")) @PathVariable Integer therapyId,
            @RequestBody UpdateTherapyResource request) {

        return new  ResponseEntity<>(mapper.toResource(therapyService.update(therapyId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a therapy", description = "Delete a therapy with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{therapyId}")
    public ResponseEntity<?> deleteTherapy(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Therapy Id", required = true, examples = @ExampleObject(name = "therapyId", value = "1")) @PathVariable Integer therapyId) {
        return therapyService.delete(therapyId);
    }

    @GetMapping("byPhysioAndPatient/{physiotherapistId}/{patientId}")
    public TherapyResource getTherapyByPhysiotherapistIdAndPatientId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @PathVariable Integer patientId, @PathVariable Integer physiotherapistId){
        //return mapper.toResource((therapyService.getTherapyByPhysiotherapistIdAndPatientId(physiotherapistId, patientId)));
        return therapyService.getResourceByPhysiotherapistIdAndPatientId(jwt,physiotherapistId,patientId);
    }

}
