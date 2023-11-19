package com.digitalholics.socialservice.Social.domain.service;

import com.digitalholics.socialservice.Social.domain.model.entity.Review;
import com.digitalholics.socialservice.Social.resource.CreateReviewResource;
import com.digitalholics.socialservice.Social.resource.UpdateReviewResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {

    List<Review> getAll();
    Page<Review> getAll(Pageable pageable);
    Review getById(Integer reviewId);
    List<Review> getByPhysiotherapistId(Integer physiotherapistId);
    Review create(String jwt, CreateReviewResource review);
    Review update(Integer reviewId, UpdateReviewResource request);
    ResponseEntity<?> delete(Integer reviewId);
}
