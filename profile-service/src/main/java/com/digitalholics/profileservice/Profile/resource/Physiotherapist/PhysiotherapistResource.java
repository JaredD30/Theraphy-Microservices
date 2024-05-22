package com.digitalholics.profileservice.Profile.resource.Physiotherapist;


import com.digitalholics.profileservice.Profile.domain.model.entity.External.User;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class PhysiotherapistResource {
    private Integer id;
    private String dni;
    private String specialization;
    private Integer age;
    private String location;
    private String photoUrl;
    private String birthdayDate;
    private Double rating;
    private Integer consultationQuantity;
    private Integer patientQuantity;
    private Integer yearsExperience;
    private Double fees;
    private User user;
}
