package com.digitalholics.healthexpertiseservice.HealthExpertise.service;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.User;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Job;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.JobRepository;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service.JobService;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.CreateJobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.UpdateJobResource;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceNotFoundException;
import com.digitalholics.healthexpertiseservice.Shared.Exception.ResourceValidationException;
import com.digitalholics.healthexpertiseservice.Shared.configuration.ExternalConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class JobServiceImpl implements JobService {
    private static final String ENTITY = "Job";
    private final JobRepository jobRepository;
    private final Validator validator;
    private final ExternalConfiguration externalConfiguration;


    public JobServiceImpl(JobRepository jobRepository,  Validator validator, ExternalConfiguration externalConfiguration) {
        this.jobRepository = jobRepository;
        this.externalConfiguration = externalConfiguration;
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

        User user = externalConfiguration.getUser(jwt);
        Job job= new Job();
        job.setPhysiotherapistId(user.getId());
        job.setPosition(jobResource.getPosition());
        job.setOrganization(jobResource.getOrganization());

        return jobRepository.save(job);
    }

    @Override
    public Job update(String jwt, Integer jobId, UpdateJobResource request) {

            Job job = getById(jobId);

            if (request.getPosition() != null) {
                job.setPosition(request.getPosition());
            }
            if (request.getOrganization() != null) {
                job.setOrganization(request.getOrganization());

                return jobRepository.save(job);
            }

        throw new ResourceValidationException("JWT",
                "Invalid access.");
    }

    @Override
    public Job updatePositionOrganization(String jwt, Integer jobId, UpdateJobResource request) {

        Job job = getById(jobId);

        if(job == null)
            throw new ResourceValidationException(ENTITY,
                    "Not found Medical History with ID:"+ jobId);

        if (request.getPosition() != null) {
            job.setPosition(request.getPosition());
        }
        if (request.getOrganization() != null) {
            job.setOrganization(request.getOrganization());
        }

        return jobRepository.save(job);
    }
    @Override
    public ResponseEntity<?> delete(String jwt, Integer jobId) {

        return jobRepository.findById(jobId).map(job -> {
                jobRepository.delete(job);
                return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(ENTITY,jobId));
    }

}
