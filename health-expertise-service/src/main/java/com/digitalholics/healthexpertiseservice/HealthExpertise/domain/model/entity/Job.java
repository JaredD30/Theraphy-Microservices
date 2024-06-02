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
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @JoinColumn(name = "physiotherapist_id")
    private Integer physiotherapistId;

    @NotBlank
    @NotNull
    @Size(max = 50)
    private String position;

    @NotBlank
    @NotNull
    @Size(max = 50)
    private String organization;

}

