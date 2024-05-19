package com.digitalholics.profileservice.Profile.domain.model.entity;

import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "physiotherapists")
public class Physiotherapist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 8, max = 8)
    private String dni;

    @Size(max = 50)
    private String specialization;

    @Min(18)
    private Integer age;

    private String location;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "birthday_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String birthdayDate;

    @Min(0)
    private Double rating;

    @Column(name = "consultation_quantity")
    private Integer consultationQuantity;

    @Column(name = "patient_quantity")
    private Integer patientQuantity;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Min(0)
    private Double fees;

    private Integer userId;

}
