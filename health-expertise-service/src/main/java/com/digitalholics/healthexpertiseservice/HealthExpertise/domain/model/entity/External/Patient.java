package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External;

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

public class Patient {

    private Integer id;
    private String dni;
    private Integer age;
    private String photoUrl;
    private String birthdayDate;
    private Integer appointmentQuantity;
    private String location;
    private User user;

}
