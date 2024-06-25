package com.digitalholics.therapyservice.Therapy.domain.model.entity;


import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Patient;
import com.digitalholics.therapyservice.Therapy.domain.model.entity.External.Physiotherapist;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "therapies")
public class Therapy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotNull
    @NotBlank
    @Size(max = 300)
    private String therapyName;

    @NotNull
    @NotBlank
    private String description;


    private Integer appointmentQuantity;


    @NotNull
    @NotBlank
    private String startAt;

    @NotNull
    @NotBlank
    private String finishAt;

    @NotNull
    private Boolean finished;

    private Integer physiotherapistId;

    private Integer patientId;

}
