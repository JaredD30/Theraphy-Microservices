package com.digitalholics.therapyservice.Therapy.resource.Therapy;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTherapyResource {


    private String therapyName;
    private String description;
    private Integer appointmentQuantity;

    private String startAt;

    private String finishAt;
    private Boolean finished;
}
