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
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "physiotherapist_id")
    private Integer physiotherapistId;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String school;

    @NotNull
    private Integer year;



}
