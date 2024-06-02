package com.digitalholics.healthexpertiseservice.HealthExpertise.domain.model.entity.External;


import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor

public class Consultation {

    private Integer id;
    private Boolean done;
    private String topic;
    private String diagnosis;
    private String date;
    private String hour;
    private String place;
    private Physiotherapist physiotherapist;
    private Patient patient;
}
