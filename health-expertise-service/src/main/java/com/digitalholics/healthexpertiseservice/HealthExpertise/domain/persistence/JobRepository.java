package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>{
    Optional<Job> findById(Integer jobId);

    List<Job> findByPhysiotherapistId(Integer physiotherapistId);
}
