package com.digitalholics.therapyservice.Therapy.domain.persistence;

import com.digitalholics.therapyservice.Therapy.domain.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Appointment findByTopic(String topic);

    List<Appointment> findAppointmentByTherapyId(Integer therapyId);

    //@Query("select * from Appointment a inner join Theraphy b  on  a.theraphy.id = b.id where b.patientId = :patientId");
    @Query("select a from Appointment  a where a.therapy.patientId = :patientId")
    List<Appointment> findAppointmentsByTherapyByPatientId(Integer patientId);

    @Query("select a from Appointment  a where a.therapy.physiotherapistId = :physiotherapistId")
    List<Appointment> findAppointmentsByTherapyByPhysiotherapistId(Integer physiotherapistId);

    @Query("select a from Appointment  a where a.therapy.id = :therapyId and a.date = :date")
    Appointment findAppointmentByDateAndTherapyId(Integer therapyId, String date);

}
