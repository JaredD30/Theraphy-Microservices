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
@Table(name = "patients")
public class Patient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 8, max = 8)
    private String dni;

    @Min(18)
    private Integer age;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "birthday_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String birthdayDate;

    @Min(0)
    private Integer appointmentQuantity;

    private String location;

    private Integer userId;
}
