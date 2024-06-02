package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "diagnoses")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name = "physiotherapist_id")
    private Integer physiotherapistId;


    @JoinColumn(name = "patient_id")

    private Integer patientId;

    private String diagnosis;

    @NotNull
    @NotBlank
    private String date;
}
