package com.digitalholics.socialservice;

import com.digitalholics.socialservice.Social.api.rest.ReviewsController;
import com.digitalholics.socialservice.Social.domain.service.ReviewService;
import com.digitalholics.socialservice.Social.mapping.ReviewMapper;
import com.digitalholics.socialservice.Social.resource.CreateReviewResource;
import com.digitalholics.socialservice.Social.resource.ReviewResource;
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

public class ReviewTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ReviewMapper mapper;

    @InjectMocks
    private ReviewsController reviewsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateReview() {
        CreateReviewResource createReviewResource = new CreateReviewResource();
        ReviewResource reviewResource = new ReviewResource();
        String authorizationHeader = "Bearer token";

        //when(reviewService.create(anyString(), any(CreateReviewResource.class))).thenReturn(reviewResource);
        when(mapper.toResource(any())).thenReturn(reviewResource);

        ResponseEntity<ReviewResource> response = reviewsController.createReview(authorizationHeader, createReviewResource);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reviewResource, response.getBody());
    }

    @Test
    void testGetReviewsByPhysiotherapistId() {
        Page<ReviewResource> reviewPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        String jwt = "Bearer token";

        when(reviewService.getResourceByPhysiotherapistId(anyString(), anyInt(), any(Pageable.class))).thenReturn(reviewPage);

        Page<ReviewResource> response = reviewsController.getReviewsByPhysiotherapistId(jwt, 1, pageable);

        assertEquals(reviewPage, response);
    }

}
