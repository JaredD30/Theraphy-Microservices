package com.digitalholics.socialservice.Social.service;


import com.digitalholics.socialservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.socialservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.socialservice.Shared.JwtValidation.JwtValidator;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Patient;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Physiotherapist;
import com.digitalholics.socialservice.Social.domain.model.entity.External.User;
import com.digitalholics.socialservice.Social.domain.model.entity.Review;
import com.digitalholics.socialservice.Social.domain.persistence.External.PatientRepository;
import com.digitalholics.socialservice.Social.domain.persistence.External.PhysiotherapistRepository;
import com.digitalholics.socialservice.Social.domain.persistence.ReviewRepository;
import com.digitalholics.socialservice.Social.domain.service.ReviewService;
import com.digitalholics.socialservice.Social.resource.CreateReviewResource;
import com.digitalholics.socialservice.Social.resource.UpdateReviewResource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String ENTITY = "Review";

    private final ReviewRepository reviewRepository;
    private final PhysiotherapistRepository physiotherapistRepository;
    private final PatientRepository patientRepository;

    private final JwtValidator jwtValidator;
    private final Validator validator;

    public ReviewServiceImpl(ReviewRepository reviewRepository, PhysiotherapistRepository physiotherapistRepository, PatientRepository patientRepository, JwtValidator jwtValidator, Validator validator) {
        this.reviewRepository = reviewRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.patientRepository = patientRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
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
    public List<Review> getByPhysiotherapistId(Integer physiotherapistId) {
        List<Review> reviews = reviewRepository.findByPhysiotherapistId(physiotherapistId);

        if(reviews.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Reviews for this physiotherapist");

        return reviews;
    }

    @Override
    public Review create(String jwt, CreateReviewResource reviewResource) {
        Set<ConstraintViolation<CreateReviewResource>> violations = validator.validate(reviewResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PATIENT");

        Optional<Patient> patientOptional = Optional.ofNullable(patientRepository.findPatientsByUserUsername(user.getUsername()));
        Patient patient = patientOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));


        Optional<Physiotherapist> physiotherapistOptional = physiotherapistRepository.findById(reviewResource.getPhysiotherapistId());
        Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found physiotherapist with ID: " + reviewResource.getPhysiotherapistId()));

        Review review = new Review();
        review.setPatient(patient);
        review.setPhysiotherapist(physiotherapist);
        review.setContent(reviewResource.getContent());
        review.setScore(reviewResource.getScore());

        double ratingPhysiotherapist = 0.0;
        List<Review> reviewsPhysiotherapist = reviewRepository.findByPhysiotherapistId(physiotherapist.getId());

        for (Review existingReview : reviewsPhysiotherapist) {
            ratingPhysiotherapist = ratingPhysiotherapist + existingReview.getScore();
;        }
        ratingPhysiotherapist = ratingPhysiotherapist + review.getScore();
        ratingPhysiotherapist = ratingPhysiotherapist/(reviewsPhysiotherapist.size()+1) ;

        physiotherapist.setRating(ratingPhysiotherapist);

        physiotherapistRepository.save(physiotherapist);


        return reviewRepository.save(review);
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
