package com.digitalholics.healthexpertiseservice.HealthExpertise.service;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Job;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.PhysiotherapistRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External.UserRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.JobRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.JobService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.CreateJobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.UpdateJobResource;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.healthexpertiseservice.Shared.JwtValidation.JwtValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class JobServiceImpl implements JobService {
    private static final String ENTITY = "Job";

    private final JobRepository jobRepository;
    private final PhysiotherapistRepository physiotherapistRepository;
    private final JwtValidator jwtValidator;
    private final Validator validator;


    public JobServiceImpl(JobRepository jobRepository, PhysiotherapistRepository physiotherapistRepository, JwtValidator jwtValidator, Validator validator) {
        this.jobRepository = jobRepository;
        this.physiotherapistRepository = physiotherapistRepository;
        this.jwtValidator = jwtValidator;
        this.validator = validator;
    }

    @Override
    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    @Override
    public Page<Job> getAll(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @Override
    public Job getById(Integer jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(()-> new ResourceNotFoundException(ENTITY, jobId));
    }

    @Override
    public List<Job> getByPhysiotherapistId(Integer physiotherapistId) {
        List<Job> jobs = jobRepository.findByPhysiotherapistId(physiotherapistId);

        if(jobs.isEmpty())
            throw new ResourceValidationException(ENTITY,
                    "Not found Jobs for this physiotherapist");

        return jobs;
    }

    @Override
    public Job create(String jwt, CreateJobResource jobResource) {

        Set<ConstraintViolation<CreateJobResource>> violations = validator.validate(jobResource);

        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);


        User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        Optional<Physiotherapist> physiotherapistOptional = Optional.ofNullable(physiotherapistRepository.findPhysiotherapistByUserUsername(user.getUsername()));
        Physiotherapist physiotherapist = physiotherapistOptional.orElseThrow(() -> new NotFoundException("Not found patient with email: " + user.getUsername()));

        Job job= new Job();
        job.setPhysiotherapist(physiotherapist);
        job.setPosition(jobResource.getPosition());
        job.setOrganization(jobResource.getOrganization());

        return jobRepository.save(job);
    }

    @Override
    public Job update(String jwt, Integer jobId, UpdateJobResource request) {
        User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        Job job = getById(jobId);

        if(Objects.equals(user.getUsername(), job.getPhysiotherapist().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")) {
            if (request.getPosition() != null) {
                job.setPosition(request.getPosition());
            }
            if (request.getOrganization() != null) {
                job.setOrganization(request.getOrganization());
            }

            return jobRepository.save(job);
        }

        throw new ResourceValidationException("JWT",
                "Invalid access.");
    }

    @Override
    public ResponseEntity<?> delete(String jwt, Integer jobId) {

        User user = jwtValidator.validateJwtAndGetUser(jwt, "PHYSIOTHERAPIST");

        return jobRepository.findById(jobId).map(job -> {
            if(Objects.equals(user.getUsername(), job.getPhysiotherapist().getUser().getUsername()) || Objects.equals(String.valueOf(user.getRole()), "ADMIN")){
                jobRepository.delete(job);
                return ResponseEntity.ok().build();
            }
            throw new ResourceValidationException("JWT",
                    "Invalid access.");
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,jobId));
    }

}
