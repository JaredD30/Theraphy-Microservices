package com.digitalholics.therapyservice.Therapy.api.rest;


import com.digitalholics.therapyservice.Therapy.domain.service.TreatmentService;
import com.digitalholics.therapyservice.Therapy.mapping.TreatmentMapper;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.CreateTreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.TreatmentResource;
import com.digitalholics.therapyservice.Therapy.resource.Treatment.UpdateTreatmentResource;

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
@RequestMapping(value = "/api/v1/therapy/treatments", produces = "application/json")
@Tag(name = "Treatments", description = "Treatments operations: listing, retrieval, creation, update, and deletion")
public class TreatmentsController {
    private final TreatmentService treatmentService;

    private final TreatmentMapper mapper;

    public TreatmentsController(TreatmentService treatmentService, TreatmentMapper mapper) {
        this.treatmentService = treatmentService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all treatments", description = "Returns treatments' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<TreatmentResource> getAllTreatments(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        //return mapper.modelListPage(treatmentService.getAll(), pageable);
        return treatmentService.getAllResources(jwt,pageable);
    }

    @Operation(summary = "Get treatment by id", description = "Returns treatment with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{treatmentId}")
    public TreatmentResource getTreatmentById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Treatment Id", required = true, examples = @ExampleObject(name = "treatmentId", value = "1")) @PathVariable Integer treatmentId) {
       // return mapper.toResource(treatmentService.getById(treatmentId));
        return treatmentService.getResourceById(jwt,treatmentId);
    }

    @Operation(summary = "Get treatment's list by therapy id", description = "Returns treatment's list with a provided therapy id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byTherapyId/{therapyId}")
    public Page<TreatmentResource> getTreatmentByTherapyId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Therapy Id", required = true, examples = @ExampleObject(name = "therapyId", value = "1")) @PathVariable Integer therapyId, Pageable pageable){
        //return mapper.modelListPage(treatmentService.getTreatmentByTherapyId(therapyId), pageable);
        return treatmentService.getResourcesByTherapyId(jwt,pageable,therapyId);
    }

    @Operation(summary = "Get treatment by therapy id and by Date", description = "Returns a treatment with a provided therapy id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byDate/{date}/TherapyId/{therapyId}")
    public  TreatmentResource getTreatmentByDateAndTherapyId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Date", required = true, examples = @ExampleObject(name = "date", value = "25/10/2022")) @PathVariable String date,
            @Parameter(description = "Therapy Id", required = true, examples = @ExampleObject(name = "therapyId", value = "1")) @PathVariable Integer therapyId) {
      //  return mapper.toResource(treatmentService.getTreatmentByDateAndTherapyId(therapyId, date));
        return treatmentService.getResourceByDateAndTherapyId(jwt,therapyId,date);
    }

    @Operation(summary = "Create treatment", description = "Register a treatment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<TreatmentResource> createTreatment(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @RequestBody CreateTreatmentResource resource) {
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Quita "Bearer " del token
        }
        return new ResponseEntity<>(mapper.toResource(treatmentService.create(jwt, resource)), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a treatment partially", description = "Updates a treatment partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{treatmentId}")
    public ResponseEntity<TreatmentResource> patchTreatment(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Treatment Id", required = true, examples = @ExampleObject(name = "treatmentId", value = "1"))@PathVariable Integer treatmentId,
            @RequestBody UpdateTreatmentResource request) {

        return new  ResponseEntity<>(mapper.toResource(treatmentService.update(treatmentId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a treatment", description = "Delete a treatment with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{treatmentId}")
    public ResponseEntity<?> deleteTreatment(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "Treatment Id", required = true, examples = @ExampleObject(name = "treatmentId", value = "1")) @PathVariable Integer treatmentId) {
        return treatmentService.delete(treatmentId);
    }



}
