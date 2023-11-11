package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence;

import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification,Integer> {

    Optional<Certification> findById(Integer certificationId);

    List<Certification> findByPhysiotherapistId(Integer physiotherapistId);
}
