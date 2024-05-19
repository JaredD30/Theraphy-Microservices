package com.digitalholics.profileservice.Profile.domain.persistence;


import com.digitalholics.profileservice.Profile.domain.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PatientRepository extends JpaRepository<Patient,Integer> {

    /*
    @Query("""
        select t.id, t.age, t.appointmentQuantity, t.birthdayDate, t.photoUrl, t.user.id from Patient t inner join User u on t.user.id = u.id
                                                                 where t.user.firstname = :firstname
    """)
    Patient findByFirstName(String firstname);*/


    Patient findPatientByDni(String dni);
    Optional<Patient> findByUserId(Integer userId);
    Patient findPatientById(Integer patientId);

}
