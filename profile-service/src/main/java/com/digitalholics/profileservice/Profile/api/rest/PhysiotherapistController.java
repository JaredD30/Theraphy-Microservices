package com.digitalholics.profileservice.Profile.api.rest;



import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import com.digitalholics.profileservice.Profile.domain.service.PhysiotherapistService;
import com.digitalholics.profileservice.Profile.mapping.PhysiotherapistMapper;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.CreatePhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.PhysiotherapistResource;
import com.digitalholics.profileservice.Profile.resource.Physiotherapist.UpdatePhysiotherapistResource;
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
@RequestMapping(value = "/api/v1/profile/physiotherapists", produces = "application/json")
@Tag(name = "Physiotherapists", description = "Physiotherapists operations: profile, listing, retrieval, validation, registration, update, and deletion")
public class PhysiotherapistController {
    private final PhysiotherapistService physiotherapistService;

    private final PhysiotherapistMapper mapper;

    public PhysiotherapistController(PhysiotherapistService physiotherapistService, PhysiotherapistMapper mapper) {
        this.physiotherapistService = physiotherapistService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all physiotherapist", description = "Returns physiotherapist's list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<PhysiotherapistResource> getAllPPhysiotherapist( @Parameter(hidden = true) @RequestHeader("Authorization") String jwt, Pageable pageable) {
        //return mapper.modelListPage(physiotherapistService.getAll(), pageable);
        return physiotherapistService.getAllPhysiotherapist(jwt, pageable);
    }

    @Operation(summary = "Get physiotherapist by id", description = "Returns physiotherapist with a provide id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{physiotherapistId}")
    //@PreAuthorize("hasAuthority('patient:read')")
    public PhysiotherapistResource getPhysiotherapistById(
            @PathVariable Integer physiotherapistId)
    {
        return physiotherapistService.getResourceById(physiotherapistId);
    }

    @GetMapping("/PhysiotherapistLogget")
    public PhysiotherapistResource getPatientLogget(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Quita "Bearer " del token
        }
        return physiotherapistService.getLoggedInPhysiotherapist(authorizationHeader);
    }

    @Operation(summary = "Get physiotherapist by user id", description = "Returns physiotherapist with a provide user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byUserId/{userId}")
    public PhysiotherapistResource getPhysiotherapistByUserId(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "User Id", required = true, examples = @ExampleObject(name = "userId", value = "1")) @PathVariable Integer userId
    ) {
        return mapper.toResource(physiotherapistService.getByUserId(userId));
    }

    @Operation(summary = "Registration physiotherapist", description = "Register a physiotherapist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping("registration-physiotherapist")
    public ResponseEntity<PhysiotherapistResource> createPhysiotherapist(
            @RequestBody CreatePhysiotherapistResource resource,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Quita "Bearer " del token
        }
        return new ResponseEntity<>(mapper.toResource(physiotherapistService.create(resource,authorizationHeader)), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a physiotherapist partially", description = "Updates a physiotherapist partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{physiotherapistId}")
    public ResponseEntity<PhysiotherapistResource> patchPhysiotherapist(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Physiotherapist Id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer physiotherapistId,
            @RequestBody UpdatePhysiotherapistResource request) {

        return new  ResponseEntity<>(mapper.toResource(physiotherapistService.update(physiotherapistId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a physiotherapist", description = "Delete a physiotherapist with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{physiotherapistId}")
    public ResponseEntity<?> deletePhysiotherapist(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Physiotherapist Id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer physiotherapistId) {
        return physiotherapistService.delete(physiotherapistId);
    }

    @PostMapping("/{physiotherapistsId}/rating")
    public Physiotherapist updatePhysiotherapistReviews(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer physiotherapistsId,
            @RequestBody Double reviews) {
        return physiotherapistService.updatePhysiotherapistRating(jwt, physiotherapistsId, reviews);
    }

    @PostMapping("/{physiotherapistsId}/consultationQuantity")
    public Physiotherapist updatePhysiotherapistConsultationQuantity(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer physiotherapistsId,
            @RequestBody Integer consultationQuantity) {
        return physiotherapistService.updatePhysiotherapistConsultationQuantity(jwt, physiotherapistsId, consultationQuantity);
    }

    @PostMapping("/{physiotherapistsId}/patientsQuantity")
    public Physiotherapist updatePhysiotherapistPatientsQuantity(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer physiotherapistsId,
            @RequestBody Integer patientsQuantity) {
        return physiotherapistService.updatePhysiotherapistPatientQuantity(jwt, physiotherapistsId, patientsQuantity);
    }
}
