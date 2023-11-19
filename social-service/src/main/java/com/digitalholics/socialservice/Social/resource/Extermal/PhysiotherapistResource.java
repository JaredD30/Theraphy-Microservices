package com.digitalholics.socialservice.Social.resource.Extermal;


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
    private UserResource user;
}
