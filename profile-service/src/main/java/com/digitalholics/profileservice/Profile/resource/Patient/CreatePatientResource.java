package com.digitalholics.profileservice.Profile.resource.Patient;

import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientResource {
    private String dni;
    private Integer age;
    private String photoUrl;
    private String birthdayDate;
    private Integer appointmentQuantity;
    private String location;
}