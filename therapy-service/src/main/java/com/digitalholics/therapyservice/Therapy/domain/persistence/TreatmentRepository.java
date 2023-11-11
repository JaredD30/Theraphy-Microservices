package com.digitalholics.therapyservice.Therapy.domain.persistence;


import com.digitalholics.therapyservice.Therapy.domain.model.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {

    List<Treatment> findTreatmentByTherapyId(Integer therapyId);

    @Query("select a from Treatment  a where a.therapy.id = :therapyId and a.day = :date")
    Treatment findTreatmentByDateAndTherapyId(Integer therapyId, String date);

}
