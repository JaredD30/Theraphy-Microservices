package com.digitalholics.socialservice.Social.service;


import com.digitalholics.socialservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.socialservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.socialservice.Shared.configuration.ExternalConfiguration;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Patient;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Physiotherapist;
import com.digitalholics.socialservice.Social.domain.model.entity.External.User;
import com.digitalholics.socialservice.Social.domain.model.entity.Review;
import com.digitalholics.socialservice.Social.domain.persistence.ReviewRepository;
import com.digitalholics.socialservice.Social.domain.service.ReviewService;
import com.digitalholics.socialservice.Social.mapping.ReviewMapper;
import com.digitalholics.socialservice.Social.resource.CreateReviewResource;
import com.digitalholics.socialservice.Social.resource.ReviewResource;
import com.digitalholics.socialservice.Social.resource.UpdateReviewResource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String ENTITY = "Review";

    private final ReviewRepository reviewRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;
    private final ReviewMapper mapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, Validator validator, ExternalConfiguration externalConfiguration, ReviewMapper mapper) {
        this.reviewRepository = reviewRepository;
        this.validator = validator;
        this.externalConfiguration = externalConfiguration;
        this.mapper = mapper;
    }

    @Override
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Page<Review> getAll(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Review getById(Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, reviewId));
    }

    @Override
    public ReviewResource getResourceById(String jwt, Integer reviewId) {
        ReviewResource reviewResource = mapper.toResource(getById(reviewId));
        reviewResource.setPatient(externalConfiguration.getPatientByID(jwt, reviewResource.getPatient().getId()));
        reviewResource.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, reviewResource.getPhysiotherapist().getId()));
        return reviewResource;
    }

    @Override
    public List<Review> getByPhysiotherapistId(Integer physiotherapistId) {
        List<Review> reviews = reviewRepository.findByPhysiotherapistId(physiotherapistId);

        if(reviews.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Reviews for this physiotherapist");

        return reviews;
    }

    @Override
    public Page<ReviewResource> getResourceByPhysiotherapistId(String jwt, Integer physiotherapistId, Pageable pageable) {
        Page<ReviewResource> consultation =
                mapper.modelListPage(getByPhysiotherapistId(physiotherapistId), pageable);
        consultation.forEach(consultationResource -> {
            consultationResource.setPatient(externalConfiguration.getPatientByID(jwt, consultationResource.getPatient().getId()));
            consultationResource.setPhysiotherapist(externalConfiguration.getPhysiotherapistById(jwt, consultationResource.getPhysiotherapist().getId()));
        });

        return consultation;
    }

    @Override
    public Review create(String jwt, CreateReviewResource reviewResource) {
        Set<ConstraintViolation<CreateReviewResource>> violations = validator.validate(reviewResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = externalConfiguration.getUser(jwt);

        if (Objects.equals(String.valueOf(user.getRole()), "ADMIN") || Objects.equals(String.valueOf(user.getRole()), "PATIENT")) {
            Patient patient = externalConfiguration.getPatientByUserId(jwt, user.getId());
            Physiotherapist physiotherapist = externalConfiguration.getPhysiotherapistById(jwt, reviewResource.getPhysiotherapistId());

            Review review = new Review();
            review.setPatientId(patient.getId());
            review.setPhysiotherapistId(physiotherapist.getId());
            review.setContent(reviewResource.getContent());

            // Convertir el score a double
            double score = Double.parseDouble(reviewResource.getScore().toString());
            review.setScore(score);

            double rating = 0.0;
            List<Review> reviewsPhysiotherapist = reviewRepository.findByPhysiotherapistId(physiotherapist.getId());

            for (Review existingReview : reviewsPhysiotherapist) {
                rating += existingReview.getScore();
            }
            rating += review.getScore();
            rating /= (reviewsPhysiotherapist.size() + 1);

            // Actualizar el rating del fisioterapeuta
            externalConfiguration.updatePhysiotherapistRating(jwt, reviewResource.getPhysiotherapistId(), rating);

            return reviewRepository.save(review);
        }
        throw new ResourceValidationException(ENTITY, "Review not created, because you are not a patient.");
    }


    @Override
    public Review update( Integer reviewId, UpdateReviewResource request) {

        Review review = getById(reviewId);

        if (request.getContent() != null) {
            review.setContent(request.getContent());
        }
        if (request.getScore() != null) {
            review.setScore(request.getScore());
        }

        return reviewRepository.save(review);
    }


    @Override
    public ResponseEntity<?> delete(Integer reviewId) {
        return reviewRepository.findById(reviewId).map(review -> {
            reviewRepository.delete(review);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,reviewId));
    }
}
