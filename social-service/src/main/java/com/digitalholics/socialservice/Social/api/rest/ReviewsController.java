package com.digitalholics.socialservice.Social.api.rest;


import com.digitalholics.socialservice.Social.domain.service.ReviewService;
import com.digitalholics.socialservice.Social.mapping.ReviewMapper;
import com.digitalholics.socialservice.Social.resource.CreateReviewResource;
import com.digitalholics.socialservice.Social.resource.ReviewResource;
import com.digitalholics.socialservice.Social.resource.UpdateReviewResource;

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
@RequestMapping(value = "/api/v1/social/reviews", produces = "application/json")
@Tag(name = "Reviews", description = "Reviews operations: listing, retrieval, registration, update and deletion")
public class ReviewsController {
    private final ReviewService reviewService;
    private final ReviewMapper mapper;

    public ReviewsController(ReviewService reviewService, ReviewMapper mapper) {
        this.reviewService = reviewService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get all reviews", description = "Returns reviews' list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public Page<ReviewResource> getAllReviews(
            @RequestHeader("Authorization") String jwt,
            Pageable pageable) {
        return mapper.modelListPage(reviewService.getAll(), pageable);
    }

    @Operation(summary = "Get review by id", description = "Returns review with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("{reviewId}")
    public ReviewResource getReviewById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Review Id", required = true, examples = @ExampleObject(name = "reviewId", value = "1")) @PathVariable Integer reviewId
    ) {
        return reviewService.getResourceById(jwt,reviewId);
    }

    @Operation(summary = "Get reviews by physiotherapist id", description = "Returns reviews with a provided physiotherapist id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("byPhysiotherapistId/{physiotherapistId}")
    public Page<ReviewResource> getReviewsByPhysiotherapistId(
            @Parameter(hidden = true)@RequestHeader("Authorization") String jwt,
            @Parameter(description = "Physiotherapist Id", required = true, examples = @ExampleObject(name = "physiotherapistId", value = "1")) @PathVariable Integer physiotherapistId, Pageable pageable
    ) {
        return reviewService.getResourceByPhysiotherapistId(jwt,physiotherapistId, pageable);
    }

    @Operation(summary = "Create review", description = "Register a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Not Found", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<ReviewResource> createReview(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody CreateReviewResource resource
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader = authorizationHeader.substring(7); // Quita "Bearer " del token
        }
        return new ResponseEntity<>(mapper.toResource(reviewService.create(authorizationHeader, resource)), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a review partially", description = "Updates a review partially based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @PatchMapping("{reviewId}")
    public ResponseEntity<ReviewResource> patchReview(
            @Parameter(hidden = true) @RequestHeader("Authorization") String jwt,
            @Parameter(description = "Review Id", required = true, examples = @ExampleObject(name = "reviewId", value = "1")) @PathVariable Integer reviewId,
            @RequestBody UpdateReviewResource request) {

        return new  ResponseEntity<>(mapper.toResource(reviewService.update(reviewId,request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a review", description = "Delete a review with a provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("{reviewId}")
    public ResponseEntity<?> deleteReview(@Parameter(hidden = true) @RequestHeader("Authorization") String jwt, @Parameter(description = "Review Id", required = true, examples = @ExampleObject(name = "reviewId", value = "1")) @PathVariable Integer reviewId) {
        return reviewService.delete(reviewId);
    }
}
