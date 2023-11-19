package com.digitalholics.socialservice.Social.api.rest;


import com.digitalholics.socialservice.Social.domain.service.ReviewService;
import com.digitalholics.socialservice.Social.mapping.ReviewMapper;
import com.digitalholics.socialservice.Social.resource.CreateReviewResource;
import com.digitalholics.socialservice.Social.resource.ReviewResource;
import com.digitalholics.socialservice.Social.resource.UpdateReviewResource;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/social/reviews", produces = "application/json")
@Tag(name = "Reviews", description = "Create, read, update and delete reviews   ")
public class ReviewsController {
    private final ReviewService reviewService;
    private final ReviewMapper mapper;

    public ReviewsController(ReviewService reviewService, ReviewMapper mapper) {
        this.reviewService = reviewService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<ReviewResource> getAllReviews(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return mapper.modelListPage(reviewService.getAll(), pageable);
    }

    @GetMapping("{reviewId}")
    public ReviewResource getReviewById(@RequestHeader("Authorization") String jwt, @PathVariable Integer reviewId) {
        return mapper.toResource(reviewService.getById(reviewId));
    }

    @GetMapping("byPhysiotherapistId/{physiotherapistId}")
    public Page<ReviewResource> getReviewsByPhysiotherapistId(@RequestHeader("Authorization") String jwt, @PathVariable Integer physiotherapistId, Pageable pageable) {
        return mapper.modelListPage(reviewService.getByPhysiotherapistId(physiotherapistId), pageable);
    }


    @PostMapping
    public ResponseEntity<ReviewResource> createReview(@RequestHeader("Authorization") String jwt, @RequestBody CreateReviewResource resource) {
        return new ResponseEntity<>(mapper.toResource(reviewService.create(jwt,resource)), HttpStatus.CREATED);
    }

    @PatchMapping("{reviewId}")
    public ResponseEntity<ReviewResource> patchReview(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Integer reviewId,
            @RequestBody UpdateReviewResource request) {

        return new  ResponseEntity<>(mapper.toResource(reviewService.update(reviewId,request)), HttpStatus.CREATED);
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<?> deleteReview(@RequestHeader("Authorization") String jwt, @PathVariable Integer reviewId) {
        return reviewService.delete(reviewId);
    }
}
