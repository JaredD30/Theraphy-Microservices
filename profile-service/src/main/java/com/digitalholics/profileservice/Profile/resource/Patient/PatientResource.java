package com.digitalholics.profileservice.Profile.resource.Patient;

import com.digitalholics.profileservice.Profile.resource.UserResource;
import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class PatientResource {
    private Integer id;
    private String dni;
    private Integer age;
    private String photoUrl;
    private String birthdayDate;
    private Integer appointmentQuantity;
    private String location;
    private UserResource user;
}
