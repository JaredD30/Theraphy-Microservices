package com.digitalholics.profileservice.Profile.domain.persistence;


import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import com.digitalholics.profileservice.Profile.domain.model.entity.Physiotherapist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhysiotherapistRepository extends JpaRepository<Physiotherapist,Integer> {


    Physiotherapist findPhysiotherapistByDni(String dni);


    Optional<Physiotherapist> findByUserId(Integer userId);

    Physiotherapist findPhysiotherapistById(Integer physiotherapyId);

}