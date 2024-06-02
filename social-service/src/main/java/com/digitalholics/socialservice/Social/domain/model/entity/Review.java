package com.digitalholics.socialservice.Social.domain.model.entity;

import com.digitalholics.socialservice.Social.domain.model.entity.External.Patient;
import com.digitalholics.socialservice.Social.domain.model.entity.External.Physiotherapist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    private String content;

    @Max(5)
    @Min(1)
    private double score;

    private Integer physiotherapistId;

    private Integer patientId;

}
