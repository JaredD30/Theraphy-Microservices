package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.persistence.External;


import com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External.Physiotherapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysiotherapistRepository extends JpaRepository<Physiotherapist,Integer> {


    Physiotherapist findPhysiotherapistByDni(String dni);

    Physiotherapist findPhysiotherapistByUserUsername(String username);

    Optional<Physiotherapist> findByUserId(Integer userId);

}