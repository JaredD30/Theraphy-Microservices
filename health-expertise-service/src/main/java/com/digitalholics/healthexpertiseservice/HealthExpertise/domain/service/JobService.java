package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.service;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Job;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.CreateJobResource;
import com.digitalholics.healthexpertiseservice.HealthExpertise.resource.Job.UpdateJobResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobService {
    List<Job> getAll();
    Page<Job> getAll(Pageable pageable);
    Job getById(Integer jobId);
    List<Job> getByPhysiotherapistId(Integer physiotherapistId);
    Job create(String jwt, CreateJobResource job);
    Job update(String jwt, Integer jobId, UpdateJobResource request);

    Job updatePositionOrganization(String jwt, Integer jobId, UpdateJobResource request);

    ResponseEntity<?> delete(String jwt, Integer jobId);
}
