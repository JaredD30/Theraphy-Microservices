package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medical_histories")
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name = "patient_id")
    private Integer patientId;

    @NotNull
    @NotBlank
    @Size(max = 10)
    private String gender;

    @NotNull
    private Double size;

    @NotNull
    private Double weight;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String birthplace;

    @Column(name = "hereditary_history")
    private String hereditaryHistory;

    @Column(name = "non_pathological_history")
    private String nonPathologicalHistory;

    @Column(name = "pathological_history")
    private String pathologicalHistory;
    
}
