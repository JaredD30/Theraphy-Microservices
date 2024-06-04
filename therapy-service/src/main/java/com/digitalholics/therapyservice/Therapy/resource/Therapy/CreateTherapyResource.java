package com.digitalholics.therapyservice.Therapy.resource.Therapy;


import lombok.*;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
public class CreateTherapyResource {

    private Integer id;
    private String therapyName;
    private String description;
    private Integer appointmentQuantity;
    private String startAt;
    private String finishAt;
    private Boolean finished;
    private Integer patientId;
}
